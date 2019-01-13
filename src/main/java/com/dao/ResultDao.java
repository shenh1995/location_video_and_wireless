package com.dao;

import java.util.List;

import com.pojo.Result;

public interface ResultDao {
	
	public void insertResult(String mac_name, String picture_paths, int status);
	
	public List<Result> getResults(List<String> mac_names);
	
}
