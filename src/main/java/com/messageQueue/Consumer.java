package com.messageQueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.Main.Main;
import com.dao.ConnectionRedis;
import com.dao.HumanPictureDao;
import com.dao.PictureAndWifiDao;
import com.dao.WifiDao;
import com.dao.impl.HumanPictureDaoImpl;
import com.dao.impl.PictureAndWifiDaoImpl;
import com.dao.impl.WifiDaoImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pojo.HumanPicture;
import com.pojo.SignalFeature;
import com.pojo.Sniffer;
import com.pojo.Wifi;
import com.service.CalculateMacs;

import redis.clients.jedis.Jedis;

public class Consumer implements Runnable{
	
	private static Logger logger = Logger.getLogger(Consumer.class);

	private Storage storage = null;
	
	public Consumer(Storage storage) {
		this.storage = storage;
	}
	
	/**
	 * 	将采集到的一条指纹插入数据库中
	 * @param pictureLists
	 * @param videoDeviceId
	 * @param wifiMacs
	 * @param wifiDeviceId
	 * @param time
	 */
	public void insertInfoToDatabase(List<String> pictureLists, String videoDeviceId, String wifiMacs,  String wifiDeviceId, long time) {
		List<Wifi> wifis = new ArrayList<Wifi>();
		String [] wifi_macs = wifiMacs.split("\t");
		for (String string : wifi_macs) {
			Wifi wifi = new Wifi();
			wifi.setMac_name(string);
			wifi.setTime(""+time);
			wifi.setDevice_id(wifiDeviceId);
			wifis.add(wifi);
		}
	    WifiDao wifiDao = new WifiDaoImpl();
	    wifiDao.insertWifi(wifis);
	    
	    List<HumanPicture> humanPictures = new ArrayList<HumanPicture>();
	    for (String string : pictureLists) {
			HumanPicture humanPicture = new HumanPicture();
			humanPicture.setDevice_id(videoDeviceId);
			humanPicture.setTime("" + time);
			humanPicture.setPicture_path(string);
			humanPictures.add(humanPicture);
		}
	    HumanPictureDao humanPictureDao = new HumanPictureDaoImpl();
	    humanPictureDao.insertHumanPicture(humanPictures);
	}
	
	public void run() {
		
		CalculateMacs calculateMacs = new CalculateMacs();
		Jedis jedis = ConnectionRedis.getJedis();
		Gson gson = new GsonBuilder().create();

		PictureAndWifiDao pictureAndWifiDao = new PictureAndWifiDaoImpl();
		HashMap<String, String> wifi_video_device_map = pictureAndWifiDao.getVedioDeviceMap();
		
		try {
			
			while (true) {
				String product = storage.pop();
				if(product == "")
					continue;
				System.out.println("" + product);
				Sniffer sniffer = gson.fromJson(product, Sniffer.class);
				List<SignalFeature> data = sniffer.getData();
				
				if(data.size() == 0)
					continue;
				
				String value = "";
				
				for(int i = 0; i < data.size() - 1; i ++) {
					if(data.get(i).getRssi() <= 58) {
						value += data.get(i).getMac() + "\t";
					}
				}
				if(data.get(data.size() - 1).getRssi() <= 58) {
					value += data.get(data.size() - 1).getMac();
				}
				
				if (jedis.get(sniffer.getSn() + "_" + sniffer.getType()) != null) {
					String result = calculateMacs.getTwoMacsUnion(jedis.get(sniffer.getSn() + "_" + sniffer.getType()), value);
					int remainTime = jedis.ttl(sniffer.getSn() + "_" + sniffer.getType()).intValue();
					
					if(remainTime < 15) {
						String video_device_id = wifi_video_device_map.get(sniffer.getSn());
						long current_time = Long.parseLong(sniffer.data.get(0).getStimestamp());
						List<String> picturePathList = new ArrayList<String>();
						boolean hasPicture = false;
					//	logger.info(video_device_id + "  " +current_time);
						for(long i = current_time; i > current_time - 60; i --) {
							
							if(jedis.hget(video_device_id, "" + i) != null) {
								picturePathList.add(video_device_id+ "/" + i + ".jpg");
								jedis.hdel(video_device_id, "" + i);
								hasPicture = true;
							}
						}
						if(hasPicture == true) {
							insertInfoToDatabase(picturePathList, video_device_id, result, sniffer.getSn(), current_time);
							
							for (String string : picturePathList) {
								jedis.set("picture_" + string, result);
							//	logger.info("picture_" + string + " " + result);
								Main.picure_storage.push(string);
							}
							
							jedis.del(sniffer.getSn() + "_" + sniffer.getType());
						}
					}else {
						jedis.del(sniffer.getSn() + "_" + sniffer.getType());
						jedis.set(sniffer.getSn() + "_" + sniffer.getType(), result);  
						jedis.expire(sniffer.getSn() + "_" + sniffer.getType(), remainTime);
					}
					
				}else {
					jedis.set(sniffer.getSn() + "_" + sniffer.getType(), value);
					jedis.expire(sniffer.getSn() + "_" + sniffer.getType(), 60);
				}
//				System.out.println("消费者：\t" + sniffer.getSn() + "\t"+ jedis.get(sniffer.getSn() + "_" + sniffer.getType()));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			ConnectionRedis.releaseJedis(jedis);
		}
	}
}
