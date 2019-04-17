package com.messageQueue;

import java.sql.SQLException;
import java.util.Set;

import org.apache.log4j.Logger;

import com.dao.ConnectionRedis;
import com.dao.ResultDao;
import com.dao.impl.ResultDaoImpl;
import com.faceComparison.FaceComparisonTest;
import com.service.CalculateMacs;

import redis.clients.jedis.Jedis;

public class PictureConsumer implements Runnable{

	private Logger logger = Logger.getLogger(PictureConsumer.class);
	
	private Storage storage;
	
	public PictureConsumer(Storage storage) {
		this.storage = storage;
	}
	
	public void run() {
		CalculateMacs calculateMacs = new CalculateMacs();
		Jedis jedis = ConnectionRedis.getJedis();
		
		while (true) {
			try {
				String imagePath1 = storage.pop();
				
		        Set<String> set = jedis.keys("picture_*"); 
		        
		        logger.info(imagePath1 + "已弹出    " + set.size());
		        for (String key : set) { 
		        	
		        	String imagePath2 = key.substring(8);
		        	
		        	if(imagePath2.equals(imagePath1))
		        		continue;
		        	
		            double score = FaceComparisonTest.match(imagePath1, imagePath2);
		            
		            logger.info("分数" + score);
		            
		            if(score >= 70) {
		            	String value1 = jedis.get("picture_" + imagePath1);
		            	String value2 = jedis.get("picture_" + imagePath2);
		            	
		            	logger.info(value1 + " " + value2 + "  "+ imagePath1 + " " + imagePath2);
		            	
		            	String macsIntersection =  calculateMacs.getTwoMacsIntersection(value1, value2);
		            	logger.info(imagePath1 + "  " + value1 + "\n"+ imagePath2 + value2 +
		            			"\n分数" + score + "   " +macsIntersection);
		            	
		            	if(macsIntersection.length() == 0) {
							jedis.del("picture_" + imagePath1);  //这里有一个问题，假设一个mac对应一个人脸写入数据库了，那么应该直接判断是否在数据库中
							jedis.del("picture_" + imagePath2);
		            	}
		                
						if(macsIntersection.length() > 0 && !macsIntersection.contains("\t")) {
							ResultDao resultDao = new ResultDaoImpl();
							resultDao.insertResult(macsIntersection, imagePath1);
							jedis.del("picture_" + imagePath1);  //这里有一个问题，假设一个mac对应一个人脸写入数据库了，那么应该直接判断是否在数据库中
							jedis.del("picture_" + imagePath2);
						}else {
							jedis.del("picture_" + imagePath2);
							jedis.set("picture_" + imagePath1, macsIntersection);
						}
		            }
		        }  
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ConnectionRedis.releaseJedis(jedis);
		}
	}
}
