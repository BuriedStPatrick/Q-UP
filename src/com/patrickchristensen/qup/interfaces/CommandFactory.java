package com.patrickchristensen.qup.interfaces;

import com.patrickchristensen.qup.commands.Command;

public interface CommandFactory {
	
	void executeCommand(Command command);

}
