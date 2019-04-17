package com.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.pojo.Result;

public interface ResultDao {
	
	public void insertResult(String mac_name, String human_picture_path) throws SQLException;
	
	public List<Result> getResults(List<String> mac_names) throws SQLException;
	
	public void batchInsertStatusNotValidIntoResult(List<String> mac_names, String picture_paths) throws SQLException;
	
	public void deleteResult(List<String> mac_names) throws SQLException;
	
	public void batchInsertIntoResult(HashMap<String, String> mac_name_map_picture_paths) throws SQLException;
}
