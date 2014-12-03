package com.patrickchristensen.qup.commands;

import android.app.Activity;

public class VoteCommand extends Command{

	public VoteCommand(long userId, String command) {
		super(userId, command);
		
	}
	
	@Override
	public synchronized void executeCommand(Activity activity) {
		super.executeCommand(activity);
		
		
	}

}
