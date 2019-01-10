package com.dao.impl;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.dao.ConnectionDB;
import com.dao.PictureAndWifiDao;

public class PictureAndWifiDaoImpl implements PictureAndWifiDao{
	
	Logger logger = Logger.getLogger(PictureAndWifiDaoImpl.class);

	public void insertRelationVideoDeviceidAndWifiId(int wifi_device_id, int video_device_id) {
		try {
			Connection connection = ConnectionDB.getConnection();
			
			String insert_wifi_sql = "insert into MapVideoDeviceAndWifiDevice (wifi_device_id, video_device_id) values(?,?)";
			
			PreparedStatement psts = connection.prepareStatement(insert_wifi_sql); 
			
			psts.setInt(1, wifi_device_id);
			psts.setInt(2, video_device_id);

			psts.executeUpdate();
			
			logger.info("MapVideoDeviceAndWifiDevice 数据库插入成功");
			psts.close();
			connection.close();
			
		} catch (SQLException e) {
			logger.error("MapVideoDeviceAndWifiDevice 数据库插入失败");
			e.printStackTrace();
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}
}
