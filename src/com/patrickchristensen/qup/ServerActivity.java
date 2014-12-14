package com.patrickchristensen.qup;

import java.util.ArrayList;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.patrickchristensen.qup.adapters.SongAdapter;
import com.patrickchristensen.qup.commands.Command;
import com.patrickchristensen.qup.listeners.DrawerItemListener;
import com.patrickchristensen.qup.listeners.SongVoteListener;
import com.patrickchristensen.qup.model.Guest;
import com.patrickchristensen.qup.model.SongQueue;
import com.patrickchristensen.qup.threads.ReceiverThread;
import com.patrickchristensen.qup.threads.SenderThread;
import com.patrickchristensen.qup.util.Utils;

public class ServerActivity extends QupActivity {
	
	private SongQueue 				songQueue;
	private ArrayList<Guest>		guests;
	
	private TextView 				serverStatus;
	private ListView				queueList;
	
	private Thread 					receiverThread;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		QupApplication.currentPage = QupApplication.PAGE_SERVER;
		setContentView(R.layout.activity_server);
		super.onCreate(savedInstanceState);
		initLogic();		
		initView();
		receiverThread = new Thread(new ReceiverThread(getReceiverHandler()));
		receiverThread.start();	//starts listening for connections in the background
		serverStatus.setText("Listening on: " + Utils.getIPAddress(true));
	}
	
	private void initLogic(){
		songQueue = new SongQueue();
		guests = new ArrayList<Guest>();
	}
	
	private void initView(){
		queueList = (ListView) findViewById(R.id.queue_list);
		queueList.setAdapter(getSongAdapter());
		queueList.setOnItemClickListener(new SongVoteListener());
		serverStatus = (TextView) findViewById(R.id.server_status);
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
					Gson gson = new Gson();
					String data = gson.toJson(songQueue.getSongs());
					sendCommand(new Command(Command.UPDATE_SONG_QUEUE, data, QupApplication.IPADDRESS, command.getSenderIp()));
					break;
				default:
					Toast.makeText(getApplicationContext(), "Command invalid", Toast.LENGTH_LONG).show();
					break;
				}
			}
		};
	}
}
