package com.service;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import com.dao.ConnectionRedis;

public class RedisHelp {
	
	Logger logger = Logger.getLogger(RedisHelp.class);

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
