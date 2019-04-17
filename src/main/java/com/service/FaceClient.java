package com.service;

import com.baidu.aip.face.AipFace;
import com.config.FaceApiConfig;

public class FaceClient {

	public AipFace getFaceClinet() {
		AipFace client = new AipFace(FaceApiConfig.APP_ID, FaceApiConfig.API_KEY, FaceApiConfig.SECRET_KEY);
		
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(600000);
		
		return client;
	}
	
}
