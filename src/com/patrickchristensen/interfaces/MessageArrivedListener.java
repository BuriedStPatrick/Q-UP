package com.patrickchristensen.interfaces;

import java.util.EventListener;

import com.patrickchristensen.qup.listeners.MessageArrivedEvent;

public interface MessageArrivedListener extends EventListener {
	  
	 /**
	  * A message arrived on the input socket stream
	  * @param event 
	  */
	  public void MessageArrived(MessageArrivedEvent event);
	  
	}