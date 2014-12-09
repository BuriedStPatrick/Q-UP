package com.patrickchristensen.qup.commands;


public class Command{
	
	private long 	userId;
	private int		command;
	private String	data;
	
	public Command(long userId, int command, String data){
		this.userId = userId;
		this.command = command;
		this.data = data;
	}
	
	public int getCommand() {
		return command;
	}
	
	public long getUserId() {
		return userId;
	}
	
	public String getData() {
		return data;
	}

}
