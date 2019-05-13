package com.db;

import org.apache.log4j.Logger;

import com.config.RedisConfig;

import redis.clients.jedis.Jedis;

public class ConnectionRedis {
	
	private static Logger logger = Logger.getLogger(ConnectionRedis.class);

	/**
	 * @return
	 *   获取redis连接
	 */
	public static Jedis getJedis() {
		Jedis jedis = new Jedis(RedisConfig.redis_host, RedisConfig.port);
		logger.info("获取redis连接成功");
		return jedis;
	}
	
	/**
	 * @param jedis
	 * 释放redis连接
	 */
	public static void releaseJedis(Jedis jedis) {
		jedis.close();
	    logger.info("释放redis连接成功");
	}
}
