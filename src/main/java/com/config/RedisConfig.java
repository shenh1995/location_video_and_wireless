package com.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author shenh
 * redis配置参数
 */
public class RedisConfig {
	
	public static String redis_host = "";
	public static int port = 0;
	private static Properties redisConfig = new Properties();
	
	private static Logger logger = Logger.getLogger(RedisConfig.class);
	
	static {
		try {
			InputStream redis_Config_File = RedisConfig.class.getClassLoader().getResourceAsStream("redis.properties");
			redisConfig.load(redis_Config_File);
			
			redis_host = redisConfig.getProperty("redis_host");
			port = Integer.parseInt(redisConfig.getProperty("redis_port"));
			logger.info("加载redis配置文件成功");
		} catch (FileNotFoundException e) {
			logger.error("redis配置文件不存在");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("加载redis配置文件失败");
			e.printStackTrace();
		}
	}
}
