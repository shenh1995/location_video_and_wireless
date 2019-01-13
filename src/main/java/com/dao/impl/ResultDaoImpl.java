package com.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.dao.ConnectionDB;
import com.dao.ResultDao;
import com.pojo.Result;

public class ResultDaoImpl implements ResultDao{
	
	Logger logger = Logger.getLogger(ResultDaoImpl.class);

	public void insertResult(String mac_name, String picture_paths, int status) {
		try {
			
			Connection connection = ConnectionDB.getConnection();
			
			String insert_result_sql = "insert into Result (mac_name, picture_paths, status) values(?,?,?)";
			
			PreparedStatement psts = connection.prepareStatement(insert_result_sql);
			
			psts.setString(1, mac_name);
			psts.setString(2, picture_paths);
			psts.setInt(3, status);
			
			psts.executeUpdate();
			
			logger.info("插入Result成功，status为：" + status);
			
		} catch (Exception e) {
			logger.error("插入Result失败");
		}
	}

	public List<Result> getResults(List<String> mac_names) {
		
		List<Result> results = new ArrayList<Result>();
		try {
			Connection connection = ConnectionDB.getConnection();
			
			String mac_names_string = "";
			for(int i = 0; i < mac_names.size() - 1; i ++) {
				mac_names_string += mac_names.get(i) + ",";
			}
			
			String query_result_sql = "select mac_name, picture_paths, status from Result where mac_name in (" +mac_names_string + "" + 
			mac_names.get(mac_names.size() - 1) + ")";
			
			PreparedStatement psts = connection.prepareStatement(query_result_sql);
			
			ResultSet rs = psts.executeQuery();

			while (rs.next()) {
				Result result = new Result();
				result.mac_name = rs.getString("mac_name");
				result.picture_paths = rs.getString("picture_paths");
				result.status = rs.getInt("status");
				results.add(result);
			}
			
			logger.info("查询Result成功");
			
		} catch (Exception e) {
			logger.info("查询Result失败");
		}
		return results;
	}
}
