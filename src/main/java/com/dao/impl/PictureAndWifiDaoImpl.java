package com.dao.impl;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.dao.ConnectionDB;
import com.dao.PictureAndWifiDao;

public class PictureAndWifiDaoImpl implements PictureAndWifiDao{
	
	Logger logger = Logger.getLogger(PictureAndWifiDaoImpl.class);
	
	
	/**
	 * 	获得视频设备id和上传无线设备id的映射关系
	 * @return
	 */
	public HashMap<String, String> getVedioDeviceMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		 
		Connection connection = null;
	    Statement statement = null;
		
		try {
			try {
				connection = ConnectionDB.getConnection();
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
			statement = connection.createStatement();
			String query_String = "select * from MapVideoDeviceAndWifiDevice";
			
			ResultSet rs = statement.executeQuery(query_String);
			
			while (rs.next()) {
				String wifi_device_id = rs.getString("wifi_device_id");
				String video_device_id = rs.getString("video_device_id");
				map.put(wifi_device_id, video_device_id);
			}
			logger.info("map大小：" + map.size());
		} catch (SQLException e) {
			logger.error("获得视频设备id和上传无线设备id的映射关系失败");
			e.printStackTrace();
		}
		return map;
	}

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
