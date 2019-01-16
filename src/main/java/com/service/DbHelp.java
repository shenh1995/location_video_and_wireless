package com.service;

import java.util.List;

import com.dao.HumanPictureDao;
import com.dao.PictureAndWifiDao;
import com.dao.WifiDao;
import com.dao.impl.HumanPictureDaoImpl;
import com.dao.impl.PictureAndWifiDaoImpl;
import com.dao.impl.WifiDaoImpl;
import com.pojo.HumanPicture;
import com.pojo.Wifi;

public class DbHelp {

	public void storageInfoToDB(List<Wifi> wifis, List<HumanPicture> humanPictures, int wifi_device_id, int video_device_id) {
		WifiDao wifiDao = new WifiDaoImpl();
		HumanPictureDao humanPictureDao = new HumanPictureDaoImpl();
		PictureAndWifiDao pictureAndWifiDao = new PictureAndWifiDaoImpl();
		
		wifiDao.insertWifi(wifis);
		humanPictureDao.insertHumanPicture(humanPictures);
		pictureAndWifiDao.insertRelationVideoDeviceidAndWifiId(wifi_device_id, video_device_id);
	}

}
