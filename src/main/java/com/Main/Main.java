package com.Main;

import org.apache.log4j.Logger;

import com.config.OtherConfig;
import com.messageQueue.Consumer;
import com.messageQueue.PictureConsumer;
import com.messageQueue.Storage;
import com.schedule.FilterScheduler;
import com.server.VideoServer;
import com.server.WirelessServer;
import com.service.FilterMac;


/*
 * @author shenh
 * @time 2019/03/07
 * 程序入口
 */
public class Main {

	private static Logger logger = Logger.getLogger(Main.class);
	public static Storage storage = new Storage();
	public static Storage picure_storage = new Storage();
	
	public static void main(String[] args) throws Exception {
		
		FilterMac.loadFixedMac();
		
		FilterScheduler.schedulerJob();
		
		Consumer consumer = new Consumer(storage);
		Thread thread = new Thread(consumer);
		thread.start();

		Thread thread2 = new Thread(new PictureConsumer(picure_storage));
		thread2.start();

		new Thread(new Runnable() {
			public void run() {
				logger.info("开启接收视频设备数据的服务");
				try {
					new VideoServer().start(OtherConfig.VideoPort);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		logger.info("开启接收无线设备数据的服务");
		new WirelessServer().start(OtherConfig.WirelessPort);
	}
	
}
