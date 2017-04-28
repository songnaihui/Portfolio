package com.ice.test.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ice.test.model.Portfolio;
import com.ice.test.model.Position;
import com.ice.test.service.PortfolioService;
import com.ice.test.vo.PortfolioVO;

@RestController
@RequestMapping("/portfolios")
public class PortfolioController {
	@Autowired
	private PortfolioService portfolioService;
	
	@RequestMapping("/")
    public String index() {
        return "index";
    }
	/**
	 * get all portfolios
	 * @return List of portfolio value object
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Collection<PortfolioVO> getPortfolios() {
		return portfolioService.getPortfolios();
	}
	/**
	 * Fetch one portfolio
	 * @param id
	 * @return PortfolioVO value object of portfolio
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<PortfolioVO> getPortfolio(@PathVariable long id) {
		PortfolioVO portfolio = portfolioService.getPortfolio(id);
		return new ResponseEntity<PortfolioVO>(portfolio, HttpStatus.OK);
	}
	/**
	 * Save portfolio entity
	 * @param portfolio
	 * @return PortfolioVO value object of portfolio
	 */
	@RequestMapping(method = RequestMethod.POST)
	public PortfolioVO savePortfolio(@RequestBody Portfolio portfolio) {	
		return portfolioService.savePortfolio(portfolio);
	
	}
	/**
	 * Add one position to one portfolio
	 * @param id portfolio id
	 * @param position position entity 
	 * @return PortfolioVO value object of portfolio
	 */
	@RequestMapping(value = "/{id}",method = RequestMethod.POST)
	public PortfolioVO addPosition(@PathVariable long id,@RequestBody Position position) {	
		return portfolioService.addPosition(id, position);	
	}
	
    /**
     * Delete one position from portfolio
     * @param portfolioId 
     * @param positionId
     * @return PortfolioVO value object of portfolio
     */
	@RequestMapping(value = "/{portfolioId}/{positionId}", method = RequestMethod.DELETE)
	public PortfolioVO deletePortfolioPosition(@PathVariable long portfolioId,@PathVariable long positionId) {
		return portfolioService.removePosition(portfolioId, positionId);		
	}
	
	@RequestMapping(value = "/{portfolioId}", method = RequestMethod.DELETE)
	public Portfolio deletePortfolio(@PathVariable long portfolioId) {
		return portfolioService.removePortfolio(portfolioId);	
	}
	
	
}