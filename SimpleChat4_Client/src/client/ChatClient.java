package client;

import ocsf.client.*;
import common.*;
import java.io.*;
import java.util.*;

public class ChatClient implements Observer {

  ChatIF clientUI;

  private ObservableClient client;

  public ChatClient(ObservableClient client, ChatIF clientUI) {
    this.client = client;
    this.clientUI = clientUI;
    client.addObserver(this);

    try {
      // Change in phase 3: Server prompts for login
      client.openConnection();
    } catch(IOException e) {
      handleMessageFromClientUI("#logoff");
      clientUI.display("Cannot open connection.  Awaiting command.");
    }
  }

  public void update(Observable obs, Object msg) {
    if (!(msg instanceof String))
      return;
    
    String message = (String)msg;
    
    // The first 3 if statements deal with messages sent by 
    // ObservableClient when notable events occur. 
    if (message.startsWith(ObservableClient.CONNECTION_CLOSED))
      clientUI.display("Connection to server closed.");
    else if (message.startsWith(ObservableClient.CONNECTION_ESTABLISHED))
      clientUI.display("Connection to server established.");
    else if (message.startsWith(ObservableClient.CONNECTION_EXCEPTION))
      clientUI.display("Connection to server lost.");
    else
      clientUI.display(message);
  }

  public void handleMessageFromClientUI(String message) {
    if (message.startsWith("#login")) {
      try {
        client.openConnection();
      } catch(IOException e) {
        clientUI.display("Cannot establish connection. Awaiting command.");
      }
      return;
    }

    //If the command is #quit.  Added in phase 2
    if (message.startsWith("#quit"))
      quit();

    //If the command is #logoff.  Added in phase 2
    if (message.startsWith("#logoff")) {
      try {
        client.closeConnection();
      } catch(IOException e) {
        clientUI.display("Cannot logoff normally.  Terminating client.");
        quit();
      }
      return;
    }

    //If the command is #gethost.  Added in phase 2
    if (message.startsWith("#gethost")) {
      clientUI.display("Current host: " + client.getHost());
      return;
    }

    //If the command is #getport.  Added in phase 2
    if (message.startsWith("#getport")) {
      clientUI.display("Current port: " + client.getPort());
      return;
    }

    //If the command is #sethost.  Added in phase 2
    if (message.startsWith("#sethost")) {
      if (client.isConnected())
        clientUI.display("Cannot change host while connected.");
      else {
        try {
          client.setHost(message.substring(9));
          clientUI.display("Host set to: " + client.getHost());
        } catch(IndexOutOfBoundsException e) {
          clientUI.display("Invalid host. Use #sethost <host>.");
        }
      }
      return;
    }

    if (message.startsWith("#setport")) {
      if (client.isConnected())
        clientUI.display("Cannot change port while connected.");
      else {
        try {
          int port = 0;
          port = Integer.parseInt(message.substring(9));

          //If the port number is invalid
          if ((port < 1024) || (port > 65535)) {
            clientUI.display("Invalid port number.  Port unchanged.");
          } else {
            client.setPort(port);
            clientUI.display("Port set to " + port);
          }
        } catch(Exception e) {
          clientUI.display("Invalid port. Use #setport <port>.");
          clientUI.display("Port unchanged.");
        }
      }
      return;
    }

    if (message.startsWith("#help") || message.startsWith("#?")) {
      clientUI.display("\nClient-side command list:"
      + "\n#block <loginID> -- Block messages from the specified client."
      + "\n#channel <channel> -- Connects to the specified channel."
      + "\n#fwd <loginID> -- Forward all messages to the specified client."
      + "\n#getchannel -- Gets the channel the client is currently connected to."
      + "\n#gethost -- Gets the host to which the client will connect/is connected."
      + "\n#getport -- Gets the port on which the client will connect/is connected."
      + "\n#help OR #? -- Lists all commands and their use."
      + "\n#login -- Connects to a server."
      + "\n#logoff -- Disconnects from a server."
      + "\n#nochannel -- Returns the client to the main channel."
      + "\n#private <loginID> <msg> -- Sends a private message to the specified client."
      + "\n#pub -- Sends a public message."
      + "\n#quit -- Terminates the client and disconnects from server."
      + "\n#sethost <newhost> -- Specify the host to connect to."
      + "\n#setport <newport> -- Specify the port on which to connect."
      + "\n#unblock -- Unblock messages from all blocked clients."
      + "\n#unblock <loginID> -- Unblock messages from a specific client."
      + "\n#unfwd -- Stop forwarding messages."
      + "\n#whoblocksme -- List all the users who are blocking messages from you."
      + "\n#whoiblock -- List all users you are blocking messages from."
      + "\n#whoison -- Gets a list of all users and the channel they are connected to.");
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
      try {
        client.sendToServer(message);
      } catch(IOException e) {
        clientUI.display("Cannot send the message to the server. Disconnecting.");
        try {
          client.closeConnection();
        } catch(IOException ex) {
          clientUI.display("Cannot logoff normally.  Terminating client.");
          quit();
        }
      }
    else
      clientUI.display("Invalid command.");
  }

  public void quit() {
    try {
      client.closeConnection();
    } catch(IOException e) {}
    System.exit(0);
  }
}