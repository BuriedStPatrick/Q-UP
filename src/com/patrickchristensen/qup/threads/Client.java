package com.patrickchristensen.qup.threads;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.patrickchristensen.interfaces.ChatClient;
import com.patrickchristensen.interfaces.MessageArrivedListener;
import com.patrickchristensen.qup.listeners.MessageArrivedEvent;
import com.patrickchristensen.qup.util.EventListenerList;

public class Client extends Thread implements ChatClient {

	Socket socket;
	PrintWriter output;
	Scanner input;
	String username;
	protected EventListenerList listenerList = new EventListenerList();

	@Override
	public void connect(String serverAddress, int port, String userName)
			throws UnknownHostException, IOException {
		socket = new Socket(serverAddress, port);
		output = new PrintWriter(socket.getOutputStream(), true);
		input = new Scanner(socket.getInputStream());
		username = userName;
		output.println("CONNECT#" + username);
		start();
	}

	@Override
	public void sendMessage(String receiver, String msg) {
		output.println("SEND#" + receiver + "#" + msg);
	}

	@Override
	public void disconnect() {
		output.println("CLOSE#");
	}

	@Override
	public void addMessageArivedEventListener(MessageArrivedListener listener) {
		listenerList.add(MessageArrivedListener.class, listener);
	}

	@Override
	public void removeMessageArivedEventListener(MessageArrivedListener listener) {
		listenerList.remove(MessageArrivedListener.class, listener);
	}

	void fireMessageArrivedEventEvent(MessageArrivedEvent evt) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i = i + 2) {
			if (listeners[i] == MessageArrivedListener.class) {
				((MessageArrivedListener) listeners[i + 1]).MessageArrived(evt);
			}
		}
	}

	@Override
	public void run() {
		boolean keepRunning = true;
		while (keepRunning) {
			String msg = input.nextLine();
			if (msg.startsWith("CLOSE#")) {
				try {
					socket.close();
					keepRunning = false;
				} catch (IOException ex) {
					Logger.getLogger(Client.class.getName()).log(Level.SEVERE,
							null, ex);
				}
			} else {
				fireMessageArrivedEventEvent(new MessageArrivedEvent(this, msg));
				System.out.println(msg);
			}
		}

	}

	// private class DummyScanner extends Thread {
	//
	// public DummyScanner() {
	// }
	//
	// @Override
	// public void run() {
	// while (true) {
	// String msg = DUMMYINPUT.nextLine();
	// sendMessage("*", msg);
	// }
	// }
	// }
}