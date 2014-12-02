package com.patrickchristensen.qup.activities;

import com.patrickchristensen.qup.R;
import com.patrickchristensen.qup.R.id;
import com.patrickchristensen.qup.R.layout;
import com.patrickchristensen.qup.threads.ClientThread;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ClientActivity extends Activity{
	
	private EditText 		serverIp;
	private String 			serverIpAddress = "";
	private Handler			handler;
	private Button			connectBtn;
	
	public static boolean	connected = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		serverIp = (EditText) findViewById(R.id.server_ip);
		connectBtn = (Button) findViewById(R.id.connect_btn);
		setContentView(R.layout.activity_client);
	}
	
	private OnClickListener connectListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(!connected){
				serverIpAddress = serverIp.getText().toString();
				if(!serverIpAddress.isEmpty()){
					Thread clientThread = new Thread(new ClientThread(serverIpAddress));
					clientThread.start();
				}
			}
		}
	};

}
