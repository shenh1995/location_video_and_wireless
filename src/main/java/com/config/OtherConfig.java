package com.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class OtherConfig {

	public static int port = 0;
	private static Properties otherConfig = new Properties();
	
	private static Logger logger = Logger.getLogger(OtherConfig.class);
	
	
	static {
		try {
			InputStream other_Config_File = JdbcConfig.class.getClassLoader().getResourceAsStream("other.properties");
			otherConfig.load(other_Config_File);
			
			
			port = Integer.parseInt(otherConfig.getProperty("port"));
			
		} catch (FileNotFoundException e) {
			logger.error("other配置文件不存在");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("加载other配置文件失败");
			e.printStackTrace();
		}
	}
}
