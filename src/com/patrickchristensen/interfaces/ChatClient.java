package com.patrickchristensen.interfaces;

import java.io.IOException;
import java.net.UnknownHostException;

public interface ChatClient
{
  /**
   * Connect to the server and sends the initila CONNECT command
   *
   * @param serverAddress
   * @param port
   * @param userName
   * @throws UnknownHostException
   * @throws IOException
   */
  public void connect(String serverAddress, int port, String userName) throws UnknownHostException, IOException;

  /**
   * Sends a message to the server
   * @param receiver (either a single receiver, or a number of receivers separated by commas or a * (send to all)
   * @param msg
   */
  public void sendMessage(String receiver, String msg);

  /**
   * Tell server to close the connection
   */
  public void disconnect();

  /**
   * Register for MessageArrivedEvents
   * @param listener 
   */
  public void addMessageArivedEventListener(MessageArrivedListener listener);

  /**
   * Un-register for MessageArrivedEvents
   * @param listener 
   */
  public void removeMessageArivedEventListener(MessageArrivedListener listener);
  
  
  /*
   * TODO: In order to notify clients (GUI's, test main's etc.) about ONLINE or CLOSE events you need
   * to either:
   *  1) Add corresponding events (OnlineMessageArrived etc.) which would give you training in 
   *     designing your own event handlers.
   *  2) Change the MessageArrivedEvent so that the same event can carry different content
   * 
   */
 
  
}