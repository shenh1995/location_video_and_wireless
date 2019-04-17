package com.dao;

import java.util.List;

import com.pojo.Wifi;

public interface WifiDao {

	public void insertWifi(List<Wifi> wifis);
	
	public String getMacsByTimeAndDeviceid(long time, String device_id);
}
