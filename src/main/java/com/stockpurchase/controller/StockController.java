package com.stockpurchase.controller;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stockpurchase.model.RequiredStock;
import com.stockpurchase.model.StockData;
import com.stockpurchase.service.StockFetcher;
import com.stockpurchase.utils.StartExp1;


@RestController
@EnableScheduling
public class StockController {

    //private static Logger log = LoggerFactory.getLogger(StockController.class);
	
    private List<StockData> listOfStockData=null;
    
    @GetMapping("/home")
    public String home(){
    	return "<html>"
    			+ "<body>"
    			+ "<a href='http://localhost:8090/getstocks'>Fetch Stock Data</a><br><br>"
    			+"<a href='http://localhost:8090/buystocks'>Buy Stock Data</a>"
    			+ "</body>"
    			+ "</html>";
    }
    
    @GetMapping("/getstocks")
    @Scheduled(cron = "30 * * * * *")
    public List<String> getStockData() {
    	List<String> listOfStockRecords = StockFetcher.getStockData("MSFT");
    	if(!listOfStockRecords.isEmpty()) {
			listOfStockData = StockFetcher.currentStockData(listOfStockRecords);
    	}
    	return listOfStockRecords;
    }
 
    @GetMapping("/buystocks")
	private String buyStock() {
    	
    	String status="Required stock not available please choose a valid one";
    	boolean buyStock = StockFetcher.stockMarketTimer();
    	if(buyStock) {    		
    		status = StockFetcher.buyStockBusinessLogic(listOfStockData,status);
    	}else {
    		status = "Stock market is closed -> Stock market hours : 08:30AM - 03.30PM ";
    	}
		return status;
	}
    
    @GetMapping("/sellstocks")
    private String sellStock() {
    	String sellStatus="";
    	
    	return sellStatus;
    }
    
/*
    @Scheduled( cron = "0/1 * * * * *")
	public void stockMarketTimer(){
    	 StartExp1 exp1 = new StartExp1();
    	 exp1.start();
	}*/
   
}