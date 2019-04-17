package com.Test;

import java.util.HashSet;
import java.util.Set;

import com.dao.ConnectionRedis;

import redis.clients.jedis.Jedis;

public class redisTest {
	
	public static void main(String[] args) {
		
		Jedis jedis = ConnectionRedis.getJedis();
		
		String test = "picture_123";
		
		
		System.out.println(test.substring(8));
		
		if(jedis.hget("122", "dfa") != null) {
			System.out.println("ok");
		}
		
		String s = "";
		System.out.println(" " + s.length());
		
		System.out.println(jedis.hget("122", "dfa"));
			
		Set<String> set = new HashSet<String>();
        set = jedis.keys("*");  
        for (String key : set) {  
        	jedis.del(key);
       }  

        set = jedis.keys("picture_*"); 
        for(String key: set) {
        	System.out.println(key + " " + jedis.get(key));
        }
		
        jedis.set("fasdf", "fafdfasf");
        jedis.set("fasdf", "dfas");
        if(jedis.get("fasdf") == null) {
        	System.out.println("ok");
        }
        System.out.println(jedis.get("fasdf"));
        
		jedis.hset("test", "" + 1552274441, "12");
		
		if(jedis.hdel("test", "1") == 1) 
			System.out.println("ok");
		System.out.println(jedis.hdel("test", "1"));
		System.out.println(jedis.hdel("test", "2"));
	}
}
