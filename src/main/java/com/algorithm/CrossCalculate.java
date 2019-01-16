package com.algorithm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import com.dao.ResultDao;
import com.dao.impl.ResultDaoImpl;
import com.service.RedisHelp;

public class CrossCalculate {
	
	Logger logger = Logger.getLogger(CrossCalculate.class);
	
	public String getPicturePathsCorrespondMac(String old_pictures, String new_pictures) {
		
		String picture_paths_result = "";
		
		HashSet<String> old_pictures_set = new HashSet<String>();
		
		String [] old_picture_split = old_pictures.split("\t");
		
		for(int i = 0; i < old_picture_split.length; i ++) {
			old_pictures_set.add(old_picture_split[i]);
		}
		
		String [] new_picture_split = new_pictures.split("\t");
		
		for(int i = 0; i < new_picture_split.length; i ++) {
			if(old_pictures_set.contains(new_picture_split[i])) {
				picture_paths_result += new_picture_split[i] + "\t";
			}
		}
		return picture_paths_result.substring(0, picture_paths_result.length() - 1);
	}

	public void getCrossResult(List<String> mac_names, String new_pictures) throws SQLException {
		
		ResultDao resultDao = new ResultDaoImpl();
		RedisHelp redisHelp = new RedisHelp();
		
		HashMap<String , List<String>> picturePathHashMap = redisHelp.getPicturePathByMacNameList(mac_names);
		
		List<String> notInRedisMacNames = picturePathHashMap.get("notInRedis");
		
		resultDao.batchInsertStatusNotValidIntoResult(mac_names, new_pictures);
		
		List<String> inRedisMacNames = picturePathHashMap.get("inRedis");
		
		List<String> deleteMacNames = new ArrayList<String>();
		
		HashMap<String, String> mac_name_map_picture_paths = new HashMap<String, String>();
		
		for (String inRedisMacName : inRedisMacNames) {
			String [] inRedisMacNameSplit = inRedisMacName.split("\t");
			String mac_name = inRedisMacNameSplit[0];
			String old_pictures = inRedisMacName.substring(mac_name.length());
			String newest_pictures = getPicturePathsCorrespondMac(old_pictures, new_pictures);
			mac_name_map_picture_paths.put(mac_name, newest_pictures);
			deleteMacNames.add(mac_name);
		}

		resultDao.deleteResult(mac_names);
		resultDao.batchInsertIntoResult(mac_name_map_picture_paths);
		resultDao.batchInsertStatusNotValidIntoResult(notInRedisMacNames, new_pictures);
		redisHelp.insertKeys(notInRedisMacNames, new_pictures);
		redisHelp.insertKeys(mac_name_map_picture_paths);
	}
}
