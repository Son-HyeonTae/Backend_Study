import java.io.*;
import client.*;
import common.*;

public class ClientConsole implements ChatIF {
    final public static int DEFAULT_PORT = 5555;

    ChatClient client;

    public ClientConsole(String host, int port, String login) {
        client = new ChatClient(host, port, login, this);
    }

    public void accept() {
        try {
            BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));

            String message;

            try {
                while (true) {
                    message = fromConsole.readLine();
                    client.handleMessageFromClientUI(message);
                }
            } catch (NullPointerException e) { }
        } catch (Exception ex) {
            System.out.println("Unexcepted error while reading from console!");
        }
    }

    public void display(String message) {
        System.out.println(message);
    }

    public static void main(String[] args) {
        String host = "";
        int port = 0;
        String loginID = null;

        try {
            loginID = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ERROR - No login ID specified. Connection aborted.");
            System.exit(1);
        }

        try {
            host = args[1];
        } catch(ArrayIndexOutOfBoundsException e) {
            host = "localhost";
        }

        try {
            port = Integer.parseInt(args[2]);
        } catch (Throwable t) {
            port = DEFAULT_PORT;
        }

        ClientConsole chat = new ClientConsole(host, port, loginID);
        chat.accept();
    }
}