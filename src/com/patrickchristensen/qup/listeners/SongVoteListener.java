package com.patrickchristensen.qup.listeners;

import com.patrickchristensen.qup.QupApplication;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class SongVoteListener implements ListView.OnItemClickListener{
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//TODO: Vote on song
		Toast.makeText(QupApplication.appContext, "Voted on: " + position, Toast.LENGTH_SHORT).show();
	}

}

	