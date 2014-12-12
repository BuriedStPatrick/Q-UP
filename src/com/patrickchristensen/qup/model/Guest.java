package com.patrickchristensen.qup.model;

public class Guest {
	
	private String name;
	private String ipAddress;
	
	public Guest(String ipAddress) {
		this("Anonymous", ipAddress);
	}
	
	public Guest(String name, String ipAddress) {
		this.name = name;
		this.ipAddress = ipAddress;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}
	
	public String getName() {
		return name;
	}

}
