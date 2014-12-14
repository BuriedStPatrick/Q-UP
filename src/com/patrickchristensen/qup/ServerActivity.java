package com.patrickchristensen.qup;

import java.util.ArrayList;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.patrickchristensen.qup.commands.Command;
import com.patrickchristensen.qup.listeners.SongVoteListener;
import com.patrickchristensen.qup.model.Guest;
import com.patrickchristensen.qup.model.SongQueue;
import com.patrickchristensen.qup.services.QupMusicService;
import com.patrickchristensen.qup.services.QupMusicService.MusicBinder;
import com.patrickchristensen.qup.threads.ReceiverThread;
import com.patrickchristensen.qup.util.Utils;

public class ServerActivity extends QupActivity {

	private SongQueue 					songQueue;
	private ArrayList<Guest> 			guests;
	private QupMusicService 			musicService;
	private Intent 						playIntent;
	private boolean 					musicBound = false;
	private boolean 					isPlaying = false;
	private Button						queuePlayBtn;

	private TextView 					serverStatus;
	private ListView 					queueList;

	private Thread 						receiverThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		QupApplication.currentPage = QupApplication.PAGE_SERVER;
		setContentView(R.layout.activity_server);
		super.onCreate(savedInstanceState);
		initLogic();
		initView();
		receiverThread = new Thread(new ReceiverThread(getReceiverHandler()));
		receiverThread.start(); // starts listening for connections in the
								// background
		serverStatus.setText("Listening on: " + Utils.getIPAddress(true));
	}

	private void initLogic() {
		songQueue = new SongQueue();
		guests = new ArrayList<Guest>();
	}

	private void initView() {
		queueList = (ListView) findViewById(R.id.queue_list);
		queueList.setAdapter(getSongAdapter());
		queueList.setOnItemClickListener(new SongVoteListener());
		serverStatus = (TextView) findViewById(R.id.server_status);
	}
	
	
	private class MusicConnection implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			MusicBinder binder = (MusicBinder) service;
			musicService = binder.getService();
			musicService.setList(songQueue.getSongs());
			songQueue.addObserver(musicService);
			musicBound = true;
			queuePlayBtn = (Button) findViewById(R.id.queue_play);
		}
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			musicBound = false;
		}
		
	}

	public void playSong(View v) {
		if (!isPlaying) {
			musicService.playSong();
			isPlaying = true;
		} else {
			musicService.pauseSong();
			isPlaying = false;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		receiverThread.interrupt();
	}

	@Override
	protected void onStart() {
		super.onStart();
		Toast.makeText(getApplicationContext(), "I'm here", Toast.LENGTH_SHORT).show();
		Intent playIntent = new Intent(this, MusicBinder.class);
		bindService(playIntent, new MusicConnection(), ServerActivity.this.BIND_AUTO_CREATE);
		startService(playIntent);
	}

	private Handler getReceiverHandler() {
		return new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Gson json = new Gson();
				Command command = json.fromJson(
						msg.getData().getString("data"), Command.class);

				switch (command.getAction()) {
				case Command.CONNECT:
					Toast.makeText(getApplicationContext(),
							"Connected IP: " + command.getSenderIp(),
							Toast.LENGTH_LONG).show();
					break;
				case Command.DISCONNECT:
					Toast.makeText(getApplicationContext(), "Disconnect",
							Toast.LENGTH_LONG).show();
					break;
				case Command.VOTE_SONG:
					Toast.makeText(getApplicationContext(),
							"Vote song: " + command.getData(),
							Toast.LENGTH_LONG).show();
					break;
				case Command.FETCH_SONGS:
					Gson gson = new Gson();
					String data = gson.toJson(songQueue.getSongs());
					sendCommand(new Command(Command.UPDATE_SONG_QUEUE, data,
							QupApplication.IPADDRESS, command.getSenderIp()));
					break;
				default:
					Toast.makeText(getApplicationContext(), "Command invalid",
							Toast.LENGTH_LONG).show();
					break;
				}
			}
		};
	}

	@Override
	protected void onDestroy() {
		stopService(playIntent);
		musicService = null;
		super.onDestroy();
	}
}
