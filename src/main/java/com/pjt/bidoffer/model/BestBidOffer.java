package com.pjt.bidoffer.model;

public class BestBidOffer {

	private Instrument instrument;
	private Order askOrder;
	private Order bidOrder;

	public Instrument getInstrument() {
		return instrument;
	}

	public Order getAskOrder() {
		return askOrder;
	}

	public Order getBidOrder() {
		return bidOrder;
	}

	public BestBidOffer(Instrument instrument, Order askOrder, Order bidOrder) {
		super();
		this.instrument = instrument;
		this.askOrder = askOrder;
		this.bidOrder = bidOrder;
	}

}
