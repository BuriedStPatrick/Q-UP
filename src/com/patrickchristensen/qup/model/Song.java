package com.patrickchristensen.qup.model;

import java.util.Observable;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

public class Song {
	
	private long songId;
	private String title;
	private String artist;
	private String album;
	private Bitmap image;
	private boolean isVoted;
	private int votes;
	
	public Song(long songId, String title, String artist, String album, Bitmap image) {
		this.songId = songId;
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.image = image;
	}
	
	public Song(JSONObject jObject) throws JSONException{
		this.songId = jObject.getLong("songId");
		this.title = jObject.getString("title");
		this.artist = jObject.getString("artist");
		this.album = jObject.getString("album");
		this.isVoted = jObject.getBoolean("isVoted");
		this.votes = jObject.getInt("votes");
	}
	
	public Song(long songId, String title, String artist, String album, Bitmap image, int votes) {
		this(songId, title, artist, album, image);
		this.votes = votes;
	}
	
	public long getSongId() {
		return songId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public String getAlbum() {
		return album;
	}
	
	public Bitmap getImage() {
		return image;
	}
	
	public int getVotes(){
		return votes;
	}
	
	public void addVote(){
		votes++;
	}
	
	public void resetVotes(){
		this.votes = 0;
	}
	
	public boolean isVoted(){
		return isVoted;
	}
	
	public int compareTo(Song other) {
		return other.getVotes() - this.votes;
	}
	
}
