package com.patrickchristensen.qup.interfaces;

import android.app.Activity;

public interface CommandInterface {
	
	String getCommand();
	void setCommand(String command);
	void executeCommand(Activity activity);
	
}
