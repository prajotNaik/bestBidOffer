package com.pjt.bidoffer.model;

import java.util.List;

public class MarketOrder {

	private String instrumentId;
	private String instrumentIdentifierType;
	private List<Order> askLevels;
	private List<Order> bidLevels;

	
	public MarketOrder(String instrumentId, String instrumentIdentifierType, List<Order> askLevels,
			List<Order> bidLevels) {
		super();
		this.instrumentId = instrumentId;
		this.instrumentIdentifierType = instrumentIdentifierType;
		this.askLevels = askLevels;
		this.bidLevels = bidLevels;
	}

	public String getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(String instrumentId) {
		this.instrumentId = instrumentId;
	}

	public String getInstrumentIdentifierType() {
		return instrumentIdentifierType;
	}

	public void setInstrumentIdentifierType(String instrumentIdentifierType) {
		this.instrumentIdentifierType = instrumentIdentifierType;
	}

	public List<Order> getAskLevels() {
		return askLevels;
	}

	public void setAskLevels(List<Order> askLevels) {
		this.askLevels = askLevels;
	}

	public List<Order> getBidLevels() {
		return bidLevels;
	}

	public void setBidLevels(List<Order> bidLevels) {
		this.bidLevels = bidLevels;
	}

	@Override
	public String toString() {
		return "MarketOrder [instrumentId=" + instrumentId + ", instrumentIdentifierType=" + instrumentIdentifierType
				+ ", askLevels=" + askLevels + ", bidLevels=" + bidLevels + "]";
	}

	
	public Instrument getIntrument() {
		return new Instrument(getInstrumentId(), getInstrumentIdentifierType());
	}
	
	
}
