package com.messageQueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.config.MqConfig;

/**
 * @author shenh
 *	 设置一个阻塞队列作为消息队列
 */
public class Storage {

	 BlockingQueue<String> queues = new LinkedBlockingDeque<String>(MqConfig.MQ_SIZE);
	 
	 public void push(String p) throws InterruptedException {
		 //LinkedBlockingDeque如果队列空间不足，会一直阻塞
		 queues.put(p);
	 }
	 
	 public String pop() throws InterruptedException {
		 //LinkedBlockingDeque如果为空会一直阻塞，直到取到值
		 return queues.take();
	 }
}
