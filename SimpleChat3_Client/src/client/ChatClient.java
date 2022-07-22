package client;

import ocsf.client.*;
import common.*;
import java.io.*;

public class ChatClient extends AbstractClient {

    ChatIF clientUI;

    public ChatClient(String host, int port, ChatIF clientUI) {
        super(host, port);
        this.clientUI = clientUI;

        try {
            openConnection();
        } catch (IOException e) {
            handleMessageFromClientUI("#logoff");
            clientUI.display("Cannot open connection. Awaiting command.");
        }
    }

    public void handleMessageFromServer(Object msg) {
        clientUI.display(msg.toString());
    }

    public void handleMessageFromClientUI(String message) {
        if (message.startsWith("#login")) {
            try {
                openConnection();
            } catch (IOException e) {
                clientUI.display("Cannot establish connection. Awaiting command.");
            }
            return;
        }

        if (message.startsWith("#quit"))
            quit();

        if (message.startsWith("#logoff")) {
            try {
                closeConnection();
            } catch (IOException e) {
                clientUI.display("Cannot logoff normally. Terminating client.");
                quit();
            }
            return;
        }

        if (message.startsWith("#gethost")) {
            clientUI.display("Current host: " + getHost());
            return;
        }

        if (message.startsWith("#getport")) {
            clientUI.display("Current port: " + getPort());
            return;
        }

        if (message.startsWith("#sethost")) {
            if (isConnected())
                clientUI.display("Cannot change host while connected.");
            else {
                try {
                    setHost(message.substring(9));
                    clientUI.display("Host set to: " + getHost());
                } catch (IndexOutOfBoundsException e) {
                    clientUI.display("Invalid host. Use #sethost <host>.");
                }
            }
            return;
        }

        if (message.startsWith("#setport")) {
            if (isConnected())
                clientUI.display("Cannot change port while connected.");
            else {
                try {
                    int port = 0;
                    port = Integer.parseInt(message.substring(9));

                    if ((port < 1024) || (port > 65535)) {
                        clientUI.display("Invalid port number. Port unchanged.");
                    } else {
                        setPort(port);
                        clientUI.display("Port set to " + port);
                    }
                } catch (Exception e) {
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
            + "\n#getport -- Gets the port on which the client will connect/is connected"
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
            + "\n#whoison == Gets a list of all users and the channel they are connected to.");
            return;
        }

        if ((!(message.startsWith("#")))
            || message.startsWith("#whoison")
            || message.startsWith("#private")
            || message.startsWith("#channel")
            || message.startsWith("#pub")
            || message.startsWith("#nochannel")
            || message.startsWith("#getchannel")
            || message.startsWith("#fwd")
            || message.startsWith("#unfwd")
            || message.startsWith("#block")
            || message.startsWith("#unblock")
            || message.startsWith("#whoiblock")
            || message.startsWith("#whoblocksme")) {

            try {
                sendToServer(message);
            } catch (IOException e) {
                clientUI.display("Cannot send the message to the server. Disconnecting.");

                try {
                    closeConnection();
                } catch (IOException ex) {
                    clientUI.display("Cannot logoff normally. Terminating client.");
                    quit();
                }
            }
        } else {
            clientUI.display("Invalid command.");
        }
    }

    public void quit() {
        try {
            closeConnection();
        } catch (IOException e) { }
        System.exit(0);
    }

    protected void connectionClosed(boolean isAbnormal) {
        if (isAbnormal)
            clientUI.display("Abnormal termination of connection.");
        else
            clientUI.display("Connection closed.");
    }

    protected void connectionEstablished() {
        clientUI.display("Connection established with " + getHost() + " on port " + getPort());
    }

    protected void connectionException(Exception exception) {
        clientUI.display("Connection to server terminated.");
    }
}