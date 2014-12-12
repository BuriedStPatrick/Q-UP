package com.patrickchristensen.qup.threads;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.patrickchristensen.qup.ClientActivity;
import com.patrickchristensen.qup.QupApplication;
import com.patrickchristensen.qup.commands.Command;

public class SenderThread implements Runnable{
	
	private String receiverIpAddress;
	private Command command;
	
	public SenderThread(Command command) {
		this.receiverIpAddress = command.getReceiverIp();
		this.command = command;
	}

	@Override
	public void run() {
		try {
            InetAddress serverAddr = InetAddress.getByName(receiverIpAddress);
            Socket socket = new Socket(serverAddr, QupApplication.serverPort);
                try {
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                                .getOutputStream())), true);
                    Looper.prepare();
                	Gson json = new Gson();
                	out.println(json.toJson(command));
                } catch (Exception e) {
                    Log.e("ClientActivity", "S: Error", e);
                }
                
            socket.close();
        } catch (Exception e) {
            Log.e("ClientActivity", "C: Error", e);
            ClientActivity.connected = false;
        }
	}

}
