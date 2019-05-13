package com.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author shenh
 * 人脸对比 参数
 */
public class FaceApiConfig {

	public static String APP_ID = "";
	public static String API_KEY = "";
	public static String SECRET_KEY = "";
	private static Properties faceApiConfig = new Properties();
	
	private static Logger logger = Logger.getLogger(FaceApiConfig.class);
	
	static {
		try {
			InputStream face_api_file = JdbcConfig.class.getClassLoader().getResourceAsStream("faceApi.properties");
			faceApiConfig.load(face_api_file);
			
			APP_ID = faceApiConfig.getProperty("APP_ID");
			API_KEY = faceApiConfig.getProperty("API_KEY");
			SECRET_KEY = faceApiConfig.getProperty("SECRET_KEY");
			
		} catch (FileNotFoundException e) {
			logger.error("face api配置文件不存在");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("加载face api配置文件失败");
			e.printStackTrace();
		}
	}
}
