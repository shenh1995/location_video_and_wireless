package com.schedule;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.db.ConnectionRedis;

import redis.clients.jedis.Jedis;

public class FilterJob implements Job {
	
	private static Logger logger = Logger.getLogger(FilterJob.class);

	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		Jedis jedis = ConnectionRedis.getJedis();
		
		Set<String> set = new HashSet<String>();
		set = jedis.keys("MQ*");

		for (int i = 0; i < 5; i++) {
			for (String key : set) {
				if (jedis.ttl(key) < 15) {
					String[] macs = jedis.get(key).split("\t");
					for (String string : macs) {
						FilterScheduler.filterMacSet.add(string);
					}
				}	
			}
			 logger.info(FilterScheduler.filterMacSet.toString());
			try {
				Thread.sleep(40000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
   
		 jedis.close();
	}
}
