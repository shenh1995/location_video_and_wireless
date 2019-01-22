package com.Main;

import com.config.OtherConfig;
import com.server.Netty;

public class Main {

	public static void main(String [] args) throws Exception {
		new Netty().bind(OtherConfig.port);
		System.out.println(OtherConfig.port);
	}
	
}
