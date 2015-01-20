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
	
	
	public static String serverIp = "";
	
	private Handler				handler;
	private ServerSocket		receiverSocket;
	
	public ReceiverThread(Handler handler){
		this.handler = handler;
		serverIp = Utils.getIPAddress(true);
	}

	@Override
	public void run() {
		try {
            if (serverIp != null) {
                receiverSocket = new ServerSocket(QupApplication.serverPort);
                while (true) {
                    // LISTEN FOR INCOMING CLIENTS
                	Socket sender = receiverSocket.accept(); // Blocking call
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
			try {
                BufferedReader in = new BufferedReader(new InputStreamReader(sender.getInputStream()));
                String line = null;
                while ((line = in.readLine()) != null) {
                	Message msg = Message.obtain();
                	Bundle bundle = new Bundle();
                	bundle.putString("data", line);
                	msg.setData(bundle);
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
		try {
			receiverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
