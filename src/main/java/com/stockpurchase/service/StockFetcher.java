package com.stockpurchase.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stockpurchase.model.RequiredStock;
import com.stockpurchase.model.StockData;

public class StockFetcher {  
	
	
	/**
	* Returns a Stock Object that contains info about a specified stock.
	* @param 	symbol the company's stock symbol
	* @return 	a stock object containing info about the company's stock
	* @see Stock
	*/
	public static List<String> getStockData(String symbol) {  
		List<String> listOfStockRecords=null;
		try { 
			//https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="+symbol+"&apikey=demo&datatype=csv
			//https://www.alphavantage.co/query?function=BATCH_QUOTES_US&symbols=MSFT,FB,AAPL&apikey=demo&datatype=csv
			String url = "https://www.alphavantage.co/query?function=BATCH_QUOTES_US&symbols=MSFT,FB,AAPL&apikey=demo&datatype=csv";
			// Retrieve stock CSV File
			URL stockDataUrl = new URL(url);
			URLConnection connection = stockDataUrl.openConnection();
			InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
			if(inputStreamReader != null) {
				listOfStockRecords=loadStockData(inputStreamReader);    
			}	
		} catch (IOException e) {
			Logger log = Logger.getLogger(StockFetcher.class.getName()); 
			log.log(Level.SEVERE, e.toString(), e);
			return null;
		}
		return listOfStockRecords;	
	}

	/**
	 * Load the stock data from input stream to list object 
	 * @param is
	 * @return list of Stock data
	 * @throws IOException
	 */
	private static List<String> loadStockData(InputStreamReader is) throws IOException {	
		List<String> listOfStockRecords = new ArrayList<>();
		BufferedReader br = new BufferedReader(is);  
		String line = br.readLine();
		  while(line!=null){  
			  listOfStockRecords.add(line);
			  line=br.readLine();
		  }  
		  br.close();
		  return listOfStockRecords;
	}
	
	/**
	 * Prepare pojo for current stock data
	 * @param listOfStockRecords
	 * @return
	 */
	public static List<StockData> currentStockData(List<String> listOfStockRecords) {
		List<StockData> stockDataList = new ArrayList<>();
		
		listOfStockRecords.forEach((stock) -> {
			
			StockData stockData = new StockData();
			
			String[] parts = stock.split(",");
			stockData.setSymbol(parts[0]);
			stockData.setOpen(parts[1]);
			stockData.setHigh(parts[2]);
			stockData.setLow(parts[3]);
			stockData.setPrice(parts[4]);
			stockData.setVolume(parts[5]);
			stockData.setTimestamp(parts[6]);
			stockData.setCurrency(parts[7]);
			stockDataList.add(stockData);
		});
		return stockDataList;
	}
	/**
	 * Prepare the required object for stock purchase
	 * @return
	 */
	 public static RequiredStock userInput(){
	    	RequiredStock requiredStock = new RequiredStock();
	    	
	    	Scanner scannerStock = new Scanner(System.in);
	    	System.out.println("Enter stock name:");
			String stockName = scannerStock.nextLine();
			requiredStock.setStockName(stockName);

			Scanner scannerBalance = new Scanner(System.in);
			System.out.println("Enter initial balance:");
			double initialBalance = scannerBalance.nextDouble();
			requiredStock.setInitialBalance(initialBalance);
			
			Scanner scannerDate = new Scanner(System.in);
			System.out.println("Enter Date :");
			String dateString= scannerDate.nextLine();
			Date date;
			try {
				date = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
				//System.out.println(dateString+"\t"+date);  
				requiredStock.setDate(date);	
			} catch (ParseException e) {
				e.printStackTrace();
			}  
		    
			//System.out.println(scannerStock+","+scannerBalance+","+scannerDate);
			return requiredStock;
	    }
	 
	 /**
	  * buy the stock available in market
	  * @param listOfStockData
	  * @param status
	  * @return
	  */
	 public static String buyStockBusinessLogic(List<StockData> listOfStockData, String status) {
		 
		 System.out.println("The stock market is open from 8:30am - 3.30pm");
		 System.out.println("Do you want to buy Stock Y/N:");
			Scanner scanner = new Scanner(System.in);
			String yesOrNo = scanner.nextLine();
			
			if(yesOrNo.equalsIgnoreCase("Y")){
				RequiredStock requiredStock = StockFetcher.userInput();
				if(!listOfStockData.isEmpty()) {
					for(StockData stockdata:listOfStockData) {
						//System.out.println(stockdata.getSymbol()+","+requiredStock.getStockName());
						if(stockdata.getSymbol().equalsIgnoreCase(requiredStock.getStockName())) {
							double stockPrice = Double.parseDouble(stockdata.getPrice());
							if(stockPrice <= requiredStock.getInitialBalance() ) {
								double balance = requiredStock.getInitialBalance()-stockPrice;
								
								System.out.println("Your account balance is updated:"+balance);
								status ="Successfully purchased stock";
							}else {
								status = "You dont have suffucient balance in your account to buy this stock";
							}
						}
					}
				}
			}
			System.out.println(status);
			return status;
		}
		
	 
	 public static boolean stockMarketTimer(){
		 	boolean buyStock = false;
			SimpleDateFormat sdfDate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
			
//Stock market Start date			
			String stockMarketStart = "08/09/2018 8:30:00 AM";
	        Date stockStartDate=null;
	        try {
	        	stockStartDate = sdfDate.parse(stockMarketStart);
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	        
//Stock market End date	        
	        String stockMarketEnd = "08/09/2018 03:50:00 PM";
	        Date stockEndDate =null;
	        try {
	        	stockEndDate = sdfDate.parse(stockMarketEnd);
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	        
	        String user_time = getCurrentTimeStamp();
	        Date now = null;
	        try {
	            now = sdfDate.parse(user_time);
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	    /*          
	        System.out.println(now);
	        System.out.println(stockStartDate);*/

	        if (now.after(stockStartDate) && now.before(stockEndDate)){
	        	System.out.println("Stock market is running. You can buy the stocks");
	        	buyStock = true;
	        }else {
	        	System.out.println("Stock market is closed - Stock market hours : 08:30AM - 03.30PM ");
	        	
	        }
	        return buyStock;
		}
	 
		public static String getCurrentTimeStamp() {
	        SimpleDateFormat sdfDate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");//dd/MM/yyyy
	        Date now = new Date();
	        String strDate = sdfDate.format(now);
	        return strDate;
	    }
}