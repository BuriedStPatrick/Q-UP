package com.patrickchristensen.qup;

import java.util.Queue;

import android.util.SparseArray;
import android.widget.Toast;

import com.patrickchristensen.qup.commands.Command;
import com.patrickchristensen.qup.model.Song;
import com.patrickchristensen.qup.model.SongQueue;

public class ServerController {
	
	private SongQueue songs;
	private SparseArray<Command> commands;
	
	private static ServerController instance;
	
	private ServerController(){
		songs = new SongQueue();
	}
	
	public static ServerController getInstance(){
		if(instance == null)
			instance = new ServerController();
		return instance;
	}
	
	public void queueCommand(long userId, int command, String data){
		//Add command to queue
		commands.append(commands.size(), new Command(userId, command, data));
	}
	
	private void executeCommands(){
		//1. Execute 10 commands or less
		if(commands.size() > 0){
			for(int i = 0; i < commands.size();){
				Command command = commands.get(i);
				switch(command.getCommand()){
				case QupApplication.CONNECT:
					//2. Connect user session
					Toast.makeText(QupApplication.appContext, "Connected: " + command.getUserId(), Toast.LENGTH_LONG).show();
					break;
				case QupApplication.VOTE_SONG:
					//3. Register vote on song
					Toast.makeText(QupApplication.appContext, "Vote song\nUser ID:" + command.getUserId() + "Song ID: " + command.getData(), Toast.LENGTH_LONG).show();
					voteSong(command.getUserId(), Long.parseLong(command.getData()));
					break;
				case QupApplication.DISCONNECT:
					//4. Disconnect user session
					Toast.makeText(QupApplication.appContext, "Disconnected: " + command.getUserId(), Toast.LENGTH_LONG).show();
					break;
				}
			}
		}
	}
	
	private void voteSong(long userId, long songId){
		Song song = new Song();
	}

}
