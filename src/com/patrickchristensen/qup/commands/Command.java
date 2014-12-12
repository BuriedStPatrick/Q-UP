package com.patrickchristensen.qup.commands;


public class Command{
	
	public static final int CONNECT = 0;
	public static final int VOTE_SONG = 1;
	public static final int DISCONNECT = 2;
	public static final int FETCH_SONGS = 3;
	public static final int UPDATE_SONG_QUEUE = 4;
	
	private int		action;
	private String senderIp;
	private String receiverIp;
	private Object	data;
	
	/**
	 * 
	 * @param action What do you want your receiver to do?
	 * @param senderIp Who are you sending as?
	 * @param receiverIp Who are you sending to?
	 */
	public Command(int action, String senderIp, String receiverIp) {
		this(action, null, senderIp, receiverIp);
	}
	
	/**
	 * 
	 * @param action What do you want your receiver to do?
	 * @param data The data you want to send to your receiver
	 * @param senderIp Who are you sending as?
	 * @param receiverIp Who are you sending to?
	 */
	public Command(int action, Object data, String senderIp, String receiverIp){
		this.action = action;
		this.data = data;
		this.senderIp = senderIp;
		this.receiverIp = receiverIp;
	}
	
	public int getAction() {
		return action;
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(String data){
		this.data = data;
	}
	
	public String getReceiverIp() {
		return receiverIp;
	}
	
	public String getSenderIp() {
		return senderIp;
	}
	
	@Override
	public String toString() {
		return "Sender: " + senderIp + ", Receiver: " + receiverIp + ", Action: " + action + ", Data: " + data;
	}

}
