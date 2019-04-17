package com.dao.impl;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
				psts.setString(3, wifi.getDevice_id());
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

	/*
	 *    根据时间和设备id返回所有的mac
	 */
	public String getMacsByTimeAndDeviceid(long time, String device_id) {
		 
		StringBuilder macs = new StringBuilder();
		
		try {
			Connection connection = ConnectionDB.getConnection();
			
			String query_sql = "select mac_name from Wifi where time = " + time +", device_id =" + device_id;
			
			Statement statement = connection.createStatement();
			
			ResultSet resultset = statement.executeQuery(query_sql);
			
			while (resultset.next()) {
				macs.append(resultset.getString("mac_name") + "\t");
			}
			
			macs.deleteCharAt(macs.length() - 1);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		return null;
	}

}
