package com.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

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
	
	public void delKeys(List<String> mac_names) {
		Jedis tmp_redis = ConnectionRedis.getJedis();
		
		for (String mac_name : mac_names) {
			if (tmp_redis.get(mac_name) != null) {
				tmp_redis.del(mac_name);
			}
		}
		
		logger.info("删除keys:" + mac_names.toString() + "成功");
		tmp_redis.close();
	}
	
	public void insertKeys(HashMap<String, String> mac_name_map_picture_paths) {
		Jedis tmp_redis = ConnectionRedis.getJedis();

		Iterator<?> iterator = mac_name_map_picture_paths.entrySet().iterator();
		
		while (iterator.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
			String mac_name = (String) entry.getKey();
			String picture_paths = (String) entry.getValue();
			tmp_redis.set(mac_name, picture_paths);
			tmp_redis.expire(mac_name, 600);
		}
	    tmp_redis.close();
	    
	    logger.info("批量插入mac_names成功");
	}
	
	public void insertKeys(List<String> mac_names, String picture_paths) {
		Jedis tmp_redis = ConnectionRedis.getJedis();
		
		for (String mac_name : mac_names) {
			tmp_redis.set(mac_name, picture_paths);
		    tmp_redis.expire(mac_name, 600);
		}

		logger.info("新增keys：" + mac_names.toString() + "成功");
		tmp_redis.close();
	}
}
