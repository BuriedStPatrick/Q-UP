package com.patrickchristensen.qup;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public static final String SERVERIP = "";
	public static final int SERVERPORT = 8080;
	
	public static Context appContext;
	
//	private ActionBarDrawerToggle 	drawerListener;
	private DrawerLayout			drawerLayout;
	
	private TextView serverStatus;
	private LinearLayout mainLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		appContext = getApplicationContext();
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
//		drawerListener = new ActionBarDrawerToggle(
//				this, drawerLayout,
//				R.string.open_drawer, R.string.close_drawer){
//			
//			@Override
//			public void onDrawerOpened(View drawerView) {
//				super.onDrawerOpened(drawerView);
//				Toast.makeText(appContext, "Opened", Toast.LENGTH_SHORT).show();
//			}
//			
//			@Override
//			public void onDrawerClosed(View drawerView) {
//				super.onDrawerClosed(drawerView);
//				Toast.makeText(appContext, "Opened", Toast.LENGTH_SHORT).show();
//			}
//		};
		
//		drawerLayout.setDrawerListener(drawerListener);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
