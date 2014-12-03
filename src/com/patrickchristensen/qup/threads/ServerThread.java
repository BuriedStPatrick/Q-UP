package com.patrickchristensen.qup.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import com.patrickchristensen.qup.QupApplication;
import com.patrickchristensen.qup.ServerActivity;
import com.patrickchristensen.qup.util.Utils;

import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

public class ServerThread implements Runnable{
	
	
	public static String serverIp = "";
	
	private Handler			handler;
	private ServerSocket	serverSocket;
	private TextView		serverStatus;
	
	public ServerThread(TextView serverStatus){
		handler = new Handler();
		this.serverStatus = serverStatus;
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
                    Socket client = serverSocket.accept();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                        	Log.d("customtag", "S: Connected");
                            serverStatus.setText("Connected.");
                        }
                    });

                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        String line = null;
                        while ((line = in.readLine()) != null) {
                            Log.d("ServerActivity", line);
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
