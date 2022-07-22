import java.util.*;
import java.io.*;
import server.*;
import common.*;
import ocsf.server.ObservableOriginatorServer;

public class ServerConsole implements ChatIF {
  final public static int DEFAULT_PORT = 5555;

  EchoServer server;

  public ServerConsole(ObservableOriginatorServer ooserver) throws IOException {
    server = new EchoServer(ooserver, this);
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
      } catch (NullPointerException e) {}
    } catch (Exception ex) {
      ex.printStackTrace();
      display("ERROR!");
    }
  }

  public static void main(String[] args) {
    int port = 0;  //Port to listen on

    try {
      port = Integer.parseInt(args[0]);  //Get port from command line
    } catch(ArrayIndexOutOfBoundsException e) {
      port = DEFAULT_PORT;  //Set port to 5555
    }

    try {
      ObservableOriginatorServer ooserver= new ObservableOriginatorServer(port);
      ServerConsole sv = new ServerConsole(ooserver);
      sv.accept();
    } catch(IOException e) {
      System.out.println("Could not start listening for clients.");
    }
  }
}