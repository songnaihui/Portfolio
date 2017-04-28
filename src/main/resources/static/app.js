var portfolioApp = angular.module('portfolioApp', []);
var configData = {
    endpoint: 'http://localhost:8080/portfolios'
};
portfolioApp.constant('CONFIG', configData);
portfolioApp.service('marketDataService', function($http, $q) {
    this.loadMarketData = function(symbolList) {
        var api = "https://query.yahooapis.com/v1/public/yql?q=select+*+from+yahoo.finance.quote+where+symbol+in(";
        var quotedAndCommaSeparated = "'" + symbolList.join("','") + "'";
        api = api + quotedAndCommaSeparated + ")&format=json&diagnostics=true&env=store://datatables.org/alltableswithkeys";
        var deferred = $q.defer();
        $http.get(api).then(function(response) {
        	if(response.data.query.results!=null){
              deferred.resolve(response.data.query.results.quote);
        	}
        });
        return deferred.promise;
    };
});
portfolioApp.service('portfolioService', function($http, $q, $rootScope, CONFIG) {
    this.loadAllPorfolios = function() {
        var api = CONFIG.endpoint;
        $http.get(api).then(function(response) {
            $rootScope.portfolios = response.data;
            var allTickers = [];
            response.data.forEach(function(portfolio) {
                portfolio.positions.forEach(function(position) {
                    if (allTickers.indexOf(position.ticker) < 0) {
                        allTickers.push(position.ticker);
                    }
                });
            });
            $rootScope.allTickers = allTickers;
        });
    };
});

portfolioApp.directive("portfolio", function(marketDataService,portfolioService) {
    var controller = ['$rootScope','$scope', '$http', 'CONFIG', function($rootScope,$scope, $http, CONFIG) {
        function removePositionFromUI(portfolio, position) {
            var allPositionsInPortfolio = portfolio.positions;
            var indexOfItem = -1;
            for (var i = 0; i < allPositionsInPortfolio.length; i++) {
                if (allPositionsInPortfolio[i].ticker == position.ticker) {
                    indexOfItem = i;
                    break;
                }
            }
            if (indexOfItem >= 0) {
                allPositionsInPortfolio.splice(indexOfItem, 1);
            }
        }
        /**
         * utility function to add one security to portfolio
         */
        function addPositionToPortfolio(portfolio, security) {
            if (security.Name) {
                var allPositionsInPortfolio = portfolio.positions;
                var containSecurity = false;
                for (var i = 0; i < allPositionsInPortfolio.length; i++) {
                    if (allPositionsInPortfolio[i].ticker == security.symbol) {
                        containSecurity = true;
                        break;
                    }
                }
                if (containSecurity) {
                    $scope.invalidTikers = "Already in portfolio";
                } else {
                    var position = {
                        "quoteSize": 0,
                        "ticker": security.symbol,
                        "companyName": security.Name,
                        "lastTradePriceOnly": security.LastTradePriceOnly,
                        "previouTradePrice": security.LastTradePriceOnly,
                        "quotePrice": security.LastTradePriceOnly
                    };
                    portfolio.positions.push(position);
                    /**
                     * If security added to portfolio succesfully, add ticker name as candidate of refresh security
                     */
                    if($rootScope.allTickers.indexOf(security.symbol)<0){
                    	$rootScope.allTickers.push(security.symbo);
                    }               
                    $scope.invalidTikers = null;
                }

            } else {
                $scope.invalidTikers = "Invalid Ticker";
            }
        }
        /**
         * delete position from one portfolio, system will remove position after position successfully remove from API calling 
         */
        $scope.deletePosition = function(position) {
                if (position.id) {
                    var deleteEndpoint = CONFIG.endpoint + "/" + $scope.portfolio.id + "/" + position.id;
                    $http({
                        method: 'DELETE',
                        url: deleteEndpoint,
                        data: $scope.portfolio
                    }).then(
                        function successCallback(response) {
                            removePositionFromUI($scope.portfolio, position);
                        }
                    );
                } else {
                    removePositionFromUI($scope.portfolio, position);
                }


            },
            /**
             * add position based on ticker name list
             */
            $scope.addPosition = function() {
                var ticketName = $scope.tickername;
                var portfolio = $scope.portfolio;
                marketDataService.loadMarketData(ticketName.split(",")).then(
                    function(result) {
                        if (angular.isArray(result)) {
                            result.forEach(function(security) {
                                addPositionToPortfolio(portfolio, security);
                            });
                        } else {
                            addPositionToPortfolio(portfolio, result);
                        }
                    }
                );

            },
            /**
             * save portfolio, system will save all positions in portfolio list
             */
            $scope.savePortfolio = function() {
                $http({
                    method: 'POST',
                    url: CONFIG.endpoint,
                    data: $scope.portfolio
                }).then(function successCallback(response) {
                	portfolioService.loadAllPorfolios();
                });
                var ticketName = $scope.tickername;
                var id = $scope.portfolio.id;
            }


    }];
    return {
        restrict: "E",
        templateUrl: "portfoliotemplate.html",
        controller: controller
    }

})
portfolioApp.run(function($interval, $rootScope, marketDataService, portfolioService) {
    portfolioService.loadAllPorfolios();
    $interval(function() {
        if ($rootScope.allTickers.length == 0) {
            return;
        }
        marketDataService.loadMarketData($rootScope.allTickers).then(
            function(result) {
                var marketData = {};
                if (angular.isArray(result)) {
                   result.forEach(function(security) {
                      marketData[security.symbol] = security
                   });
                }else{
                	marketData[result.symbol] = result;
                }
                $rootScope.marketData = marketData;
                $rootScope.portfolios.forEach(function(portfolio) {
                    portfolio.positions.forEach(function(position) {
                        var tickerMarketData = $rootScope.marketData[position.ticker];
                        if (tickerMarketData != null) {
                            position.companyName = tickerMarketData.Name;
                            position.previouTradePrice = position.lastTradePriceOnly;
                            position.lastTradePriceOnly = tickerMarketData.LastTradePriceOnly;                  
                           
                        }

                    });
                });
            }
        );
    }, 5000);
});