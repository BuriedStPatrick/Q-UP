package com.patrickchristensen.qup.adapters;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.patrickchristensen.qup.QupApplication;
import com.patrickchristensen.qup.R;
import com.patrickchristensen.qup.model.Song;
import com.patrickchristensen.qup.model.SongQueue;

public class SongAdapter extends ArrayAdapter<Song> implements Observer{
	
	public SongAdapter(Context context, SongQueue queue) {
        super(context, R.id.queue_list, queue.getSongs());
        SongQueue.getInstance().addObserver(this);
        notifyDataSetChanged();
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		SongQueue _queue = SongQueue.getInstance();
		if(row == null){
			LayoutInflater inflater
						= (LayoutInflater) QupApplication.appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.queue_list_row, parent, false);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.title = (TextView) row.findViewById(R.id.song_title);
			viewHolder.artist = (TextView) row.findViewById(R.id.song_artist);
			viewHolder.queueBtn = (ImageView) row.findViewById(R.id.song_votebtn);
			row.setTag(viewHolder);
		}
		
		//fill data
		ViewHolder holder = (ViewHolder) row.getTag();
		Song song = (Song) getItem(position);
		holder.title.setText(song.getTitle());
		holder.artist.setText(song.getArtist());
		if(song.isVoted()){
			holder.queueBtn.setImageResource(R.drawable.queue_btn);
		}
		
		return row;
	}
	
	@Override
	public void update(Observable observable, Object data) {
		if(observable instanceof SongQueue){
			notifyDataSetChanged();
		}
	}
	
	private static class ViewHolder{
		public TextView title;
		public TextView artist;
		public ImageView queueBtn;
	}

}
