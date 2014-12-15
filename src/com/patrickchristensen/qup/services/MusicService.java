package com.patrickchristensen.qup.services;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.patrickchristensen.qup.QupApplication;
import com.patrickchristensen.qup.model.Song;
import com.patrickchristensen.qup.model.SongQueue;

public class MusicService extends Service implements OnPreparedListener,
		OnErrorListener, OnCompletionListener, Observer {

	private final IBinder musicBinder = new MusicBinder();
	private MediaPlayer player;
	private ArrayList<Song> songs;
	private final static int SONG_POS = 0;

	@Override
	public void onCreate() {
		super.onCreate();
		player = new MediaPlayer();
		initMusicPlayer();
	}

	private void initMusicPlayer() {
		// set player properties
		player.setWakeMode(QupApplication.appContext,
				PowerManager.PARTIAL_WAKE_LOCK);
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);

		player.setOnPreparedListener(this);
		player.setOnCompletionListener(this);
		player.setOnErrorListener(this);
	}
	
	public void playSong() {
		player.reset();
		Song playSong = songs.get(SONG_POS);
		long currSong = playSong.getSongId();
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
	
	public void pauseSong(){
		player.pause();
	}

	private void updateList(ArrayList<Song> songs) {
		this.songs = songs;
	}

	public void setList(ArrayList<Song> songs) {
		this.songs = songs;
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
	 	
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.start();
	}

	@Override
	public void update(Observable observable, Object data) {
		if (observable instanceof SongQueue) {
			updateList(((SongQueue) observable).getSongs());
		}
	}

}
