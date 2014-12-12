package com.patrickchristensen.qup.listeners;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.patrickchristensen.qup.ClientActivity;
import com.patrickchristensen.qup.QupApplication;
import com.patrickchristensen.qup.ServerActivity;


public class DrawerItemListener implements ListView.OnItemClickListener{
	
	private Activity activity;
	
	public DrawerItemListener(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		switch(position){
		case 0:
			if(QupApplication.currentPage !=  0)
				activity.startActivity(new Intent(QupApplication.appContext, ServerActivity.class));
			break;
		case 1:
			if(QupApplication.currentPage != 1)
				activity.startActivity(new Intent(QupApplication.appContext, ClientActivity.class));
			break;
		}
	}

}
