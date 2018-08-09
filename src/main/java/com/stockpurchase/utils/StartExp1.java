package com.stockpurchase.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StartExp1 extends Thread  
{    
    public void run()  
    {    
    	 System.out.println("Stock market is closes by 03.30 PM");
    	 DateFormat dateFormat = new SimpleDateFormat("HH:mm");
         Date date = new Date();
         String dateString = dateFormat.format(date);
         System.out.print(dateString);
         Timestamp timestamp1 = new Timestamp(new Date().getTime());
         System.out.println(timestamp1);
         Timestamp timestamp2 = new Timestamp(new Date().getTime());
         System.out.println(timestamp2); 
    }    
    public static void main(String args[])  
    {    
        StartExp1 t1=new StartExp1();    
        // this will call run() method  
        t1.start();    
    }    
}   