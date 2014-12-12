package com.patrickchristensen.qup;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.google.gson.GsonBuilder;
import com.patrickchristensen.qup.adapters.SongAdapter;
import com.patrickchristensen.qup.commands.Command;
import com.patrickchristensen.qup.listeners.DrawerItemListener;
import com.patrickchristensen.qup.model.Song;
import com.patrickchristensen.qup.model.SongQueue;
import com.patrickchristensen.qup.threads.ReceiverThread;
import com.patrickchristensen.qup.threads.SenderThread;
import com.patrickchristensen.qup.util.SongQueueDeserializer;

public class ClientActivity extends ActionBarActivity implements Observer{
	
	private SongQueue				songQueue;
	
	private ActionBarDrawerToggle 	drawerListener;
	private ListView				drawerList;
	private ListView				queueList;
	private DrawerLayout			drawerLayout;
	private Toolbar					toolbar;
	private TextView 				serverStatus;

	private ArrayAdapter<String> 	pages;
	private SongAdapter				songAdapter;
	
	private EditText 				serverIp;
	private String 					serverIpAddress = "";
	private final String 			actionBar = "Client";
	private Button					connectBtn;
	private Button					getSongsBtn;
	private Thread					receiverThread;
	
	public static boolean			connected = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		QupApplication.appContext = getApplicationContext();
		setContentView(R.layout.activity_client);
		songQueue = new SongQueue();
		
		pages = new ArrayAdapter<String>(this, R.id.drawer_list);
		songAdapter = new SongAdapter(this, songQueue);
		initViews();
		initDrawer();
		QupApplication.currentPage = 1;
		receiverThread = new Thread(new ReceiverThread(getReceiverHandler()));
		receiverThread.start();	//starts listening for connections in the background
	}
	
	private void initViews(){
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		serverStatus = (TextView) findViewById(R.id.server_status);
		drawerList = (ListView) findViewById(R.id.drawer_list);
		queueList = (ListView) findViewById(R.id.queue_list);
		
		serverIp = (EditText) findViewById(R.id.server_ip);
		connectBtn = (Button) findViewById(R.id.connect_btn);
		getSongsBtn = (Button) findViewById(R.id.getsongs_btn);
		
		//TODO: Move out to separate method
		connectBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				serverIpAddress = serverIp.getText().toString();
				if(!connected){
					serverIpAddress = serverIp.getText().toString();
					if(!serverIpAddress.isEmpty()){
						new Thread(
								new SenderThread(
										new Command(Command.CONNECT, QupApplication.IPADDRESS, serverIpAddress)))
											.start();
					}
				}
			}
		});
		
		getSongsBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(!serverIpAddress.isEmpty()){
					Toast.makeText(getApplicationContext(), QupApplication.IPADDRESS + "serverIP: " + serverIpAddress, Toast.LENGTH_SHORT).show();
					new Thread(new SenderThread(new Command(Command.FETCH_SONGS, QupApplication.IPADDRESS, serverIpAddress))).start();
				}else{
					Toast.makeText(getApplicationContext(), "Please connect first", Toast.LENGTH_SHORT).show();
				}
			}
		});
		queueList.setAdapter(songAdapter);
		queueList.setOnItemClickListener(listQueueListener);
		
		if(toolbar != null){
			toolbar.setTitle(actionBar);
			setSupportActionBar(toolbar);
		}
	}
	
	private Handler getReceiverHandler(){
		return new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Gson json = new GsonBuilder()
								.registerTypeAdapter(SongQueue.class, new SongQueueDeserializer())
								.create();
				
				String data = msg.getData().getString("data");
				Log.d("customtag", data);
				Command command =
						json.fromJson(data, Command.class);
				
				switch(command.getAction()){
				case Command.UPDATE_SONG_QUEUE:
					//TODO: update song queue from data-object
//					songQueue = (SongQueue) command.getData();
//					SongQueue _queue = json.fromJson(command.getData(), SongQueue.class);
//					Log.d("customtag", "_queue is: " + _queue);
//					songQueue.updateSongs(_queue.getSongs());
					break;
				}
			}
		};
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
