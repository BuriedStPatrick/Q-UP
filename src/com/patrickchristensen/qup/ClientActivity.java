package com.patrickchristensen.qup;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.patrickchristensen.qup.commands.Command;
import com.patrickchristensen.qup.listeners.DrawerItemListener;
import com.patrickchristensen.qup.model.Song;
import com.patrickchristensen.qup.model.SongQueue;
import com.patrickchristensen.qup.threads.ClientThread;

public class ClientActivity extends Activity implements Observer{
	
	private SongQueue				songQueue;
	
	private ActionBarDrawerToggle 	drawerListener;
	private ListView				drawerList;
	private ListView				queueList;
	private DrawerLayout			drawerLayout;
	private Toolbar					toolbar;
	private TextView 				serverStatus;

	private ArrayAdapter<String> 	pages;
	private ArrayAdapter<Song>	songs;
	
	private EditText 		serverIp;
	private String 			serverIpAddress = "";
	private Button			connectBtn;
	
	public static boolean	connected = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		QupApplication.appContext = getApplicationContext();
		setContentView(R.layout.activity_client);
		songQueue = new SongQueue();
		
		pages = new ArrayAdapter<String>(this, R.id.drawer_list);
		songs = new ArrayAdapter<Song>(this, R.id.queue_list, songQueue.getSongs());
		//TODO: Need a class to extends ArrayAdapter, so that we can use Song-objects instead of Strings
		initViews();
		initDrawer();
	}
	
	private void initViews(){
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		serverStatus = (TextView) findViewById(R.id.server_status);
		drawerList = (ListView) findViewById(R.id.drawer_list);
		queueList = (ListView) findViewById(R.id.queue_list);
		
		serverIp = (EditText) findViewById(R.id.server_ip);
		connectBtn = (Button) findViewById(R.id.connect_btn);
		
		//TODO: Move out to separate method
		connectBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				serverIpAddress = serverIp.getText().toString();
				if(!connected){
					serverIpAddress = serverIp.getText().toString();
					if(!serverIpAddress.isEmpty()){
						new Thread(new ClientThread(serverIpAddress, getClientHandler(), new Command(Command.CONNECT))).start();
					}
				}
			}
		});
		queueList.setAdapter(songs);
		queueList.setOnItemClickListener(listQueueListener);
	}
	
	private void initDrawer(){
		
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
	
	private Handler getClientHandler(){
		return new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Gson json = new Gson();
				Command command =
						json.fromJson(msg.getData().getString("data"), Command.class);
				
				switch(command.getAction()){
				//Receiving song list from server
				case Command.FETCH_SONGS:
					Toast.makeText(getApplicationContext(), "Fetching songs", Toast.LENGTH_LONG).show();
					//TODO
					break;
				}
			}
		};
	}
	
	private ListView.OnItemClickListener listQueueListener = new ListView.OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//TODO: Vote on song
			Toast.makeText(getApplicationContext(), "Voted on: " + position, Toast.LENGTH_SHORT).show();
		}
		
	};

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		if(observable instanceof SongQueue){
			SongQueue songQueue = (SongQueue) observable;
			
		}
	}

}
