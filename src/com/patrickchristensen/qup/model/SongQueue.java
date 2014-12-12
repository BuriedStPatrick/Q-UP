package com.patrickchristensen.qup.model;

import java.util.ArrayList;
import java.util.Observable;

import android.util.SparseArray;

import com.patrickchristensen.qup.QupApplication;
import com.patrickchristensen.qup.commands.Command;

public class SongQueue extends Observable{
	
	private ArrayList<Song> songs;
	
	public SongQueue() {
		songs = getSongsFromDisk();
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
	
	public void updateSongs(ArrayList<Song> songs){
		this.songs = songs;
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
