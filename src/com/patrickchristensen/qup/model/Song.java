package com.patrickchristensen.qup.model;

import android.graphics.Bitmap;

public class Song {
	
	private long songId;
	private String title;
	private String artist;
	private String album;
	private Bitmap image;
	
	public Song(long songId, String title, String artist, String album, Bitmap image) {
		this.songId = songId;
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.image = image;
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

}
