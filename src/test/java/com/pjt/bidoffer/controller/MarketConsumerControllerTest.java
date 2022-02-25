package com.pjt.bidoffer.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pjt.bidoffer.BidofferApplication;
import com.pjt.bidoffer.model.InstrumentOrders;
import com.pjt.bidoffer.model.MarketOrder;
import com.pjt.bidoffer.model.Order;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BidofferApplication.class)
public class MarketConsumerControllerTest {

	@Autowired
	private MarketConsumerController marketConsumerController;
	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testConsumeMarketConditionSingle() {
		InstrumentOrders instrumentOrders = new InstrumentOrders();
		instrumentOrders.setAskSet(new ConcurrentSkipListSet<>());
		instrumentOrders.setBidSet(new ConcurrentSkipListSet<>());
		List<Order> askOrders = new ArrayList<>();
		askOrders.add(new Order(100d, 10l));
		askOrders.add(new Order(101d, 1l));
		askOrders.add(new Order(100.5d, 100l));

		List<Order> bidOrders = new ArrayList<>();
		bidOrders.add(new Order(99d, 10l));
		bidOrders.add(new Order(99.99d, 1l));
		bidOrders.add(new Order(98.60d, 100l));

		MarketOrder marketOrders = new MarketOrder("ABC", "ISIN", askOrders, bidOrders);
		marketConsumerController.consumeMarketCondition(marketOrders);

		assertEquals(100d, marketConsumerController.marketCache.globalOrderCache.get(marketOrders.getIntrument()).getAskSet().first().getPrice());
		assertEquals(10l, marketConsumerController.marketCache.globalOrderCache.get(marketOrders.getIntrument()).getAskSet().first().getSize());
		assertEquals(99.99d, marketConsumerController.marketCache.globalOrderCache.get(marketOrders.getIntrument()).getBidSet().last().getPrice());
		assertEquals(1l, marketConsumerController.marketCache.globalOrderCache.get(marketOrders.getIntrument()).getBidSet().last().getSize());

	}

	
	@Test
	void testConsumeMarketCondition() throws InterruptedException, ExecutionException, TimeoutException {
		InstrumentOrders instrumentOrders = new InstrumentOrders();
		instrumentOrders.setAskSet(new ConcurrentSkipListSet<>());
		instrumentOrders.setBidSet(new ConcurrentSkipListSet<>());
		List<Order> askOrders = new ArrayList<>();
		askOrders.add(new Order(100d, 10l));
		askOrders.add(new Order(101d, 1l));
		askOrders.add(new Order(100.5d, 100l));

		List<Order> bidOrders = new ArrayList<>();
		bidOrders.add(new Order(99d, 10l));
		bidOrders.add(new Order(99.99d, 1l));
		bidOrders.add(new Order(98.60d, 100l));

		marketConsumerController.marketCache.keepOrders = 3;
		MarketOrder marketOrders = new MarketOrder("ABC", "ISIN", askOrders, bidOrders);
		//marketConsumerController.consumeMarketCondition(marketOrders);

		List<Order> askOrders2 = new ArrayList<>();
		askOrders2.add(new Order(99.99d, 15l));
		List<Order> bidOrders2 = new ArrayList<>();
		bidOrders2.add(new Order(99.97d, 100l));
		MarketOrder marketOrders2 = new MarketOrder("ABC", "ISIN", askOrders2, bidOrders2);

		List<Order> askOrders3 = new ArrayList<>();
		askOrders3.add(new Order(99.01d, 16l));
		List<Order> bidOrders3 = new ArrayList<>();
		bidOrders3.add(new Order(99.25d, 100l));
		MarketOrder marketOrders3 = new MarketOrder("ABC", "ISIN", askOrders3, bidOrders3);

		List<Order> askOrders4 = new ArrayList<>();
		askOrders4.add(new Order(110.01d, 6l));
		List<Order> bidOrders4 = new ArrayList<>();
		bidOrders4.add(new Order(105.25d, 10l));
		MarketOrder marketOrders4 = new MarketOrder("ABC", "ISIN", askOrders4, bidOrders4);

		ExecutorService executorService = Executors.newFixedThreadPool(4);
		
		List<Future<Boolean>> listResults = new ArrayList<Future<Boolean>>();
		listResults.add(executorService.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				marketConsumerController.consumeMarketCondition(marketOrders);
				return true;
			}
		}));
		
		listResults.add(executorService.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				marketConsumerController.consumeMarketCondition(marketOrders2);
				return true;
			}
		}));

		listResults.add(executorService.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				marketConsumerController.consumeMarketCondition(marketOrders3);
				return true;
			}
		}));
		
		listResults.add(executorService.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				marketConsumerController.consumeMarketCondition(marketOrders4);
				return true;
			}
		}));
		

		executorService.shutdownNow();


		for(Future<Boolean> future: listResults) {
			future.get();
		}
		
		assertEquals(99.01d, marketConsumerController.marketCache.globalOrderCache.get(marketOrders.getIntrument()).getAskSet().first().getPrice());
		assertEquals(16l, marketConsumerController.marketCache.globalOrderCache.get(marketOrders.getIntrument()).getAskSet().first().getSize());
		assertEquals(105.25d, marketConsumerController.marketCache.globalOrderCache.get(marketOrders.getIntrument()).getBidSet().last().getPrice());
		assertEquals(10l, marketConsumerController.marketCache.globalOrderCache.get(marketOrders.getIntrument()).getBidSet().last().getSize());

	}

	@Test
	void testSetAskOrder() {
		InstrumentOrders instrumentOrders = new InstrumentOrders();
		instrumentOrders.setAskSet(new ConcurrentSkipListSet<>());
		instrumentOrders.setBidSet(new ConcurrentSkipListSet<>());
		List<Order> orders = new ArrayList<>();
		orders.add(new Order(100d, 10l));
		orders.add(new Order(101d, 1l));
		orders.add(new Order(100.5d, 100l));
		
		marketConsumerController.setAskOrder(instrumentOrders, orders);
		assertEquals(100d, instrumentOrders.getAskSet().first().getPrice());

	}

	@Test
	void testSetBidOrder() {
		InstrumentOrders instrumentOrders = new InstrumentOrders();
		instrumentOrders.setAskSet(new ConcurrentSkipListSet<>());
		instrumentOrders.setBidSet(new ConcurrentSkipListSet<>());
		List<Order> orders = new ArrayList<>();
		orders.add(new Order(100d, 10l));
		orders.add(new Order(101d, 1l));
		orders.add(new Order(100.5d, 100l));

		marketConsumerController.setBidOrder(instrumentOrders, orders);
		assertEquals(101d, instrumentOrders.getBidSet().last().getPrice());

	}

}
