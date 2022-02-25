package com.pjt.bidoffer.model;

import java.util.concurrent.ConcurrentSkipListSet;

public class InstrumentOrders {

	private ConcurrentSkipListSet<Order> bidSet;
	private ConcurrentSkipListSet<Order> askSet;

	public ConcurrentSkipListSet<Order> getBidSet() {
		return bidSet;
	}

	public void setBidSet(ConcurrentSkipListSet<Order> bidSet) {
		this.bidSet = bidSet;
	}

	public ConcurrentSkipListSet<Order> getAskSet() {
		return askSet;
	}

	public void setAskSet(ConcurrentSkipListSet<Order> askSet) {
		this.askSet = askSet;
	}

	public InstrumentOrders() {
		this.bidSet = new ConcurrentSkipListSet<>();
		this.askSet = new ConcurrentSkipListSet<>();
	}
	
	@Override
	public String toString() {
		return "InstrumentOrders [bidSet=" + bidSet + ", askSet=" + askSet + "]";
	}

}
