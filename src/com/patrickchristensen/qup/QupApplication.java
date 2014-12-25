package com.patrickchristensen.qup;

import com.patrickchristensen.qup.util.Utils;

import android.app.Application;
import android.content.Context;

public class QupApplication extends Application{
	
	public static final int PAGE_SERVER = 0;
	public static final int PAGE_CLIENT = 1;
	
	public static final int serverPort = 8080;
	public static final String IPADDRESS = Utils.getIPAddress(true);
	public static final boolean DEVELOPER_MODE = true;
	
	public static int currentPage = 0;
	public static Context appContext;
	
	//SET FALSE ON SERVER
	//SET TRUE ON CLIENT
	public static final boolean DEBUG = true;

}
