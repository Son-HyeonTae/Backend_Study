package client;

import ocsf.client.*;
import common.*;
import java.io.*;

public class ChatClient extends AbstractClient {
    ChatIF clientUI;

    public ChatClient(String host, int port, ChatIF clientUI)
    throws IOException
    {
        super(host, port);
        this.clientUI = clientUI;
        openConnection();
    }

    public void handleMessageFromServer(Object msg)
    {
        clientUI.display(msg.toString());
    }
    
    public void handleMessageFromClientUI(String message)
    {
        try {
            sendToServer(message);
        } catch(IOException e) {
            clientUI.display("Could not send message to server. Terminating client.");
            quit();
        }
    }

    public void quit()
    {
        try {
            closeConnection();
        } catch(IOException e) { }
        System.exit(0);
    }
}
