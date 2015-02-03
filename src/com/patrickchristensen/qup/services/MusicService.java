package com.patrickchristensen.qup.services;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.patrickchristensen.qup.QupActivity;
import com.patrickchristensen.qup.R;
import com.patrickchristensen.qup.ServerActivity;
import com.patrickchristensen.qup.listeners.PlaybackStateListener;
import com.patrickchristensen.qup.model.Song;

public class MusicService extends Service implements OnPreparedListener,
		OnErrorListener, OnCompletionListener, OnAudioFocusChangeListener {

	private final IBinder 						musicBinder = new MusicBinder();
	private static final int 					NOTIFY_ID = 1;
	private final static int 					SONG_POS = 0;
	private PlaybackStateListener				callbackListener;
	private Song currentSong;
	
	private MediaPlayer 						player;

	@Override
	public void onCreate() {
		super.onCreate();
		player = new MediaPlayer();
		AudioManager manager = (AudioManager) getSystemService(AUDIO_SERVICE);
		manager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
		initMusicPlayer();
	}

	private void initMusicPlayer() {
		// set player properties
		player.setWakeMode(getApplicationContext(),
				PowerManager.PARTIAL_WAKE_LOCK);
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);

		player.setOnPreparedListener(this);
		player.setOnCompletionListener(this);
		player.setOnErrorListener(this);
	}
	
	private void playSong() {
		player.reset();
		long currSong = QupActivity.songQueue.getMostPopular();
		currentSong = QupActivity.songQueue.getSongById(currSong);
		
		Uri trackUri = ContentUris.withAppendedId(
				android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				currSong);
		try {
			player.setDataSource(getApplicationContext(), trackUri);
		} catch (Exception e) {
			Log.e("MUSIC SERVICE", "Error setting data source", e);
		}
		player.prepareAsync();
	}

	public class MusicBinder extends Binder {
		
		public MusicService getService() {
			return MusicService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return musicBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		player.stop();
		player.release();
		return false;
	}
	
	@Override
	public void onCompletion(MediaPlayer mp) {
		callbackListener.onPlaybackCompleted(currentSong); //tell to update the database
	 	player.reset();
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		player.reset();
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		player.start();
		Intent notificationIntent = new Intent(this, ServerActivity.class);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendInt
			= PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		Notification.Builder builder = new Notification.Builder(this);
		builder.setContentIntent(pendInt)
			.setSmallIcon(R.drawable.ic_launcher_v2)
			.setTicker(currentSong.getTitle())
			.setOngoing(true)
			.setContentTitle("Playing")
			.setContentText(currentSong.getTitle());
		
		Notification notification = builder.build();
		startForeground(NOTIFY_ID, notification);
	}
	
	public int getDur(){
		return player.getDuration();
	}
	
	public boolean isPlaying(){
		return player.isPlaying();
	}
	
	public int getPos(){
		return SONG_POS;
	}
	
	public void pausePlayer(){
		player.pause();
	}
	
	public void seek(int pos){
		player.seekTo(pos);
	}
	
	public void go(){
		playSong();
	}
	
	@Override
	public void onDestroy() {
		stopForeground(true);
		if(player != null)
			player.release();
		super.onDestroy();
	}
	
	public void setCallbackListener(PlaybackStateListener callbackListener){
		this.callbackListener = callbackListener;
	}

	@Override
	public void onAudioFocusChange(int focusChange) {
		switch(focusChange){
		case AudioManager.AUDIOFOCUS_GAIN:
			if(player == null) initMusicPlayer();
			else if(!player.isPlaying()) player.start();
			player.setVolume(1.0f, 1.0f);
			break;
		case AudioManager.AUDIOFOCUS_LOSS:
			// Lost focus for an unbounded amount of time: stop playback and release media player
			if(player.isPlaying()) player.stop();
			player.release();
			player = null;
			break;
		case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
			// Lost focus for a short time, but we have to stop
            // playback. We don't release the media player because playback
            // is likely to resume
			if(player.isPlaying()) player.pause();
			break;
		case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
			// Lost focus for a short time, but it's ok to keep playing
            // at an attenuated level
			if(player.isPlaying()) player.setVolume(0.1f, 0.1f);
			break;
		}
	}

}
