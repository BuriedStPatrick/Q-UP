package com.patrickchristensen.qup.model;

import android.util.SparseArray;

import com.patrickchristensen.qup.QupApplication;

public class SongQueue {
	
	private SparseArray<Song> songs;
	
	public SongQueue() {
		songs = getSongsFromDisk();
	}
	
	/**
	 * Loads songs from the disk.
	 * Loads dummy data if QupApplication.DEBUG is true
	 * @return A list of songs from the disk
	 */
	private SparseArray<Song> getSongsFromDisk(){
		SparseArray<Song> _songs = new SparseArray<Song>();
		
		if(QupApplication.DEBUG){
			//load dummy data
			_songs.put(0, new Song(0, "A Tribute to the Fallen", "Killswitch Engage", "Disarm the Descent", null));
			_songs.put(1, new Song(1, "Addicted to Pain", "Alter Bridge", "Fortress", null));
			_songs.put(2, new Song(2, "Over the Mountain", "Ozzy Osbourne", "Diary of a Madman", null));
		}else{
			//TODO: load data from disk
		}
		return _songs;
	}
	
	public void registerVote(long songId){
		
	}

}
