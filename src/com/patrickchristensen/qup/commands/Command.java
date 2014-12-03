package com.patrickchristensen.qup.commands;

import android.app.Activity;

import com.patrickchristensen.qup.interfaces.CommandInterface;

public class Command implements CommandInterface{
	
	private String 	command;
	private long 	userId;
	
	public Command(long userId, String command){
		this.command = command;
		this.userId = userId;
	}

	@Override
	public String getCommand() {
		return command;
	}

	@Override
	public void setCommand(String command) {
		this.command = command;
	}
	
	public synchronized void executeCommand(Activity activity){
		
	}

}
