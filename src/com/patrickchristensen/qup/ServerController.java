package com.patrickchristensen.qup;

import android.util.SparseArray;
import android.widget.Toast;

import com.patrickchristensen.qup.commands.Command;
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
	
	public void queueCommand(int command, String data){
		//Add command to queue
		commands.append(commands.size(), new Command(command, data));
	}
	
	private void executeCommands(){
		//1. Execute 10 commands or less
		if(commands.size() > 0){
			for(int i = 0; i < commands.size();){
				Command command = commands.get(i);
				switch(command.getCommand()){
					case QupApplication.CONNECT:
						//2. Connect user session
						break;
					case QupApplication.VOTE_SONG:
						//3. Register vote on song
						break;
					case QupApplication.DISCONNECT:
						//4. Disconnect user session
						break;
				}
			}
		}
	}
	
	private void voteSong(long userId, long songId){
//		Song song = new Song();
		songs.registerVote(songId);
	}

}
