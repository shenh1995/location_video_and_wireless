package com.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pojo.HumanPicture;
import com.pojo.Wifi;
import com.service.DbHelp;

public class TempServer {

	private String inputFile = "E:\\北大杭研院\\专利\\视觉信号与无线信号的跟踪定位方法\\模拟输入\\input.txt";
	
	public void temp_server() throws IOException{
		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(inputFile)));
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String readline = null;
		
		while ((readline = bufferedReader.readLine()) != null) {
			String [] intput_split = readline.split("\t");
			
			List<Wifi> wifis = new ArrayList<Wifi>();
			List<HumanPicture> humanPictures = new ArrayList<HumanPicture>();
			
			int wifi_device_id = Integer.parseInt(intput_split[0]);
			int wifi_mac_num = Integer.parseInt(intput_split[1]);
			
		    int i = 2;
			for(; i < wifi_mac_num + 2; i ++) {
				Wifi wifi = new Wifi();
				wifi.setDevice_id(wifi_device_id);
				wifi.setMac_name(intput_split[i]);
				wifi.setTime(df.format(new Date()));
				wifis.add(wifi);
			}
			
			int video_device_id = Integer.parseInt(intput_split[i++]);
			
			i ++;
			for(; i < intput_split.length; i ++) {
				HumanPicture humanPicture = new HumanPicture();
				humanPicture.setDevice_id(video_device_id);
				humanPicture.setPicture_path(intput_split[i]);
				humanPicture.setTime(df.format(new Date()));
				humanPictures.add(humanPicture);
			}
			
			
			new DbHelp().storageInfoToDB(wifis, humanPictures, wifi_device_id, video_device_id);
		}
		
		bufferedReader.close();
	}
	
	public static void main(String [] args) throws IOException {
		new TempServer().temp_server();
	}
}
