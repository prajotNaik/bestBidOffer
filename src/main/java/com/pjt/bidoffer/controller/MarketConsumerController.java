package com.pjt.bidoffer.controller;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pjt.bidoffer.model.InstrumentOrders;
import com.pjt.bidoffer.model.MarketCache;
import com.pjt.bidoffer.model.MarketOrder;
import com.pjt.bidoffer.model.Order;

@RequestMapping("/market")
@RestController
public class MarketConsumerController {

	@Autowired
	public MarketCache marketCache;

	@PostMapping("")
	public void consumeMarketCondition(@RequestBody MarketOrder marketOrders) {
		InstrumentOrders instrumentOrders = marketCache.globalOrderCache.get(marketOrders.getIntrument());
		if (null == instrumentOrders) {
			instrumentOrders = new InstrumentOrders();
			marketCache.globalOrderCache.put(marketOrders.getIntrument(), instrumentOrders);
		}

		System.out.println(marketOrders);
		setAskOrder(instrumentOrders, marketOrders.getAskLevels());
		setBidOrder(instrumentOrders, marketOrders.getBidLevels());
		
		System.out.println(marketCache.globalOrderCache);
		
		System.out.println("Best Ask : " + marketCache.globalOrderCache.get(marketOrders.getIntrument()).getAskSet().first());
		System.out.println("Best Bid : " + marketCache.globalOrderCache.get(marketOrders.getIntrument()).getBidSet().last());
	}

	protected synchronized void setAskOrder(final InstrumentOrders instrumentOrders, List<Order> orders) {
		orders.forEach(o -> instrumentOrders.getAskSet().add(o));
		if(marketCache.keepOrders >0) {
			ConcurrentSkipListSet<Order> firstNorders = new ConcurrentSkipListSet<>();
			for(int i =0 ;i<marketCache.keepOrders;i++)
				firstNorders.add(instrumentOrders.getAskSet().iterator().next());
			
			instrumentOrders.setAskSet(firstNorders);
		}
	}

	protected synchronized void setBidOrder(final InstrumentOrders instrumentOrders, List<Order> orders) {
		orders.forEach(o -> instrumentOrders.getBidSet().add(o));
		
		if(marketCache.keepOrders >0) {
			ConcurrentSkipListSet<Order> firstNorders = new ConcurrentSkipListSet<>();
			for(int i =0 ;i<marketCache.keepOrders;i++)
				firstNorders.add(instrumentOrders.getBidSet().descendingIterator().next());
			
			instrumentOrders.setBidSet(firstNorders);
		}
	}
}
