package com.patrickchristensen.qup;

import com.patrickchristensen.qup.util.Utils;

import android.app.Application;
import android.content.Context;

public class QupApplication extends Application{
	
	public static final int serverPort = 8080;
	public static final String IPADDRESS = Utils.getIPAddress(true);
	public static final boolean DEVELOPER_MODE = true;
	
	public static int currentPage = 0;
	public static Context appContext;
	
	//SET FALSE IN PROD
	public static final boolean DEBUG = true;

}
