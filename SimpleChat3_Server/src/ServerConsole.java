import java.io.*;
import server.*;
import common.*;

public class ServerConsole implements ChatIF {
    final public static int DEFAULT_PORT = 5555;

    EchoServer server;

    public ServerConsole(int port) throws IOException {
        server = new EchoServer(port, this);
    }

    public void display(String message) {
        System.out.println(message);
    }

    public void accept() {
        try {
            BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
            String message;

            try {
                while (true) {
                    message = fromConsole.readLine();
                    server.handleMessageFromServerUI(message);
                }
            } catch (NullPointerException e) { }
        } catch (Exception ex) {
            ex.printStackTrace();
            display("ERROR!");
        }
    }

    public static void main(String[] args) {
        int port = 0;

        try {
            port = Integer.parseInt(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            port = DEFAULT_PORT;
        }

        try {
            ServerConsole sv = new ServerConsole(port);
            sv.accept();
        } catch (IOException e) {
            System.out.println("could not start listening for clients.");
        }
    }
}