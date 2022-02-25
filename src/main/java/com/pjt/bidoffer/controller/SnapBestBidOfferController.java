package com.pjt.bidoffer.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.pjt.bidoffer.model.BestBidOffer;
import com.pjt.bidoffer.model.MarketCache;
import com.pjt.bidoffer.model.MarketStarter;

@RequestMapping("/bidask")
@RestController
public class SnapBestBidOfferController {

	@Autowired
	public MarketCache marketCache;

	Timer timer;

	@PostMapping("/start")
	public ResponseEntity<String> startCalculating(@RequestBody MarketStarter keepOrders) {
		if (timer != null) {
			return new ResponseEntity<String>("Market already started", HttpStatus.PRECONDITION_FAILED);
		}
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				marketCache.keepOrders = keepOrders.getKeepOrders();
				List<BestBidOffer> bestBidOffers = new ArrayList<>();
				marketCache.globalOrderCache.forEach((instrument, orders) -> bestBidOffers
						.add(new BestBidOffer(instrument, orders.getAskSet().first(), orders.getBidSet().last())));
				
				String json = LocalDateTime.now() + ":" + new Gson().toJson(bestBidOffers) + "\n";

				try (FileWriter fileWriter = new FileWriter(new File("bestBidOffer.json"), true);
						BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);) {
					bufferedWriter.write(json);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println(json);
			}
		}, 0, 1000);

		return new ResponseEntity<String>("Market started", HttpStatus.OK);
	}

	@PostMapping("/stop")
	public ResponseEntity<String> stopCalculating() {
		if (timer != null) {
			timer.cancel();
			File file = new File("bestBidOffer.json");
			LocalDateTime now = LocalDateTime.now();
			String new_name = now.format(DateTimeFormatter.BASIC_ISO_DATE) + now.getHour() +now.getMinute()+now.getSecond()  + "_bestBidOffer.json";
			System.out.println("New FIle name " + new_name);
			boolean fileRenamed = file.renameTo(
					new File(new_name));
			if (!fileRenamed) {
				marketCache.globalOrderCache.clear();
				return new ResponseEntity<String>("Market closed, But file needs to be cleaned up",
						HttpStatus.PRECONDITION_FAILED);
			}
		}

		timer = null;
		marketCache.globalOrderCache.clear();

		return new ResponseEntity<String>("Market closed", HttpStatus.OK);
	}

}
