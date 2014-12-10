package com.patrickchristensen.qup.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.patrickchristensen.qup.QupApplication;
import com.patrickchristensen.qup.util.Utils;

public class ServerThread implements Runnable{
	
	
	public static String serverIp = "";
	
	private Handler				handler;
	private ServerSocket		serverSocket;
	private ArrayList<Socket> 	clients;
	
	public ServerThread(Handler handler){
		this.handler = handler;
		clients = new ArrayList<Socket>();
		serverIp = Utils.getIPAddress(true);
	}

	@Override
	public void run() {
		try {
            if (serverIp != null) {
                serverSocket = new ServerSocket(QupApplication.serverPort);
                Log.d("customtag", "created serversocket with port: " + QupApplication.serverPort + ", IP: " + Utils.getIPAddress(true));
                while (true) {
                    // LISTEN FOR INCOMING CLIENTS
                	Socket client = serverSocket.accept(); // Blocking call
                    clients.add(client);
                	
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(clients.get(clients.size()-1).getInputStream()));
                        String line = null;
                        while ((line = in.readLine()) != null) {
                        	Message msg = Message.obtain();
                        	Bundle bundle = new Bundle();
                        	bundle.putString("data", line);
                        	msg.setData(bundle);
                        	handler.sendMessage(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void stop(){
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
