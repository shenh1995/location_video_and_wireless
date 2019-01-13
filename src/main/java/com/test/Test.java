package com.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.config.RedisConfig;

import redis.clients.jedis.Jedis;

public class Test {

	public static void main(String [] args) {
		Logger logger = Logger.getLogger(Test.class);
		
		Jedis jedis = new Jedis(RedisConfig.redis_host, RedisConfig.port);
		
		List<String> paths = new ArrayList<String>();
		paths.add("python");
		paths.add("java");
		
		jedis.set("java", paths.toString());
		
		System.out.println(jedis.get("java"));
		
		jedis.close();
		
		logger.debug("测试成功");
	}
}
