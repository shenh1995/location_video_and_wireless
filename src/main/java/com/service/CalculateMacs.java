package com.service;

import java.util.HashSet;

public class CalculateMacs {
	
	/**
	 *  计算出两个macList的mac交集
	 * @param macs1
	 * @param macs2
	 * @return
	 */
	public String getTwoMacsIntersection(String macs1, String macs2) {
		if(macs1 == null || macs2 == null || macs1.length() <=5 || macs2.length() <= 5)
			return "";
		
		String [] mac1Split = macs1.split("\t");
		String [] mac2Split = macs2.split("\t");
		
		HashSet<String> macHashSet = new HashSet<String>();
		for(int i = 0; i < mac1Split.length; i ++) {
			if("".equals(mac1Split[i]))
				continue;
			if(FilterMac.fixedMac.contains(mac1Split[i]))
				continue;
			macHashSet.add(mac1Split[i]);
		}
		
		String result = "";
		for(int i = 0; i < mac2Split.length; i ++) {
			if(macHashSet.contains(mac2Split[i]))
				result += mac2Split[i] + "\t";
		}
		
		if("".equals(result))
			return "";
        return result.substring(0, result.length() - 1);
	}
	
	/**
	 * 计算两个macList的并集
	 * @param macs1
	 * @param mac2
	 * @return
	 */
	public String getTwoMacsUnion(String macs1, String macs2) {
		String [] mac1Split = macs1.split("\t");
		String [] mac2Split = macs2.split("\t");
		
		HashSet<String> macHashSet = new HashSet<String>();
		String result = "";
		for(int i = 0; i < mac1Split.length; i ++) {
			if("".equals(mac1Split[i]))
				continue;
			if(FilterMac.fixedMac.contains(mac1Split[i]))
			       continue;
			macHashSet.add(mac1Split[i]);
			result += mac1Split[i] + "\t";
		}
		
		for(int i = 0; i < mac2Split.length; i ++) {
			if(macHashSet.contains(mac2Split[i]))
				continue;
			if(FilterMac.fixedMac.contains(mac2Split[i]))
			       continue;
			result += mac2Split[i] + "\t";
		}
		
		if("".equals(result))
			return "";
        return result.substring(0, result.length() - 1);
	}
	
}
