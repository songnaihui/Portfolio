package com.ice.test.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Portfolio {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "portfolio_id")
	private long id;
	private String portfolioName;
	private String userName;

	public long getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	@OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
	private Set<Position> positions = new HashSet<Position>();
	public Set<Position> getPositions() {
		return positions;
	}

	public void setPositions(Set<Position> positions) {			
		this.positions = positions;
	}

}