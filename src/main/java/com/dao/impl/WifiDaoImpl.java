package com.dao.impl;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.dao.ConnectionDB;
import com.dao.WifiDao;
import com.pojo.Wifi;

public class WifiDaoImpl implements WifiDao{

	Logger logger = Logger.getLogger(WifiDaoImpl.class);
	
	public void insertWifi(List<Wifi> wifis) {
		try {
			Connection connection = ConnectionDB.getConnection();
			
			String insert_wifi_sql = "insert into Wifi (mac_name, time, device_id) values(?,?,?)";
			
			PreparedStatement psts = connection.prepareStatement(insert_wifi_sql); 
			
			for (Wifi wifi : wifis) {
				psts.setString(1, wifi.getMac_name());
				psts.setString(2, wifi.getTime());
				psts.setInt(3, wifi.getDevice_id());
				psts.addBatch();
			}
			psts.executeBatch();
			
			logger.info("Wifi 数据库插入成功");
			psts.close();
			connection.close();
			
		} catch (SQLException e) {
			logger.error("Wifi 数据库插入失败");
			e.printStackTrace();
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		
	}

}
