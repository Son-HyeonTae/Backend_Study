import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import ocsf.client.*;
import client.*;
import common.*;

public class SimpleChatClientFrame extends Frame implements ChatIF
{
  public static final int DEFAULT_PORT = 5555;

  private Button quitB =     new Button("Quit");
  private Button connectB =    new Button("Connect");
  private Button logoffB =      new Button("Logoff");
  private Button sendB =      new Button("Send");
  private TextField command = new TextField("");
  private Label commandLB =   new Label("Command: ", Label.RIGHT);
  private List list =        new List();
  private ChatIFChatClient client;

  public SimpleChatClientFrame(ObservableClient oclient)
  {
    super("Simple Chat Client");
    client = new ChatIFChatClient(oclient, this);

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

    quitB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        quit();
      }
    });

    connectB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        connect();
      }
    });

    logoffB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        logoff();
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
 
    bottom.add(connectB);
    bottom.add(sendB);
    bottom.add(logoffB);
    bottom.add(quitB);

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
  
  public void connect()
  {
    client.handleMessageFromClientUI("#login");
    list.setBackground(Color.green);
  }

  public void logoff()
  {
    client.handleMessageFromClientUI("#logoff");
    list.setBackground(Color.yellow);
  }

  public void send()
  {
    client.handleMessageFromClientUI(command.getText());
  }

  public void quit()
  {
    client.handleMessageFromClientUI("#quit");
  }

  /**
   * This method is responsible for the creation of the server UI and
   * the server instance.
   *
   * @param args[0] The host to connect to.
   * @param args[1] The port number to listen on.  Defaults to 5555 
   *         if no argument is entered.
   */
  public static void main(String[] args) 
  {
    String host = null;
    int port = 0;  //Port to listn on
    
    try
    {
      host = args[0];
    }
    catch (IndexOutOfBoundsException e)
    {
      host = "localhost";
    }
    
    try
    {
      port = Integer.parseInt(args[1]);  //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT;  //Set port to 5555
    }
    
    ObservableClient oclient = new ObservableClient(host, port);
    SimpleChatClientFrame cf = new SimpleChatClientFrame(oclient);
  }
}
