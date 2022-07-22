package client;

import ocsf.client.*;
import java.io.*;
import java.util.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
 
public class ChatClient extends Observable implements Observer
{
  // Instance variables *********************************************
  
  /**
   * The client being observed.
   */
  private ObservableClient client;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.  Login removed in 
   * phase 3 as the server will prompt for it.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   */
  
  public ChatClient(ObservableClient oclient) 
  {
    client = oclient;
    client.addObserver(this);
    
    // Change in phase 5, do not attempt the connection since if there
    // is an error establishing it, the GUI will not show up.
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server. Added
   * in Phase 4.
   *
   * @param obs The observable.
   * @param msg The message from the server.
   */
  public void update(Observable obs, Object msg)
  {
    if (!(msg instanceof String))
      return;
    
    String message = (String)msg;
    
    // The first 3 if statements deal with messages sent by 
    // ObservableClient when notable events occur. 
    if (message.startsWith(ObservableClient.CONNECTION_CLOSED))
    {
      notifyObservers("Connection to server closed.");
      notifyObservers("#disconnected");
    }  
    else if (message.startsWith(ObservableClient.CONNECTION_ESTABLISHED))
    {
      notifyObservers("Connection to server established.");
      notifyObservers("#connected");
    }  
    else if (message.startsWith(ObservableClient.CONNECTION_EXCEPTION))
    {
      notifyObservers("Connection to server lost.");
      notifyObservers("#disconnected");
    }  
    else
      notifyObservers(msg);
  }

  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      client.closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  public void handleMessageFromClientUI(String message)
  {
    //If the command is #login. Added in phase 2
    if (message.startsWith("#login"))
    {
      try
      {
        client.openConnection();
      }
      catch(IOException e)
      {
        notifyObservers("Cannot establish connection."
                + " Awaiting command.");
      }
      return;
    }
    
    //If the command is #quit.  Added in phase 2
    if (message.startsWith("#quit"))
      quit();
        
    //If the command is #logoff.  Added in phase 2
    if (message.startsWith("#logoff"))
    {
      try
      {
        client.closeConnection();
      }
      catch(IOException e)
      {
        notifyObservers
           ("Cannot logoff normally.  Terminating client.");
        quit();
      }
      return;
    }
        
    //If the command is #gethost.  Added in phase 2
    if (message.startsWith("#gethost"))
    {
      notifyObservers("Current host: " + client.getHost());
      return;
    }
         
    //If the command is #getport.  Added in phase 2
    if (message.startsWith("#getport"))
    {
      notifyObservers("Current port: " + client.getPort());
      return;
    }
          
    //If the command is #sethost.  Added in phase 2
    if (message.startsWith("#sethost"))
    {
      if (client.isConnected())
        notifyObservers("Cannot change host while connected.");
      else
        try
        {
          client.setHost(message.substring(9));
          notifyObservers("Host set to: " + client.getHost());
        }
        catch(IndexOutOfBoundsException e)
        {
          notifyObservers("Invalid use of the #sethost command.");
        }
      return;
    }
    
    //If the command is #setport.  Added in phase 2
    if (message.startsWith("#setport"))
    {
      if (client.isConnected())
        notifyObservers("Cannot change port while connected.");
      else
        try
        {
          int port = 0;
          port = Integer.parseInt(message.substring(9));
        
          //If the port number is invalid
          if ((port < 1024) || (port > 65535))
          {
            notifyObservers("Invalid port number.  Port unchanged.");
          }
          else
          {
            client.setPort(port);
            notifyObservers("Port set to " + port);
          }
        }
        catch(Exception e)
        {
          notifyObservers("Invalid use of the #setport command.");
          notifyObservers("Port unchanged.");
        }
      return;
    }
    
    //If a help command
    if (message.startsWith("#help") || message.startsWith("#?"))
    {
      notifyObservers("\nClient-side command list:"
      + "\n#block <loginID> -- Block messages from the specified"
         + " client."
      + "\n#channel <channel> -- Connects to the specified channel."
      + "\n#fwd <loginID> -- Forward all messages to the specified"
         + " client."
      + "\n#getchannel -- Gets the channel the client is currently"
         + " connected to."
      + "\n#gethost -- Gets the host to which the client will" 
         + " connect/is connected."
      + "\n#getport -- Gets the port on which the client will"
         + " connect/is connected."
      + "\n#help OR #? -- Lists all commands and their use."
      + "\n#login -- Connects to a server."
      + "\n#logoff -- Disconnects from a server."
      + "\n#nochannel -- Returns the client to the main channel."
      + "\n#private <loginID> <msg> -- Sends a private message to"
         + " the specified client."
      + "\n#pub -- Sends a public message."
      + "\n#quit -- Terminates the client and disconnects from"
         + " server."
      + "\n#sethost <newhost> -- Specify the host to connect to."
      + "\n#setport <newport> -- Specify on which to connect."
      + "\n#unblock -- Unblock messages from all blocked clients."
      + "\n#unblock <loginID> -- Unblock messages from a specific"
         + " client."
      + "\n#unfwd -- Stop forwarding messages."
      + "\n#whoblocksme -- List all the users who are blocking"
         + " messages from you."
      + "\n#whoiblock -- List all users you are blocking messages"
         + " from."
      + "\n#whoison -- Gets a list of all users and channel they are"
         + " connected to.");
      return;
    }
    
    //If not a client-side command or is message to be displayed
    if ((!(message.startsWith("#")))
       || message.startsWith("#whoison")        //Added phase 3
       || message.startsWith("#private")        //Added phase 3
       || message.startsWith("#channel")        //Added phase 3
       || message.startsWith("#pub")            //Added phase 3
       || message.startsWith("#nochannel")      //Added phase 3
       || message.startsWith("#getchannel")     //Added phase 3
       || message.startsWith("#fwd")            //Added phase 3
       || message.startsWith("#unfwd")          //Added phase 3
       || message.startsWith("#block")          //Added phase 3
       || message.startsWith("#unblock")        //Added phase 3
       || message.startsWith("#whoiblock")      //Added phase 3
       || message.startsWith("#whoblocksme"))   //Added phase 3
      try
      {
        client.sendToServer(message);
      }
      catch(IOException e)
      {
        notifyObservers("Cannot send the message to the server."
                  + " Disconnecting.");
        try
        {
          client.closeConnection();
        }
        catch(IOException ex)
        {
          notifyObservers
             ("Cannot logoff normally.  Terminating client.");
          quit();
        }
      }
    else
      notifyObservers("Invalid command.");
  }
  
  /**
   * This method overrides the method inherited from Observable as the 
   * setChanged() method is protected and cannot be accessed from the
   * GUI adapter classes.
   *
   * @param arg The argument sent to the observers.
   */
  public void notifyObservers(Object arg)
  {
    setChanged();
    super.notifyObservers(arg);
  }
}
//End of ChatClient class
