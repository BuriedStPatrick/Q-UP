package com.patrickchristensen.qup;

import java.util.ArrayList;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.patrickchristensen.qup.adapters.SongAdapter;
import com.patrickchristensen.qup.commands.Command;
import com.patrickchristensen.qup.listeners.DrawerItemListener;
import com.patrickchristensen.qup.model.Song;
import com.patrickchristensen.qup.model.SongQueue;
import com.patrickchristensen.qup.threads.SenderThread;

public class QupActivity extends ActionBarActivity{
	
	public static SongQueue 		songQueue;
	
	private ActionBarDrawerToggle 	drawerListener;
	private ListView				drawerList;
	private DrawerLayout			drawerLayout;
	private Toolbar					toolbar;
	private SongAdapter				songAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		QupApplication.appContext = getApplicationContext();
	}
	
	protected void initLogic(){
		songAdapter = new SongAdapter(this, songQueue);
		songAdapter.notifyDataSetChanged();
	}
	
	protected void initView(){
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.drawer_list);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		
		if(toolbar != null){
			toolbar.setTitle(getResources().getStringArray(R.array.menu_categories)[QupApplication.currentPage]);
			setSupportActionBar(toolbar);
			toolbar.setTitleTextColor(Color.WHITE);
		}
	}
	
	protected void initDrawer(){
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
		drawerList.setOnItemClickListener(new DrawerItemListener(this));
	}
	
	protected void updateSongQueue(ArrayList<Song> songs){
		songQueue.updateSongs(songs);
	}
	
	protected void setSongQueue(boolean loadLocal){
		songQueue = new SongQueue(this, loadLocal);
	}
	
	protected SongAdapter getSongAdapter(){
		return songAdapter;
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
	
	protected void sendCommand(Command command){
		new Thread(new SenderThread(command)).start();
	}

}
