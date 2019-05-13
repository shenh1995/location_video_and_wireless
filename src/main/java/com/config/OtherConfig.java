package com.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author shenh
 * 其他配置参数
 */
public class OtherConfig {

	public static int WirelessPort = 0;
	public static int VideoPort = 0;
	private static Properties otherConfig = new Properties();
	
	private static Logger logger = Logger.getLogger(OtherConfig.class);
	
	static {
		try {
			InputStream other_Config_File = JdbcConfig.class.getClassLoader().getResourceAsStream("other.properties");
			otherConfig.load(other_Config_File);
			
			WirelessPort = Integer.parseInt(otherConfig.getProperty("WirelessPort"));
			VideoPort = Integer.parseInt(otherConfig.getProperty("VideoPort"));
			
		} catch (FileNotFoundException e) {
			logger.error("other配置文件不存在");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("加载other配置文件失败");
			e.printStackTrace();
		}
	}
}
