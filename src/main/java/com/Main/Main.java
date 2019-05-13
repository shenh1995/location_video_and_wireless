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
	
	//下面是两个阻塞队列，充当存储仓库的作用
	public static Storage storage = new Storage();    
	public static Storage picure_storage = new Storage();
	
	public static void main(String[] args) throws Exception {
		
		//将要过滤的mac加载进来
		FilterMac.loadFixedMac();
		//这里是一个定时器，也是为了获得需要过滤的mac，当前没有实际用这个来过滤
		FilterScheduler.schedulerJob();
		
		//这里启动一个针对无线数据的消费者
		Consumer consumer = new Consumer(storage);
		Thread thread = new Thread(consumer);
		thread.start();

		//这里启动一个针对摄像头数据的消费者
		Thread thread2 = new Thread(new PictureConsumer(picure_storage));
		thread2.start();

		//启动接收摄像头数据服务
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

		//启动接收无线数据服务
		logger.info("开启接收无线设备数据的服务");
		new WirelessServer().start(OtherConfig.WirelessPort);
	}
	
}
