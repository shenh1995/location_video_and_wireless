package com.algorithm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.dao.ResultDao;
import com.dao.impl.ResultDaoImpl;
import com.service.RedisHelp;

public class CrossCalculate {
	
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

	public String getCrossResult(List<String> mac_names, String new_pictures) {
		
		ResultDao resultDao = new ResultDaoImpl();
		
		HashMap<String , List<String>> picturePathHashMap = new RedisHelp().getPicturePathByMacNameList(mac_names);
		
		List<String> notInRedisMacNames = picturePathHashMap.get("notInRedis");
		
		resultDao.batchInsertStatusNotValidIntoResult(mac_names, new_pictures);
		
		List<String> inRedisMacNames = picturePathHashMap.get("inRedis");
		
		for (String inRedisMacName : inRedisMacNames) {
			String [] inRedisMacNameSplit = inRedisMacName.split("\t");
			String mac_name = inRedisMacNameSplit[0];
			String old_pictures = inRedisMacName.substring(mac_name.length());
			String newest_pictures = getPicturePathsCorrespondMac(old_pictures, new_pictures);
		}
	}
}
