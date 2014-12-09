package com.patrickchristensen.qup;

import android.app.Application;
import android.content.Context;

public class QupApplication extends Application{
	
	public static int currentPage = 0;
	public static int serverPort = 8080;
	public static final boolean DEVELOPER_MODE = true;
	public static Context appContext;
	
	//SET FALSE IN PROD
	public static final boolean DEBUG = true;
	
	//COMMANDS
	public static final int CONNECT = 0;
	public static final int VOTE_SONG = 1;
	public static final int DISCONNECT = 2;

}
