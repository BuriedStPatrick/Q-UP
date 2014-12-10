package com.patrickchristensen.qup.commands;


public class Command{
	
	public static final int CONNECT = 0;
	public static final int VOTE_SONG = 1;
	public static final int DISCONNECT = 2;
	public static final int FETCH_SONGS = 3;
	public static final int UPDATE_SONG_QUEUE = 4;
	
	private int		action;
	private String	data;
	
	public Command(int action) {
		this.action = action;
	}
	
	public Command(int action, String data){
		this.action = action;
		this.data = data;
	}
	
	public int getAction() {
		return action;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data){
		this.data = data;
	}

}
