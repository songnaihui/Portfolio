package com.ice.test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.ice.test.model.Portfolio;
import com.ice.test.model.Position;
import com.ice.test.vo.PortfolioVO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class PortfolioRestControllerTest {
	@Test
	public void testPortfolio() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		Portfolio portfolio=new Portfolio();
		portfolio.setPortfolioName("Test");		
		Position firstPosition=new Position();
		firstPosition.setTicker("AAPL");
		firstPosition.setQuotePrice(102.23);
		firstPosition.setQuoteSize(100);		
		Position secondPosition=new Position();
		secondPosition.setTicker("AAPL");
		secondPosition.setQuotePrice(102.23);
		secondPosition.setQuoteSize(1000);		
		Position thirdPosition=new Position();
		thirdPosition.setTicker("BABA");
		thirdPosition.setQuotePrice(102.23);
		thirdPosition.setQuoteSize(1000);
		portfolio.getPositions().add(firstPosition);
		portfolio.getPositions().add(secondPosition);
		portfolio.getPositions().add(thirdPosition);
		/**
		 * Check the duplicate
		 */
		assertEquals(portfolio.getPositions().size(),2);
		/**
		 * insert new portfolio
		 */
		PortfolioVO newPortfolio=restTemplate.postForObject("http://127.0.0.1:8080/portfolios", portfolio, PortfolioVO.class);
		PortfolioVO vo1 = restTemplate.getForObject("http://127.0.0.1:8080/portfolios/"+newPortfolio.getId(), PortfolioVO.class);
		/**
		 * Check the portfolio is created
		 */
		assertEquals(vo1.getPortfolioName(),"Test");
		/**
		 * Delete portfolio
		 */
		restTemplate.delete("http://127.0.0.1:8080/portfolios/"+newPortfolio.getId(), PortfolioVO.class);
		/**
		 * Get the portfolio
		 */
		vo1 = restTemplate.getForObject("http://127.0.0.1:8080/portfolios/"+newPortfolio.getId(), PortfolioVO.class);
		/**
		 * Verify the portfolio is deleted
		 */
		assertNull(vo1);
			
	}

}
