package com.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.dao.ConnectionDB;
import com.dao.ResultDao;
import com.pojo.Result;

public class ResultDaoImpl implements ResultDao{
	
	Logger logger = Logger.getLogger(ResultDaoImpl.class);
	
	
	public void batchInsertIntoResult(HashMap<String, String> mac_name_map_picture_paths) throws SQLException {
		Connection connection = null;
		PreparedStatement psts = null;
		try {
			connection = ConnectionDB.getConnection();
			
			String insert_result_sql = "insert into Result(mac_name, picture_paths, status) value(?,?,?)";
			
			psts = connection.prepareStatement(insert_result_sql);
			
			Iterator<?> iterator = mac_name_map_picture_paths.entrySet().iterator();
			
			while (iterator.hasNext()) {
				Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
				String mac_name = (String) entry.getKey();
				String picture_paths = (String) entry.getValue();
				String [] picture_path_split = picture_paths.split("\t");
				if(picture_path_split.length == 1) {
					psts.setString(1, mac_name);
					psts.setString(2, picture_path_split[0]);
					psts.setInt(3, 1);
				}else {
					psts.setString(1, mac_name);
					psts.setString(2, picture_paths);
					psts.setInt(3, 0);
				}
				psts.addBatch();
			}
			psts.executeBatch();
			
			logger.info("批量插入Result成功:" + mac_name_map_picture_paths.keySet());
			
		} catch (Exception e) {
			logger.error("批量插入Result失败");
		}finally {
			connection.close();
			psts.close();
		}
	}
	
	public void batchInsertStatusNotValidIntoResult(List<String> mac_names, String picture_paths) throws SQLException{
		Connection connection = null;
		PreparedStatement psts = null;
		try {
			connection = ConnectionDB.getConnection();
			
			String insert_result_sql = "insert into Result(mac_name, picture_paths, status) values(?,?,?)";
			
		    psts = connection.prepareStatement(insert_result_sql);
			
		    for (String mac_name : mac_names) {
		    	psts.setString(1, mac_name);
		    	psts.setString(2, picture_paths);
		    	psts.setInt(3, 0);
		    	psts.addBatch();
			}
		    psts.executeBatch();

		    logger.info("插入未生效的Result成功:" + mac_names.toString());
			
		} catch (Exception e) {
			logger.error("插入未生效的Result失败");
		}finally {
		    psts.close();
		    connection.close();
		}
	}

	public void deleteResult(List<String> mac_names) throws SQLException{
		Connection connection = null;
		PreparedStatement psts = null;
		try {
			connection = ConnectionDB.getConnection();
			
			String delete_result_sql = "delete from Result where mac_name = ?";
			
			psts = connection.prepareStatement(delete_result_sql);
		
			for (String mac_name : mac_names) {
				psts.setString(1, mac_name);
				psts.addBatch();
			}
			psts.executeBatch();
			
			logger.info("批量删除result成功:" + mac_names.toString());
			
		} catch (Exception e) {
			logger.error("批量删除result失败");
		}finally {
			connection.close();
			psts.close();
		}
	}
	
	public void insertResult(String mac_name, String human_picture_path) throws SQLException{
		Connection connection = null;
		PreparedStatement psts = null;
		try {
			connection = ConnectionDB.getConnection();
			
			String insert_result_sql = "insert into Result (mac_name, human_picture_path) values(?,?)";
			psts = connection.prepareStatement(insert_result_sql);
			psts.setString(1, mac_name);
			psts.setString(2, human_picture_path);
			
			psts.executeUpdate();
			
			logger.info("插入Result成功，mac_name为：" + mac_name);
			
		} catch (Exception e) {
			logger.error("插入Result失败");
		}finally {
			connection.close();
			psts.close();
		}
	}

	public List<Result> getResults(List<String> mac_names) throws SQLException{
		Connection connection = null;
		PreparedStatement psts = null;
		
		List<Result> results = new ArrayList<Result>();
		try {
			connection = ConnectionDB.getConnection();
			
			String mac_names_string = "";
			for(int i = 0; i < mac_names.size() - 1; i ++) {
				mac_names_string += mac_names.get(i) + ",";
			}
			
			String query_result_sql = "select mac_name, picture_paths, status from Result where mac_name in (" +mac_names_string + "" + 
			mac_names.get(mac_names.size() - 1) + ")";
			
			psts = connection.prepareStatement(query_result_sql);
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
		}finally {
			connection.close();
			psts.close();
		}
		return results;
	}
}
