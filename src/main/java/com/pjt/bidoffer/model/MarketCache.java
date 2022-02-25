package com.pjt.bidoffer.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class MarketCache {
	public Map<Instrument, InstrumentOrders> globalOrderCache = new ConcurrentHashMap<>();
	public int keepOrders;

	public Map<Instrument, InstrumentOrders> getGlobalOrderCache() {
		return globalOrderCache;
	}

	public void setGlobalOrderCache(Map<Instrument, InstrumentOrders> globalOrderCache) {
		this.globalOrderCache = globalOrderCache;
	}

}
