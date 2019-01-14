package com.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import com.dao.ConnectionRedis;

public class RedisHelp {
	
	Logger logger = Logger.getLogger(RedisHelp.class);
	
	public HashMap<String , List<String>> getPicturePathByMacNameList(List<String> mac_names) {
		Jedis tmp_redis = ConnectionRedis.getJedis();
		
		HashMap<String, List<String>> picturePathHashMap = new HashMap<String, List<String>>();
		
		List<String> inRedisList = new ArrayList<String>();
		List<String> notInRedisList = new ArrayList<String>();
		
		for (String mac_name : mac_names) {
			if(tmp_redis.get(mac_name) != null) {
				inRedisList.add(mac_name + "\t" + tmp_redis.get(mac_name));
			}else {
				notInRedisList.add(mac_name);
			}
		}
		
		picturePathHashMap.put("inRedis", inRedisList);
		picturePathHashMap.put("notInRedis", notInRedisList);
		
		tmp_redis.close();
		return picturePathHashMap;
	}

	public String getPicturePathsByMacName(String mac_name) {
		
		Jedis tmp_redis = ConnectionRedis.getJedis();
		
		String picture_paths = tmp_redis.get(mac_name);
		
		if(picture_paths != null) {
			logger.info("mac_name："+ mac_name +" 对应的picture_paths为：" + picture_paths);
		}
		else {
			logger.info("mac_name："+ mac_name + "不存在");
		}
			
		tmp_redis.close();
		return picture_paths;
	}
	
	public void delKey(String mac_name) {
		Jedis tmp_redis = ConnectionRedis.getJedis();
		
		if (tmp_redis.get(mac_name) != null) {
			tmp_redis.del(mac_name);
		}
		
		logger.info("删除key:" + mac_name + "成功");
		tmp_redis.close();
	}
	
	public void insertKeys(List<String> mac_names, List<String> picture_path_list) {
		Jedis tmp_redis = ConnectionRedis.getJedis();
		
	    String picture_paths = "";
	    
	    for(int i = 0; i < picture_path_list.size() - 1; i ++) {
	    	picture_paths += picture_path_list.get(i) + "\t";
	    }
	    
	    picture_paths += picture_path_list.get(picture_path_list.size() - 1);
	    
	    for (String mac_name : mac_names) {
			tmp_redis.append(mac_name, picture_paths);
			tmp_redis.expire(mac_name, 600);
		}
	    
	    tmp_redis.close();
	    
	    logger.info("批量插入mac_names成功");
	}
	
	public void insertKey(String mac_name, String picture_paths) {
		Jedis tmp_redis = ConnectionRedis.getJedis();
		
		if(tmp_redis.get(mac_name) != null) {
			tmp_redis.del(mac_name);
		}	
		
		tmp_redis.append(mac_name, picture_paths);
	    tmp_redis.expire(mac_name, 600);
		
		tmp_redis.close();
		
		logger.info("新增key：" + mac_name + "成功");
	}
}
