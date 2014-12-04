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

import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.patrickchristensen.qup.QupApplication;
import com.patrickchristensen.qup.util.Utils;

public class ServerThread implements Runnable{
	
	
	public static String serverIp = "";
	private String command = "";
	
	private Handler				handler;
	private ServerSocket		serverSocket;
	private TextView			serverStatus;
	private ArrayList<Socket> 	clients;
	
	public ServerThread(TextView serverStatus){
		handler = new Handler();
		this.serverStatus = serverStatus;
		clients = new ArrayList<Socket>();
		serverIp = Utils.getIPAddress(true);
	}

	@Override
	public void run() {
		try {
            if (serverIp != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        serverStatus.setText("Listening on IP: " + serverIp);
                    }
                });
                serverSocket = new ServerSocket(QupApplication.serverPort);
                Log.d("customtag", "created serversocket with port: " + QupApplication.serverPort);
                while (true) {
                    // LISTEN FOR INCOMING CLIENTS
                    clients.add(serverSocket.accept());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                        	Toast.makeText(QupApplication.appContext, clients.get(clients.size()-1).getInetAddress() + " connected.", Toast.LENGTH_LONG).show();
                        }
                    });

                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(clients.get(clients.size()-1).getInputStream()));
                        String line = null;
                        while ((line = in.readLine()) != null) {
                            serverStatus.setText(line);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // DO WHATEVER YOU WANT TO THE FRONT END
                                    // THIS IS WHERE YOU CAN BE CREATIVE
                                	
                                }
                            });
                        }
                        break;
                    } catch (Exception e) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                serverStatus.setText("Oops. Connection interrupted. Please reconnect your phones.");
                            }
                        });
                        e.printStackTrace();
                    }
                }
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        serverStatus.setText("Couldn't detect internet connection.");
                    }
                });
            }
        } catch (Exception e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    serverStatus.setText("Error");
                }
            });
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
