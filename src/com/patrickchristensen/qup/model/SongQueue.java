package com.patrickchristensen.qup.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Observable;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.patrickchristensen.qup.QupApplication;
import com.patrickchristensen.qup.util.QupDBAdapter;

public class SongQueue extends Observable {
	
	private ArrayList<Song> 		songs;
	private ContentResolver			musicResolver;
	private Uri 					musicUri;
	private Cursor 					musicCursor;
	
	private QupDBAdapter			dbAdapter;
	
	public SongQueue(Context context, boolean loadLocal) {
		musicResolver = context.getContentResolver();
		dbAdapter = new QupDBAdapter(context);
		musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		if(loadLocal)
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
			_songs.add(new Song(3, "Eyeless", "Slipknot", "Slipknot", null, 11));
			_songs.add(new Song(4, "Dancers To A Discordant System", "Meshuggah", "ObZen", null, 26));
		}else{
			//TODO: load data from disk
			musicCursor = musicResolver.query(musicUri, null, null, null, null);
			if(musicCursor != null && musicCursor.moveToFirst()){
				int titleColumn
					= musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
				int idColumn
					= musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
				int artistColumn
					= musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);
				int albumColumn
					= musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ALBUM);
				do{
					long _id = musicCursor.getLong(idColumn);
					String _title = musicCursor.getString(titleColumn);
					String _artist = musicCursor.getString(artistColumn);
					String _album = musicCursor.getString(albumColumn);
					_songs.add(new Song(_id, _title, _artist, _album, null));
				}while(musicCursor.moveToNext());
			}
			//TODO: load data from sqlite db
			for(Song _song : _songs){
				dbAdapter.insertSong(_song.getSongId(), _song.getVotes());
			}
		}
		return _songs;
	}
	
	public void updateSongs(ArrayList<Song> songs){
		this.songs = songs;
		setChanged();
		notifyObservers();
	}
	
	public synchronized void registerVote(long songId){
		for(Song song : songs){
			if(song.getSongId() == songId){
				song.addVote();
				dbAdapter.updateSong(songId, song.getVotes());
				break;
			}
		}
		updateSongs(songs); //yeah, it's stupid but whatever I'm tired
	}
	
	public synchronized void resetVotes(Song song){
		for(Song _song : songs){
			if(_song.getSongId() == song.getSongId()){
				_song.resetVotes();
				dbAdapter.updateSong(_song.getSongId(), _song.getVotes());
				break;
			}
		}
		updateSongs(songs); //yeah, it's stupid but whatever I'm tired
	}
	
	public Song getSongById(long id){
		for(Song _song : songs){
			int i = 0;
			if(_song.getSongId() == id)
				return _song;
		}
		return null;
	}
	
	public long getMostPopular(){
		return dbAdapter.getMostPopular();
	}
	
	public Song get(int index){
		return songs.get(index);
	}
	
	public String printDB(){
		return dbAdapter.printDB();
	}
	
	public synchronized ArrayList<Song> getSongs(){
		return songs;
	}

}
