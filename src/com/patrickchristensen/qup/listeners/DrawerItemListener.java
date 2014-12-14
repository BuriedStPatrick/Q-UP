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
		case QupApplication.PAGE_SERVER:
			if(QupApplication.currentPage !=  QupApplication.PAGE_SERVER)
				activity.startActivity(new Intent(QupApplication.appContext, ServerActivity.class));
			break;
		case QupApplication.PAGE_CLIENT:
			if(QupApplication.currentPage != QupApplication.PAGE_CLIENT)
				activity.startActivity(new Intent(QupApplication.appContext, ClientActivity.class));
			break;
		}
	}

}
