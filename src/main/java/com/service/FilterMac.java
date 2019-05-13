package com.service;

import java.util.HashSet;

public class FilterMac {

   public static HashSet<String> fixedMac = new HashSet<String>();
   
   public static void loadFixedMac() {
	   fixedMac.add("9C:B6:D0:F9:60:5B");
	   fixedMac.add("94:E0:D6:14:22:2A");   //存在
	   fixedMac.add("48:5F:99:1E:A9:8F");   //存在
	   fixedMac.add("06:DB:67:E6:9A:F9");
	   fixedMac.add("14:BD:61:AA:9A:B0");
	   fixedMac.add("D4:BB:C8:21:D0:D9");   //存在
	   fixedMac.add("A6:E4:10:B4:97:09");
	   fixedMac.add("FC:1A:11:72:A4:9C");   //存在
	   fixedMac.add("06:CB:FF:EB:5F:E7");   
	   fixedMac.add("70:1C:E7:DC:FC:1C");   //存在
	   fixedMac.add("D8:9C:67:B3:F3:A1");   
	   fixedMac.add("0C:96:E6:9A:D1:B9");
	   fixedMac.add("FC:BE:7B:24:35:45");   //存在
	   fixedMac.add("DA:A1:19:7C:78:EE");   //存在
	   fixedMac.add("E6:9D:97:A6:2F:51");   //存在
	   fixedMac.add("2A:5F:99:1E:A9:8F");   //存在
	   fixedMac.add("2E:96:E6:9A:D1:B9");   
		/*
		 * fixedMac.add("94:E0:D6:14:22:2A"); 
		 * fixedMac.add("E0:13:B5:DF:36:AB"); 
		 * fixedMac.add("A6:E4:10:B4:97:09");
		 * fixedMac.add("FC:BE:7B:24:35:45");
		 * fixedMac.add("70:1C:E7:DC:FC:1C");
		 */
   }
 	
}

