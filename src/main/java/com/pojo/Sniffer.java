package com.pojo;

import java.util.List;


/**
 * @author shenh
 * 无线采集设备上传数据
 */
public class Sniffer {

	public String sn;   //设备编号
	public int type;    //上传数据种类，1代表wifi，2代表beacon
	public List<SignalFeature> data;  //数据列表
	
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public List<SignalFeature> getData() {
		return data;
	}
	public void setData(List<SignalFeature> data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "Sniffer [sn=" + sn + ", type=" + type + ", features=" + data + "]";
	}
	
}
