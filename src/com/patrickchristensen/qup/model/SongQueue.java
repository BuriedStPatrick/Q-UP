package com.patrickchristensen.qup.model;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.patrickchristensen.qup.QupApplication;
import com.patrickchristensen.qup.commands.Command;

public class SongQueue extends Observable{
	
	private ArrayList<Song> songs;
	private static SongQueue instance;
	
	private SongQueue() {
		songs = getSongsFromDisk();
	}
	
	public static SongQueue getInstance(){
		if(instance == null)
			instance = new SongQueue();
		return instance;
	}
	
	/**
	 * Loads songs from the disk.
	 * Loads dummy data if QupApplication.DEBUG is true
	 * @return A list of songs from the disk
	 */
	private ArrayList<Song> getSongsFromDisk(){
		ArrayList<Song> _songs = new ArrayList<Song>();
		
		if(QupApplication.DEBUG){
			//load dummy data
			_songs.add(new Song(0, "A Tribute to the Fallen", "Killswitch Engage", "Disarm the Descent", null, 57));
			_songs.add(new Song(1, "Addicted to Pain", "Alter Bridge", "Fortress", null, 31));
			_songs.add(new Song(2, "Over the Mountain", "Ozzy Osbourne", "Diary of a Madman", null, 42));
		}else{
			//TODO: load data from disk
			//TODO: load data from sqlite db
		}
		return _songs;
	}
	
	@Override
	public void addObserver(Observer observer) {
		Toast.makeText(QupApplication.appContext, "adding observer: " + observer.toString(), Toast.LENGTH_SHORT).show();
		super.addObserver(observer);
	}
	
	public void updateSongs(ArrayList<Song> songs){
		this.songs = songs;
		Log.d("customtag", "in update songs");
		Log.d("customtag", "songs is now: " + songs);
		setChanged();
		notifyObservers();
	}
	
	public ArrayList<Song> getSongs(){
		return songs;
	}
	
	public void registerVote(long songId){
		//TODO: notify that dataset has changed
		for(Song song : songs){
			
		}
	}

}
