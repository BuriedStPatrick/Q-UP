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

//TODO: Turn into AsyncTask
public class SenderThread implements Runnable {

	private String receiverIpAddress;
	private Command command;

	/**
	 * The constructor of this runnable
	 * 
	 * @param command
	 *            The command we want to execute
	 */
	public SenderThread(Command command) {
		// Get the receiverIp from the command
		this.receiverIpAddress = command.getReceiverIp();
		this.command = command;
	}

	@Override
	public void run() {
		// Try this code, catch any errors so the app doesn't crash
		try {
			// Instantiate a InetAddress object required for socket connections
			InetAddress serverAddr = InetAddress.getByName(receiverIpAddress);
			Socket socket = new Socket(serverAddr, QupApplication.serverPort);
			try {
				// Write an output stream to the socket (our JSON data)
				PrintWriter out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);
				Looper.prepare();
				// Use the GSON library to write our command to a JSON string
				Gson json = new Gson();
				// Write to the socket's output stream
				out.println(json.toJson(command));
			} catch (Exception e) {
				Log.e("ClientActivity", "S: Error", e);
			}
			// Close the socket once the action is complete
			socket.close();
		} catch (Exception e) {
			Log.e("ClientActivity", "C: Error", e);
			ClientActivity.connected = false;
		}
	}

}
