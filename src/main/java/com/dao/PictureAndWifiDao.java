package com.dao;

import java.util.HashMap;

public interface PictureAndWifiDao {
	
	public HashMap<String, String> getVedioDeviceMap();
	
	public void insertRelationVideoDeviceidAndWifiId(int wifi_device_id, int video_device_id);
	
}
