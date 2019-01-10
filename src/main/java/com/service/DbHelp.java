package com.service;

import java.util.List;

import com.dao.HumanPictureDao;
import com.dao.WifiDao;
import com.dao.impl.HumanPictureDaoImpl;
import com.dao.impl.WifiDaoImpl;
import com.pojo.HumanPicture;
import com.pojo.Wifi;

public class DbHelp {

	public void storageInfoToDB(List<Wifi> wifis, List<HumanPicture> humanPictures) {
		WifiDao wifiDao = new WifiDaoImpl();
		HumanPictureDao humanPictureDao = new HumanPictureDaoImpl();
		
		wifiDao.insertWifi(wifis);
		humanPictureDao.insertHumanPicture(humanPictures);
	}

}
