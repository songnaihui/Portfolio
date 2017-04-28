package com.ice.test.vo;

import java.util.HashSet;
import java.util.Set;

public class PortfolioVO {
	private long id;
	private String portfolioName;
	private String userName;
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	private Set<PositionVO> positions = new HashSet<PositionVO>();
	public Set<PositionVO> getPositions() {
		return positions;
	}

	public void setPositions(Set<PositionVO> positions) {			
		this.positions = positions;
	}
	
	public int hashCode(){
		return Long.hashCode(this.id);
	}

}
