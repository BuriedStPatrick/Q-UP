package com.patrickchristensen.qup.threads;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.patrickchristensen.qup.ClientActivity;
import com.patrickchristensen.qup.QupApplication;
import com.patrickchristensen.qup.commands.Command;

public class ClientThread implements Runnable{
	
	private String serverIpAddress;
	private Handler handler;
	private Command command;
	
	public ClientThread(String serverIpAddress, Handler handler, Command command) {
		this.serverIpAddress = serverIpAddress;
		this.handler = handler;
		this.command = command;
	}

	@Override
	public void run() {
		try {
            InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
            Socket socket = new Socket(serverAddr, QupApplication.serverPort);
            
                try {
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                                .getOutputStream())), true);
                    // WHERE YOU ISSUE THE COMMANDS
                	Gson json = new Gson();
                	out.println(json.toJson(command));
                	
                } catch (Exception e) {
                    Log.e("ClientActivity", "S: Error", e);
                }
            socket.close();
            Log.d("ClientActivity", "C: Closed.");
        } catch (Exception e) {
            Log.e("ClientActivity", "C: Error", e);
            ClientActivity.connected = false;
        }
	}

}
