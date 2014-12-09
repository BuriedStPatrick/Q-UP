package com.patrickchristensen.qup.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.patrickchristensen.qup.QupApplication;
import com.patrickchristensen.qup.ServerController;
import com.patrickchristensen.qup.commands.Command;
import com.patrickchristensen.qup.util.Utils;

public class ServerThread implements Runnable{
	
	
	public static String serverIp = "";
	
	private Handler				handler;
	private ServerSocket		serverSocket;
	private ArrayList<Socket> 	clients;
	private ServerController	serverController;
	
	public ServerThread(Handler handler){
		this.handler = handler;
		clients = new ArrayList<Socket>();
		serverIp = Utils.getIPAddress(true);
		serverController = ServerController.getInstance();
	}

	@Override
	public void run() {
		try {
            if (serverIp != null) {
                serverSocket = new ServerSocket(QupApplication.serverPort);
                Log.d("customtag", "created serversocket with port: " + QupApplication.serverPort);
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
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) { return inetAddress.getHostAddress().toString(); }
                }
            }
        } catch (SocketException ex) {
            Log.e("ServerActivity", ex.toString());
        }
        return null;
    }
	
	public void stop(){
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
