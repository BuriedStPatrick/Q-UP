package com.patrickchristensen.qup.listeners;

import com.patrickchristensen.qup.model.Song;

public interface PlaybackStateListener {

	public void onPlaybackCompleted(Song song);
	
}
