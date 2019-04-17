package com.Test;

import java.util.HashSet;
import java.util.List;

import com.dao.ConnectionRedis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.messageQueue.Storage;
import com.pojo.SignalFeature;
import com.pojo.Sniffer;

import redis.clients.jedis.Jedis;

public class Test {

	public static Storage storage = new Storage();
	
	public String getTwoMacsUnion(String macs1, String macs2) {
		String [] mac1Split = macs1.split("\t");
		String [] mac2Split = macs2.split("\t");
		
		HashSet<String> macHashSet = new HashSet<String>();
		for(int i = 0; i < mac1Split.length; i ++) {
			macHashSet.add(mac1Split[i]);
		}
		
		for(int i = 0; i < mac2Split.length; i ++) {
			if(macHashSet.contains(mac2Split[i]))
				continue;
			macHashSet.add(mac2Split[i]);
		}
		
		String result = "";
        for (String string : macHashSet) {
			result += string + "\t";
		}		
        
        return result.substring(0, result.length() - 1);
        
	}
	
	public static void main(String [] args) throws InterruptedException {
		
		Jedis jedis = ConnectionRedis.getJedis();
		
		Gson gson = new GsonBuilder().create();
		
		String message = "{ \"sn\": \"MQ2BB85150000163\", \"type\": \"1\", \"data\": [ { \"mac\": \"3C:28:6D:DA:64:4D\", \"ssid\": \"\", \"rssi\": \"83\", \"stimestamp\": \"1551324210\" }, { \"mac\": \"72:98:0B:7D:2A:86\", \"ssid\": \"\", \"rssi\": \"86\", \"stimestamp\": \"1551324210\" } ] }\r\n" + 
				"";
		String message1 = "{ \"sn\": \"MQ2BB85150000163\", \"type\": \"1\", \"data\": [ { \"mac\": \"3F:28:6D:DA:64:4D\", \"ssid\": \"\", \"rssi\": \"83\", \"stimestamp\": \"1551324210\" }, { \"mac\": \"72:98:0B:7D:2A:86\", \"ssid\": \"\", \"rssi\": \"86\", \"stimestamp\": \"1551324210\" } ] }\r\n" + 
				"";
		
		System.err.println(new Test().getTwoMacsUnion(message, message1));
		
		Sniffer sniffer = gson.fromJson(message, Sniffer.class);
		
		List<SignalFeature> data = sniffer.getData();
		HashSet<String> hashSet = new HashSet<String>();
		
		for (SignalFeature signalFeature : data) {
			hashSet.add(signalFeature.mac);
			
		}
		jedis.set(sniffer.sn + "_" + sniffer.type, hashSet.toString());
		jedis.expire(sniffer.sn + "_" + sniffer.type, 20);
		
		
		
		System.out.println(jedis.get(sniffer.sn + "_" + sniffer.type));
		
		Thread.sleep(1000);
		
		System.out.println(jedis.ttl(sniffer.sn + "_" + sniffer.type).intValue());
		
		System.out.println(jedis.get(sniffer.sn + "_" + sniffer.type));
		
		System.out.println(hashSet.toString());
		System.out.println("消费者：" + sniffer.getData());
		System.out.println(message);
	}
}
