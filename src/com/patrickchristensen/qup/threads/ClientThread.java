package com.patrickchristensen.qup.threads;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Queue;

import android.util.Log;
import android.widget.Toast;

import com.patrickchristensen.qup.ClientActivity;
import com.patrickchristensen.qup.QupApplication;
import com.patrickchristensen.qup.interfaces.CommandInterface;

public class ClientThread implements Runnable{
	
	private String serverIpAddress;
	private Queue<CommandInterface> commands;
	
	public ClientThread(String serverIpAddress) {
		this.serverIpAddress = serverIpAddress;
	}

	@Override
	public void run() {
		try {
            InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
            Log.d("ClientActivity", "C: Connecting...");
            Socket socket = new Socket(serverAddr, QupApplication.serverPort);
            ClientActivity.connected = true;
            while (ClientActivity.connected) {
                try {
                    Log.d("ClientActivity", "C: Sending command.");
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                                .getOutputStream())), true);
                        // WHERE YOU ISSUE THE COMMANDS
                        out.println("dostuff");
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
