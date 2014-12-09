package com.patrickchristensen.qup.listeners;

import java.util.EventObject;

public class MessageArrivedEvent extends EventObject {

	private String message;

	/**
	 * The complete message in the form COMMAND#MSG
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Constructs a new MessageArrived Instance
	 * 
	 * @param source
	 */
	public MessageArrivedEvent(Object source) {
		super(source);
	}

	/**
	 * Constructs a new MessageArrived Instance
	 * 
	 * @param source
	 * @param message
	 */
	public MessageArrivedEvent(Object source, String message) {
		super(source);
		this.message = message;
	}
}