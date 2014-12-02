package com.patrickchristensen.qup.threads;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import com.patrickchristensen.qup.activities.ClientActivity;
import com.patrickchristensen.qup.activities.ServerActivity;

import android.util.Log;

public class ClientThread implements Runnable{
	
	private String serverIpAddress;
	
	public ClientThread(String serverIpAddress) {
		this.serverIpAddress = serverIpAddress;
	}

	@Override
	public void run() {
		try {
            InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
            Log.d("ClientActivity", "C: Connecting...");
            Socket socket = new Socket(serverAddr, ServerActivity.SERVERPORT);
            ClientActivity.connected = true;
            while (ClientActivity.connected) {
                try {
                    Log.d("ClientActivity", "C: Sending command.");
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                                .getOutputStream())), true);
                        // WHERE YOU ISSUE THE COMMANDS
                        out.println("Hey Server!");
                        Log.d("ClientActivity", "C: Sent.");
                } catch (Exception e) {
                    Log.e("ClientActivity", "S: Error", e);
                }
            }
            socket.close();
            Log.d("ClientActivity", "C: Closed.");
        } catch (Exception e) {
            Log.e("ClientActivity", "C: Error", e);
            ClientActivity.connected = false;
        }
	}

}
