package com.patrickchristensen.qup;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.patrickchristensen.qup.commands.Command;
import com.patrickchristensen.qup.interfaces.CommandFactory;
import com.patrickchristensen.qup.threads.ClientThread;

public class ClientActivity extends Activity implements CommandFactory{
	
	private EditText 		serverIp;
	private String 			serverIpAddress = "";
	private String			userIpAddress = "";
	private Handler			handler;
	private Button			connectBtn;
	
	public static boolean	connected = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		QupApplication.appContext = getApplicationContext();
		setContentView(R.layout.activity_client);
		initViews();
	}
	
	private void initViews(){
		serverIp = (EditText) findViewById(R.id.server_ip);
		connectBtn = (Button) findViewById(R.id.connect_btn);
		connectBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				serverIpAddress = serverIp.getText().toString();
				Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
				if(!connected){
					Toast.makeText(getApplicationContext(), "not connected yet, doing it", Toast.LENGTH_SHORT).show();
					serverIpAddress = serverIp.getText().toString();
					if(!serverIpAddress.isEmpty()){
						Toast.makeText(getApplicationContext(), "Creating thread", Toast.LENGTH_SHORT).show();
						Thread clientThread = new Thread(new ClientThread(serverIpAddress));
						clientThread.start();
						Toast.makeText(getApplicationContext(), "Started thread with ip: " + serverIpAddress, Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	@Override
	public synchronized void executeCommand(Command command) {
	}

}
