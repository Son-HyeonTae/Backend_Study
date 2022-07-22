import java.awt.*;

import java.awt.event.*;

import java.io.IOException;

import ocsf.server.*;

import server.*;

import common.*;



public class SimpleChatServerFrame extends Frame implements ChatIF

{

  public static final int DEFAULT_PORT = 5555;



  private Button closeB =     new Button("Close");

  private Button listenB =    new Button("listen");

  private Button stopB =      new Button("Stop");

  private Button sendB =      new Button("Send");

  private TextField command = new TextField("");

  private Label commandLB =   new Label("Command: ", Label.RIGHT);

  private List list =        new List();

  private ChatIFEchoServer server;



  public SimpleChatServerFrame(ObservableOriginatorServer ooserver)

  {

    super("Simple Chat Server");

    server = new ChatIFEchoServer(ooserver, this);



    addWindowListener(new WindowAdapter() {

      public void windowClosing(WindowEvent e)

      {

        quit();

      }

    });



    sendB.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e)

      {

        send();

      }

    });



    closeB.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e)

      {

        close();

      }

    });



    listenB.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e)

      {

        listen();

      }

    });



    stopB.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e)

      {

        stop();

      }

    });



    Panel com = new Panel();

    com.setLayout(new BorderLayout(5,5));

    com.add("West", commandLB);

    com.add("Center", command);



    Panel comlist = new Panel();

    comlist.setLayout(new BorderLayout(5,5));

    comlist.add("Center", list);

    comlist.add("South", com);



    Panel bottom = new Panel();

    bottom.setLayout(new GridLayout(2,2,5,5));

 

    bottom.add(listenB);

    bottom.add(sendB);

    bottom.add(stopB);

    bottom.add(closeB);



    setLayout(new BorderLayout(5,5));

    add("Center", comlist);

    add("South", bottom);

    setSize(300,400);

    setVisible(true);

  }



  public void display(String msg)

  {

    list.add(msg);

    list.makeVisible(list.getItemCount()-1);

  }

  

  public void close()

  {

    server.handleMessageFromServerUI("#close");

    list.setBackground(Color.red);

  }



  public void listen()

  {

    server.handleMessageFromServerUI("#start");

    list.setBackground(Color.green);

  }



  public void stop()

  {

    server.handleMessageFromServerUI("#stop");

    list.setBackground(Color.yellow);

  }



  public void send()

  {

    server.handleMessageFromServerUI(command.getText());

  }



  public void quit()

  {

    server.handleMessageFromServerUI("#quit");

  }



  /**

   * This method is responsible for the creation of the server UI and

   * the server instance.

   *

   * @param args[0] The port number to listn on.  Defaults to 5555 

   *         if no argument is entered.

   */

  public static void main(String[] args) 

  {

    int port = 0;  //Port to listn on

    

    try

    {

      port = Integer.parseInt(args[0]);  //Get port from command line

    }

    catch(Throwable t)

    {

      port = DEFAULT_PORT;  //Set port to 5555

    }

    

    ObservableOriginatorServer ooserver = new ObservableOriginatorServer(port);

    SimpleChatServerFrame sv = new SimpleChatServerFrame(ooserver);

  }

}

