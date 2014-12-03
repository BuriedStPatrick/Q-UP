package com.patrickchristensen.qup.commands;

import com.patrickchristensen.qup.interfaces.CommandFactory;
import com.patrickchristensen.qup.interfaces.CommandInterface;

public class Command implements CommandInterface{
	
	private long 	userId;
	
	public Command(long userId){
		this.userId = userId;
	}
	
	public synchronized void executeCommand(CommandFactory factory){
		
	}

}
