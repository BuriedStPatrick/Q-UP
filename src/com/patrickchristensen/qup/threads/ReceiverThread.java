package com.patrickchristensen.qup.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.patrickchristensen.qup.QupApplication;
import com.patrickchristensen.qup.util.Utils;

public class ReceiverThread implements Runnable{
	
	
	public static String localIp = "";
	
	private Handler				handler;
	private ServerSocket		receiverSocket;
	
	/**
	 * The constructor of this runnable
	 * @param handler The handler that communicates back to the Activity
	 */
	public ReceiverThread(Handler handler){
		this.handler = handler;
		localIp = Utils.getIPAddress(true); //get and set the local IP address
	}

	@Override
	public void run() {
		try {
            if (localIp != null) {
                receiverSocket = new ServerSocket(QupApplication.serverPort);
                //Infinite loop, since we want to keep listening for as long as the application runs
                while (true) {
                    //Listen for incoming messages
                	Socket sender = receiverSocket.accept(); // Blocking call, will "pause" the code until a connection occurs
                	//When a connection is found, start an action accordingly in a separate thread
                	new Thread(new ActionThread(sender)).start();
                }     
            }
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private class ActionThread implements Runnable{
		
		private Socket sender;
		
		public ActionThread(Socket sender){
			this.sender = sender;
		}
		
		@Override
		public void run() {
			//To catch any exceptions (things that go wrong), we have to tell it to try and catch all errors, so the app doesn't crash
			try {
				//Get the stream of data (the data JSON string) from the sender
                BufferedReader in = new BufferedReader(new InputStreamReader(sender.getInputStream()));
                String line = null;
                //Go over each line and execute a command accordingly. 1 line = 1 command
                while ((line = in.readLine()) != null) {
                	//Obtain message from pool. This means that we only allocate memory for a new Message object if we can't already use an existing
                	//Messages are used to communicate data with Handlers
                	Message msg = Message.obtain();
                	//Bundle is required for putting data in a Message
                	Bundle bundle = new Bundle();
                	//We put the JSON string in the Bundle with a key "data"
                	bundle.putString("data", line);
                	//We set the Message's data to the instance of the Bundle
                	msg.setData(bundle);
                	//We send the message with the data to the Handler that then executes on the Activity
                	handler.sendMessage(msg);
                }
            }catch(IOException ioe){
            	ioe.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
		}
	}
	
	public void stop(){
		//If the application stops, so we need to stop the thread as well
		try {
			//Closes our socket
			receiverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
