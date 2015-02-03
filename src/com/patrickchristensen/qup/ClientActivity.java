package com.patrickchristensen.qup;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.patrickchristensen.qup.commands.Command;
import com.patrickchristensen.qup.model.Song;
import com.patrickchristensen.qup.threads.ReceiverThread;

public class ClientActivity extends QupActivity {
	
	private ListView				queueList;
	
	private EditText 				serverIp;
	private String 					serverIpAddress;
	private Button					connectBtn;
	private Button					getSongsBtn;
	private Thread					receiverThread;
	
	public static boolean			connected = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		QupApplication.currentPage = QupApplication.PAGE_CLIENT;
		setContentView(R.layout.activity_client);
		super.onCreate(savedInstanceState);
		if(savedInstanceState != null){
			serverIpAddress = savedInstanceState.getString("serverIpAddress");
		}
		initLogic();
		initView();
		initDrawer();
		receiverThread = new Thread(new ReceiverThread(getReceiverHandler()));
		receiverThread.start();
	}
	
	@Override
	protected void initView(){
		super.initView();
		queueList = (ListView) findViewById(R.id.queue_list);
		serverIp = (EditText) findViewById(R.id.server_ip);
		connectBtn = (Button) findViewById(R.id.connect_btn);
		getSongsBtn = (Button) findViewById(R.id.getsongs_btn);
		
		//TODO: Move out to separate method
		connectBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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
	
	@Override
	protected void initLogic(){
		setSongQueue(false);
		super.initLogic();
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
					ArrayList<Song> _songs = new ArrayList<Song>();
					try {
						JSONArray jArray = new JSONArray(command.getData());
						for(int i = 0; i < jArray.length(); i++){
							JSONObject jObject = jArray.getJSONObject(i);
							_songs.add(new Song(jObject));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					updateSongQueue(_songs);
					break;
				}
			}
		};
	}
	
	private class SongVoteListener implements ListView.OnItemClickListener{
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//TODO: Vote on song
			Toast.makeText(QupApplication.appContext, "Voted on: " + id, Toast.LENGTH_SHORT).show();
			sendCommand(new Command(Command.VOTE_SONG, id + "", QupApplication.IPADDRESS, serverIpAddress ));
		}

	}
	
	@Override
    protected void onStop() {
        super.onStop();
        receiverThread.interrupt();
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		receiverThread = new Thread(new ReceiverThread(getReceiverHandler()));
		receiverThread.start();
		getSongAdapter().notifyDataSetChanged();
	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if(serverIpAddress != null)
			outState.putString("serverIpAddress", serverIpAddress);
		super.onSaveInstanceState(outState);
	}

}
