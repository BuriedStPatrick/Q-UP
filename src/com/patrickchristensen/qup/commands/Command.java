package com.patrickchristensen.qup.commands;


public class Command{
	
	private int		command;
	private String	data;
	
	public Command(int command, String data){
		this.command = command;
		this.data = data;
	}
	
	public int getCommand() {
		return command;
	}
	
	public String getData() {
		return data;
	}

}
