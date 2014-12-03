package com.patrickchristensen.qup;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.patrickchristensen.qup.R;
import com.patrickchristensen.qup.threads.ServerThread;

public class ServerActivity extends ActionBarActivity{
	
	public static String SERVERIP = "";
	private String actionBar = "Now Playing";
	
	private ActionBarDrawerToggle 	drawerListener;
	private ListView				drawerList;
	private DrawerLayout			drawerLayout;
	private Toolbar					toolbar;
	private TextView 				serverStatus;

	private ArrayAdapter<String> 	pages;
	private Thread					serverThread;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_server);
		initView();
		initDrawer();
		serverThread = new Thread(new ServerThread(serverStatus));
		serverThread.start();
	}
	
	private void initView(){
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		serverStatus = (TextView) findViewById(R.id.server_status);
		drawerList = (ListView) findViewById(R.id.drawer_list);
		updateViews();
	}
	
	private void initDrawer(){
		pages = new ArrayAdapter<String>(this, R.id.drawer_list);
		drawerListener = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer){
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				invalidateOptionsMenu();
				drawerList.bringToFront();
			}
			
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				invalidateOptionsMenu();
			}
			
		};
		drawerLayout.setDrawerListener(drawerListener);
		
		drawerList.setOnItemClickListener(new ListView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch(position){
				case 0:
					if(QupApplication.currentPage !=  0)
						startActivity(new Intent(getApplicationContext(), ServerActivity.class));
					break;
				case 1:
					if(QupApplication.currentPage != 1)
						startActivity(new Intent(getApplicationContext(), ClientActivity.class));
					break;
				}
			}
		});
	}
	
	private void updateViews(){
		if(toolbar != null){
			toolbar.setTitle(actionBar);
			setSupportActionBar(toolbar);
		}
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerListener.syncState();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    drawerListener.onConfigurationChanged(newConfig);
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
		if(drawerListener.onOptionsItemSelected(item)){
			invalidateOptionsMenu();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
    protected void onStop() {
        super.onStop();
        serverThread.interrupt();
    }
}
