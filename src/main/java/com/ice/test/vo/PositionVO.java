package com.ice.test.vo;

public class PositionVO {
	private long id;
	private String ticker;
	private int quoteSize;
	private double quotePrice;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTicker() {
		return ticker;
	}
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	public int getQuoteSize() {
		return quoteSize;
	}
	public void setQuoteSize(int quoteSize) {
		this.quoteSize = quoteSize;
	}
	public double getQuotePrice() {
		return quotePrice;
	}
	public void setQuotePrice(double quotePrice) {
		this.quotePrice = quotePrice;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(id);
		result = prime * result + ((ticker == null) ? 0 : ticker.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PositionVO other = (PositionVO) obj;
		if (id != other.id)
			return false;
		if (ticker == null) {
			if (other.ticker != null)
				return false;
		} else if (!ticker.equals(other.ticker))
			return false;
		return true;
	}
	
}
