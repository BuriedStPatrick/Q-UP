package com.patrickchristensen.qup;

import java.util.ArrayList;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.patrickchristensen.qup.commands.Command;
import com.patrickchristensen.qup.controllers.MusicController;
import com.patrickchristensen.qup.listeners.PlaybackStateListener;
import com.patrickchristensen.qup.model.Guest;
import com.patrickchristensen.qup.model.Song;
import com.patrickchristensen.qup.model.SongQueue;
import com.patrickchristensen.qup.services.MusicService;
import com.patrickchristensen.qup.services.MusicService.MusicBinder;
import com.patrickchristensen.qup.threads.ReceiverThread;
import com.patrickchristensen.qup.util.Utils;

public class ServerActivity extends QupActivity implements MediaPlayerControl, PlaybackStateListener{

	private boolean 					isMusicBound = false;
	private boolean						paused = false;
	private boolean						playbackPaused = false;
	
	private SongQueue 					songQueue;
	private ArrayList<Guest> 			guests;
	private Button						queuePlayBtn;

	private TextView 					serverStatus;
	private ListView 					queueList;

	private MusicService				musicService;
	private Thread 						receiverThread;
	private Intent 						playIntent;
	private MusicController				musicController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		QupApplication.currentPage = QupApplication.PAGE_SERVER;
		setContentView(R.layout.activity_server);
		super.onCreate(savedInstanceState);
		initLogic();
		initView();
		setMusicController();
		receiverThread = new Thread(new ReceiverThread(getReceiverHandler()));
		receiverThread.start(); // starts listening for connections in the
								// background
		serverStatus.setText("Connect on: " + Utils.getIPAddress(true));
	}
	
	private ServiceConnection musicConnection =  new ServiceConnection(){
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			MusicBinder binder = (MusicBinder) service;
			musicService = binder.getService();
			musicService.setList(songQueue.getSongs());
			songQueue.addObserver(musicService);
			musicService.setCallbackListener(ServerActivity.this);
			isMusicBound = true;
			queuePlayBtn = (Button) findViewById(R.id.queue_play);
		}
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			isMusicBound = false;
		}
	};

	private void initLogic() {
		songQueue = new SongQueue(this);
		guests = new ArrayList<Guest>();
	}
	
	private void initView() {
		queueList = (ListView) findViewById(R.id.queue_list);
		queueList.setAdapter(getSongAdapter());
		serverStatus = (TextView) findViewById(R.id.server_status);
	}
	
	private void setMusicController(){
		musicController = new MusicController(this);
		musicController.setMediaPlayer(this);
		musicController.setAnchorView(findViewById(R.id.queue_list));
		musicController.setEnabled(true);
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		paused = true;
		receiverThread.interrupt();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(paused){
			setMusicController();
			paused = false;
		}
		receiverThread = new Thread(new ReceiverThread(getReceiverHandler()));
		receiverThread.start();
	}

	public void playSong(View v) {
		start();
	}

	@Override
	protected void onStop() {
		receiverThread.interrupt();
		musicController.hide();
		super.onStop();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if(playIntent == null){
			Toast.makeText(getApplicationContext(), "I'm here", Toast.LENGTH_SHORT).show();
			playIntent = new Intent(this, MusicService.class);
			getApplicationContext().bindService(playIntent, musicConnection, BIND_AUTO_CREATE);
			startService(playIntent);
		}
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
					guests.add(new Guest(command.getSenderIp()));
					break;
				case Command.DISCONNECT:
					Toast.makeText(getApplicationContext(), "Disconnected : " + command.getSenderIp(),
							Toast.LENGTH_LONG).show();
					for(int i = 0; i < guests.size() ; i++){
						if(guests.get(i).getIpAddress().equals(command.getSenderIp())){
							guests.remove(i);
							break;
						}
					}
					break;
				case Command.VOTE_SONG:
					Toast.makeText(getApplicationContext(),
							"Vote song: " + command.getData(),
							Toast.LENGTH_LONG).show();
					songQueue.registerVote(Long.parseLong(command.getData()));
					sendCommand(new Command(Command.UPDATE_SONG_QUEUE, QupApplication.IPADDRESS, command.getSenderIp()));
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

	@Override
	public void start() {
		if(musicService != null)
			musicService.go();
	}

	@Override
	public void pause() {
		if(musicService != null){
			playbackPaused = true;
			musicService.pausePlayer();
		}
	}

	@Override
	public int getDuration() {
		if(musicService != null)
			return musicService.getDur();
		return 0;
	}

	@Override
	public int getCurrentPosition() {
		if(musicService != null && isMusicBound && musicService.isPlaying())
			return musicService.getPos();
		return 0;
	}

	@Override
	public void seekTo(int pos) {
		if(musicService != null)
			musicService.seek(pos);
	}

	@Override
	public boolean isPlaying() {
		if(musicService != null && isMusicBound)
			return musicService.isPlaying();
		return false;
	}

	@Override
	public int getBufferPercentage() {
		return 0;
	}

	@Override
	public boolean canPause() {
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		return true;
	}

	@Override
	public boolean canSeekForward() {
		return true;
	}

	@Override
	public int getAudioSessionId() {
		return 0;
	}

	@Override
	public void onPlaybackCompleted(Song song) {
		Toast.makeText(getApplicationContext(), "Song is completed", Toast.LENGTH_SHORT).show();
		songQueue.resetVotes(song);
	}
}
