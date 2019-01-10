package com.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Jdbc_Config {
	
	public static String jdbc_driver = "";
	public static String jdbc_user = "";
	public static String jdbc_password = "";
	public static String jdbc_className = "";
	public static String jdbc_url = "";
	public static int max_pool_size = 0;
	public static int min_pool_size = 0;
	public static int initial_pool_size = 0;
	public static int max_statements = 0;
	public static int max_idle_time = 0;

	private static Properties jdbcConfig = new Properties();
	
	private static Logger logger = Logger.getLogger(Jdbc_Config.class);
	
	static {
		try {
			InputStream jdbc_Config_File = Jdbc_Config.class.getClassLoader().getResourceAsStream("jdbc.properties");
			jdbcConfig.load(jdbc_Config_File);
			
			jdbc_driver = jdbcConfig.getProperty("jdbc.driver");
			jdbc_user = jdbcConfig.getProperty("jdbc.user");
			jdbc_password = jdbcConfig.getProperty("jdbc.password");
			jdbc_className = jdbcConfig.getProperty("jdbc.className");
			jdbc_url = jdbcConfig.getProperty("jdbc.url");
			max_pool_size = Integer.parseInt(jdbcConfig.getProperty("MaxPoolSize"));
			min_pool_size = Integer.parseInt(jdbcConfig.getProperty("MinPoolSize"));
			initial_pool_size = Integer.parseInt(jdbcConfig.getProperty("InitialPoolSize"));
			max_statements = Integer.parseInt(jdbcConfig.getProperty("MaxStatements"));
			max_idle_time = Integer.parseInt(jdbcConfig.getProperty("MaxIdleTime"));
			
		} catch (FileNotFoundException e) {
			logger.error("jdbc配置文件不存在");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("加载配置文件失败");
			e.printStackTrace();
		}
	}
}
