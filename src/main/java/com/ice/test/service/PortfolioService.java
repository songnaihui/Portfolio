package com.ice.test.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ice.test.model.Portfolio;
import com.ice.test.model.Position;
import com.ice.test.repository.PortfolioRepository;
import com.ice.test.vo.PortfolioVO;
import com.ice.test.vo.PositionVO;

@Service
public class PortfolioService {
	@Autowired
	private PortfolioRepository portfolioRepo;

	public PortfolioVO getPortfolio(Long portfolioId) {
		Portfolio portfolioEntity = portfolioRepo.findOne(portfolioId);
		return convertEntityToVO(portfolioEntity);
	}

	public PortfolioVO addPosition(Long portfolioId, Position position) {
		Portfolio portfolioEntity = portfolioRepo.findOne(portfolioId);
		portfolioEntity.getPositions().add(position);
		savePortfolio(portfolioEntity);
		return getPortfolio(portfolioId);
	}

	public PortfolioVO savePortfolio(Portfolio portfolio) {
		if(portfolio.getPortfolioName().equals("New")){
			/**
			 * Change the portfolio name for new create portfolio
			 */
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m");
			portfolio.setPortfolioName(format.format(new Date()));
		}
		for (Position position : portfolio.getPositions()) {
			position.setPortfolio(portfolio);
		}
		portfolioRepo.save(portfolio);
		return getPortfolio(portfolio.getId());
	}
	public Portfolio removePortfolio(long portfolioId) {
		Portfolio portfolio = portfolioRepo.findOne(portfolioId);
		if(portfolio!=null){
		  portfolioRepo.delete(portfolioId);
		}
		return portfolio;
	}
	public PortfolioVO removePosition(long portfolioId,long positionId) {
		Portfolio portfolio = portfolioRepo.findOne(portfolioId);
		CopyOnWriteArrayList<Position> positions = new CopyOnWriteArrayList<Position>( portfolio.getPositions() );
		for (Position position : positions) {
			if(position.getId()==positionId){
				portfolio.getPositions().remove(position);
				position.setPortfolio(null);
			}
		
		}
		portfolioRepo.save(portfolio);
		return getPortfolio(portfolio.getId());
	}

	public Collection<PortfolioVO> getPortfolios() {

		Collection<Portfolio> allPortfolio = portfolioRepo.findAll();
		
			List<PortfolioVO> portfolioVOList = new ArrayList<PortfolioVO>(allPortfolio.size()+1);
			for (Portfolio portfolioEntity : allPortfolio) {
				portfolioVOList.add(convertEntityToVO(portfolioEntity));
			}
			/**
			 * Add empty portfolio to let the user can add new portfolio
			 */
			PortfolioVO portfolio=new PortfolioVO();
			portfolio.setPortfolioName("New");
			portfolioVOList.add(portfolio);
			return portfolioVOList;
	}

	private PortfolioVO convertEntityToVO(Portfolio portfolioEntity) {
		if(portfolioEntity==null){
			return null;
		}
		PortfolioVO portfolioVO = new PortfolioVO();
		portfolioVO.setPortfolioName(portfolioEntity.getPortfolioName());
		portfolioVO.setUserName(portfolioEntity.getUserName());
		portfolioVO.setId(portfolioEntity.getId());
		Set<PositionVO> positions = new HashSet<PositionVO>();
		for (Position position : portfolioEntity.getPositions()) {
			PositionVO positionVo = new PositionVO();
			positionVo.setQuotePrice(position.getQuotePrice());
			positionVo.setTicker(position.getTicker());
			positionVo.setQuoteSize(position.getQuoteSize());
			positionVo.setId(position.getId());
			positions.add(positionVo);
		}
		portfolioVO.setPositions(positions);
		return portfolioVO;
	}
}
