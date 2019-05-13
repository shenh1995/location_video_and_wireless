package com.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author shenh
 * 消息队列参数
 */
public class MqConfig {

	public static int MQ_SIZE = 0;
	private static Properties mqConfig = new Properties();
	
	private static Logger logger = Logger.getLogger(MqConfig.class);
	
	static {
		try {
			InputStream mq_file = JdbcConfig.class.getClassLoader().getResourceAsStream("mq.properties");
			mqConfig.load(mq_file);
			
			MQ_SIZE = Integer.parseInt(mqConfig.getProperty("MQ_SIZE"));
			
		} catch (FileNotFoundException e) {
			logger.error("mq配置文件不存在");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("加载mq配置文件失败");
			e.printStackTrace();
		}
	}

}
