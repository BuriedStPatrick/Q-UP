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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.patrickchristensen.qup.adapters.SongAdapter;
import com.patrickchristensen.qup.commands.Command;
import com.patrickchristensen.qup.listeners.DrawerItemListener;
import com.patrickchristensen.qup.listeners.SongVoteListener;
import com.patrickchristensen.qup.model.Song;
import com.patrickchristensen.qup.model.SongQueue;
import com.patrickchristensen.qup.threads.ReceiverThread;
import com.patrickchristensen.qup.threads.SenderThread;

public class ClientActivity extends QupActivity {
	
	private ListView				queueList;
	
	private EditText 				serverIp;
	private String 					serverIpAddress = "";
	private Button					connectBtn;
	private Button					getSongsBtn;
	private Thread					receiverThread;
	
	public static boolean			connected = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client);
		initViews();
		QupApplication.currentPage = QupApplication.PAGE_CLIENT;
		receiverThread = new Thread(new ReceiverThread(getReceiverHandler()));
		receiverThread.start();
	}
	
	private void initViews(){
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
						sendCommand(new Command(Command.CONNECT, QupApplication.IPADDRESS, serverIpAddress));
					}
				}
			}
		});
		
		getSongsBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(!serverIpAddress.isEmpty()){
					sendCommand(new Command(Command.FETCH_SONGS, QupApplication.IPADDRESS, serverIpAddress));
				}else{
					Toast.makeText(getApplicationContext(), "Please connect first", Toast.LENGTH_SHORT).show();
				}
			}
		});
		queueList.setAdapter(getSongAdapter());
		queueList.setOnItemClickListener(new SongVoteListener());
	}
	
	private Handler getReceiverHandler(){
		return new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Gson json = new Gson();
				String data = msg.getData().getString("data");
				Command command =
						json.fromJson(data, Command.class);
				
				switch(command.getAction()){
				
				case Command.UPDATE_SONG_QUEUE:
					ArrayList<Song> _queue = (ArrayList<Song>) json.fromJson(command.getData(), ArrayList.class);
					updateSongQueue(_queue);
					break;
				}
			}
		};
	}
	
	@Override
    protected void onStop() {
        super.onStop();
        receiverThread.interrupt();
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		getSongAdapter().notifyDataSetChanged();
	}

}
