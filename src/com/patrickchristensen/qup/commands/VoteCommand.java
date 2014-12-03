package com.patrickchristensen.qup.commands;

import com.patrickchristensen.qup.interfaces.CommandFactory;

public class VoteCommand extends Command{
	
	private long songId;

	public VoteCommand(long userId, String command, long songId) {
		super(userId);
		this.songId = songId;
	}
	
	@Override
	public synchronized void executeCommand(CommandFactory factory) {
		super.executeCommand(factory);
		factory.executeCommand(this);
	}

}
