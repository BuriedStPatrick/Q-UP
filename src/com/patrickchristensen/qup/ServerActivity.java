package com.patrickchristensen.qup;

import java.util.Observable;
import java.util.Observer;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.patrickchristensen.qup.commands.Command;
import com.patrickchristensen.qup.listeners.DrawerItemListener;
import com.patrickchristensen.qup.model.SongQueue;
import com.patrickchristensen.qup.threads.ReceiverThread;
import com.patrickchristensen.qup.threads.SenderThread;
import com.patrickchristensen.qup.util.Utils;

public class ServerActivity extends ActionBarActivity implements Observer{
	
	private final String actionBar = "Now Playing";
	private SongQueue songQueue;
	
	private ActionBarDrawerToggle 	drawerListener;
	private ListView				drawerList;
	private DrawerLayout			drawerLayout;
	private Toolbar					toolbar;
	private TextView 				serverStatus;

	private ArrayAdapter<String> 	pages;
	
	private Thread 					receiverThread;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		QupApplication.appContext = getApplicationContext();
		setContentView(R.layout.activity_server);
		
		initView();
		initDrawer();
		receiverThread = new Thread(new ReceiverThread(getReceiverHandler()));
		receiverThread.start();	//starts listening for connections in the background
		QupApplication.currentPage = 2;
		serverStatus.setText("Listening on: " + Utils.getIPAddress(true));
		songQueue = new SongQueue();
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
				Toast.makeText(getApplicationContext(), "derp", Toast.LENGTH_SHORT).show();
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
        receiverThread.interrupt();
    }
	
	private Handler getReceiverHandler(){
		return new Handler(){
			
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Gson json = new Gson();
				Command command =
						json.fromJson(msg.getData().getString("data"), Command.class);
				
				switch(command.getAction()){
				case Command.CONNECT:
					Toast.makeText(getApplicationContext(), "Connected IP: " + command.getSenderIp(), Toast.LENGTH_LONG).show();
					break;
				case Command.DISCONNECT:
					Toast.makeText(getApplicationContext(), "Disconnect", Toast.LENGTH_LONG).show();
					break;
				case Command.VOTE_SONG:
					Toast.makeText(getApplicationContext(), "Vote song: " + command.getData(), Toast.LENGTH_LONG).show();
					break;
				case Command.FETCH_SONGS:
					Toast.makeText(getApplicationContext(), "Fetch song list from IP: " + command.getSenderIp(), Toast.LENGTH_LONG).show();
					sendCommand(new Command(Command.UPDATE_SONG_QUEUE, songQueue, QupApplication.IPADDRESS, command.getSenderIp()));
					break;
				default:
					Toast.makeText(getApplicationContext(), "Command invalid", Toast.LENGTH_LONG).show();
					break;
				}
			}
		};
	}
	
	private void sendCommand(Command command){
		Toast.makeText(getApplicationContext(), "sendCommand", Toast.LENGTH_SHORT).show();
		new Thread(new SenderThread(command)).start();
	}

	@Override
	public void update(Observable observable, Object data) {
	}
}
