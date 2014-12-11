package com.patrickchristensen.qup.adapters;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.patrickchristensen.qup.QupApplication;
import com.patrickchristensen.qup.R;
import com.patrickchristensen.qup.commands.Command;
import com.patrickchristensen.qup.model.Song;
import com.patrickchristensen.qup.model.SongQueue;

public class SongAdapter extends ArrayAdapter<Song> implements Observer{
	
	private SongQueue songs;
	
	public SongAdapter(Context context, SongQueue songs) {
        super(context, R.layout.queue_list_row);
        this.songs = songs;
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater
			= (LayoutInflater) QupApplication.appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.queue_list_row, parent, false);
		
		TextView title = (TextView) row.findViewById(R.id.song_title);
		TextView artist = (TextView) row.findViewById(R.id.song_artist);
		
		title.setText(songs.getSongs().get(position).getTitle());
		artist.setText(songs.getSongs().get(position).getArtist());
		
		return row;
	}
	
	@Override
	public void update(Observable observable, Object data) {
		switch(((Command) data).getAction()){
		case Command.FETCH_SONGS:
			notifyDataSetChanged();
			break;
		case Command.UPDATE_SONG_QUEUE:
			//TODO: update list order
			break;
		}
	}

}
