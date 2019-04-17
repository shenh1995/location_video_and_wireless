package com.pojo;

public class SignalFeature {

	public String mac;
	public int rssi;
	public String stimestamp;
	
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public int getRssi() {
		return rssi;
	}
	public void setRssi(int rssi) {
		this.rssi = rssi;
	}
	
	public String getStimestamp() {
		return stimestamp;
	}
	public void setStimestamp(String stimestamp) {
		this.stimestamp = stimestamp;
	}
	@Override
	public String toString() {
		return "SignalFeature [mac=" + mac + ", rssi=" + rssi + ", timeStamp=" + stimestamp + "]";
	}
	
}
