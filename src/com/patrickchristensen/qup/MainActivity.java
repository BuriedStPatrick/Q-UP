package com.patrickchristensen.qup;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.internal.widget.AdapterViewCompat.OnItemClickListener;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnItemClickListener{
	
	public static final String SERVERIP = "";
	public static final int SERVERPORT = 8080;
	private String actionBar = "Now Playing";
	
	private ActionBarDrawerToggle 	drawerListener;
	private ListView				drawerList;
	private DrawerLayout			drawerLayout;
	private Toolbar					toolbar;
	
	private TextView serverStatus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initDrawer();
	}
	
	private void initView(){
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		serverStatus = (TextView) findViewById(R.id.server_status);
		drawerList = (ListView) findViewById(R.id.drawer_list);
		updateViews();
	}
	
	private void initDrawer(){
		drawerListener = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer){
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				invalidateOptionsMenu();
			}
			
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				invalidateOptionsMenu();
			}
		};
		drawerLayout.setDrawerListener(drawerListener);
		drawerList.setOnItemClickListener((android.widget.AdapterView.OnItemClickListener) this);
		
		drawerLayout.post(new Runnable() {
			@Override
			public void run() {
				drawerListener.syncState();
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
	public void onItemClick(AdapterViewCompat<?> parent, View view, int position,
			long id) {
		Toast.makeText(this, position, Toast.LENGTH_LONG).show();
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
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
