package com.patrickchristensen.qup;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.patrickchristensen.qup.commands.Command;
import com.patrickchristensen.qup.listeners.DrawerItemListener;
import com.patrickchristensen.qup.threads.ClientThread;

public class ClientActivity extends Activity{
	
	private ActionBarDrawerToggle 	drawerListener;
	private ListView				drawerList;
	private DrawerLayout			drawerLayout;
	private Toolbar					toolbar;
	private TextView 				serverStatus;

	private ArrayAdapter<String> 	pages;
	
	private EditText 		serverIp;
	private String 			serverIpAddress = "";
	private Button			connectBtn;
	private Thread			clientThread;
	
	public static boolean	connected = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		QupApplication.appContext = getApplicationContext();
		setContentView(R.layout.activity_client);
		initViews();
		initDrawer();
	}
	
	private void initViews(){
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		serverStatus = (TextView) findViewById(R.id.server_status);
		drawerList = (ListView) findViewById(R.id.drawer_list);
		
		serverIp = (EditText) findViewById(R.id.server_ip);
		connectBtn = (Button) findViewById(R.id.connect_btn);
		connectBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				serverIpAddress = serverIp.getText().toString();
				if(!connected){
					serverIpAddress = serverIp.getText().toString();
					if(!serverIpAddress.isEmpty()){
						Toast.makeText(getApplicationContext(), "Started thread with ip: " + serverIpAddress + ":" + QupApplication.serverPort, Toast.LENGTH_SHORT).show();
						clientThread = new Thread(new ClientThread(serverIpAddress, getClientHandler()));
						clientThread.start();
					}
				}
			}
		});
	}
	
	private void initDrawer(){
		pages = new ArrayAdapter<String>(this, R.id.drawer_list);
		drawerListener = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer){
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				invalidateOptionsMenu();
				drawerList.bringToFront();
			}
			
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				invalidateOptionsMenu();
			}
			
		};
		drawerLayout.setDrawerListener(drawerListener);
		drawerList.setOnItemClickListener(new DrawerItemListener(this));
	}
	
	private Handler getClientHandler(){
		return new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Gson json = new Gson();
				Command command =
						json.fromJson(msg.getData().getString("data"), Command.class);
				
				switch(command.getCommand()){
				case QupApplication.FETCH_SONGS:
					Toast.makeText(getApplicationContext(), "Fetching songs", Toast.LENGTH_LONG).show();
					break;
				}
			}
		};
	}

}
