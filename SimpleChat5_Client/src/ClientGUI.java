import java.awt.*;  //Added phase 5
import java.awt.event.*;  //Added phase 5
import java.io.*;
import client.*;
import java.util.*;  //Added in phase 4
import clientUIUtilities.*; //Added phase 5
import commonUIUtilities.window.*;  //Added phase 5
import ocsf.client.ObservableClient;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Changed in phase 4 to implement the Observer interface.
 * Warning: Some of the code here is cloned in ServerGUI
 * Note that in previous phases, this class was called ClientConsole
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */

public class ClientGUI extends Frame implements Observer
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  /**
  * ASCII value of the "*" (used as an echo character). It's defined as final
  * because the value will never change.
  * Added in Phase 5
  */
  static final int STAR = 42;
    

  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;

  /**
  * The objects that will be used in the frame
  * Added in Phase 5
  */
  TextArea clientText = new TextArea("");
  TextField clientInput = new TextField();

  StringList listOfClients = new StringList();

  Button btnLogin = new Button("Login");
  Button btnLogoff = new Button("Logoff");
  Button btnQuit = new Button("Quit");

  MenuBar menuBar = new MenuBar();

  Menu menuFile = new Menu("File");
  Menu menuEdit = new Menu("Edit");
  Menu menuView = new Menu("View");

  MenuItem menuFileExit = new MenuItem("Exit");
  MenuItem menuFileLogin = new MenuItem("Login");
  MenuItem menuFileLogoff = new MenuItem("Logoff");
  MenuItem menuEditBlock = new MenuItem("Block");
  MenuItem menuEditUnblock = new MenuItem("Unblock");
  MenuItem menuEditClear = new MenuItem("Clear");
  MenuItem menuEditFwd = new MenuItem("Forward");
  MenuItem menuEditUnFwd = new MenuItem("UnForward");
  MenuItem menuViewPort = new MenuItem("Port Number");
  MenuItem menuViewHost = new MenuItem("Host Name");
  MenuItem menuViewIBlock = new MenuItem("Blocked Users");
  MenuItem menuViewWhoBlocks = new MenuItem("Blocking Users");
  

  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.  Login removed
   * in phase 3 as the server will prompt for it.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientGUI(ObservableClient oclient) 
  {
    client= new ChatClient(oclient);
    client.addObserver((Observer)this);

    // Add the menu item keylisteners
    menuFileExit.addActionListener(new ClientMenuExitAdapter(client));
    menuFileLogoff.addActionListener(new ClientMenuLogoffAdapter(client));
    menuFileLogin.addActionListener(new ClientMenuLoginAdapter(client));
    menuViewPort.addActionListener(new ClientMenuPortAdapter(client));
    menuViewHost.addActionListener(new ClientMenuHostAdapter(client));
    menuViewIBlock.addActionListener(new ClientMenuIBlockAdapter(client));
    menuViewWhoBlocks.addActionListener(new ClientMenuWhoBlocksAdapter(client));
    menuEditBlock.addActionListener(new ClientMenuBlockAdapter(client));
    menuEditUnblock.addActionListener(new ClientMenuUnblockAdapter(client));
    menuEditClear.addActionListener(new ClientMenuClearAdapter(client));
    menuEditFwd.addActionListener(new ClientMenuForwardAdapter(client));
    menuEditUnFwd.addActionListener(new ClientMenuUnForwardAdapter(client));

    // Add the menus
    menuBar.add(menuFile);
    menuBar.add(menuEdit);
    menuBar.add(menuView);

    // Add the menu items
    menuFile.add(menuFileLogin);
    menuFile.add(menuFileLogoff);
    menuFile.add(new MenuItem("-"));
    menuFile.add(menuFileExit);
    menuEdit.add(menuEditBlock);
    menuEdit.add(menuEditUnblock);
    menuEdit.add(new MenuItem("-"));
    menuEdit.add(menuEditClear);
    menuEdit.add(new MenuItem("-"));
    menuEdit.add(menuEditFwd);
    menuEdit.add(menuEditUnFwd);
    menuView.add(menuViewPort);
    menuView.add(menuViewHost);
    menuView.add(new MenuItem("-"));
    menuView.add(menuViewIBlock);
    menuView.add(menuViewWhoBlocks);

    // Add the menu bar to the frame
    setMenuBar(menuBar);
    
    // Set the window properties
    // Added in Phase 5
    setLayout(null); // Used to let a layout manager place your objects
    setResizable(false);
    setTitle("Client Window");
    setBounds(1,1,600,550);
    addWindowListener(new MyWindowAdapter());
    setVisible(true);

    // Set the text area properties
    // Added in Phase 5
    add(clientText);
    clientText.setEditable(false);
    clientText.setBounds(10,100,380,400);

    // Set the list box properties
    // Added in Phase 5
    add(listOfClients);
    listOfClients.setBounds(400,60,175,450);

    // This method will set the text field properties including a KeyListener
    // that will handle the events generated by the text field.
    // Added in Phase 5
    newClientInput();

    // Set the button properties
    // Added in Phase 5

    // Add the button keylisteners
    // Added in Phase 5
    btnLogin.addActionListener(new ClientButtonLoginAdapter(client));
    btnLogoff.addActionListener(new ClientButtonLogoffAdapter(client));
    btnQuit.addActionListener(new ClientButtonQuitAdapter(client));

    // Add the buttons to the frame
    // Added in Phase 5
    add(btnLogin);
    add(btnLogoff);
    add(btnQuit);

    // Set the bounds
    // Added in Phase 5
    btnLogin.setBounds(10,510,75,30);
    btnLogoff.setBounds(100,510,75,30);
    btnQuit.setBounds(190,510,75,30);
    
    clientText.append("\n\nWelcome to the Simple Chat!!\n\n");
    
    //Set the objects to their not connected state.
    notConnected();
  }

  //Instance methods ************************************************
  
  /**
   * Description: This method sets object properties to what the should be when
   *  the client is connected. Added in phase 5.
   */
  void connected() 
  {
    menuFileLogin.setEnabled(false);
    menuFileLogoff.setEnabled(true);
    menuEditFwd.setEnabled(true);
    menuEditUnFwd.setEnabled(true);
    menuEditBlock.setEnabled(true);
    menuEditUnblock.setEnabled(true);
    menuViewIBlock.setEnabled(true);
    menuViewWhoBlocks.setEnabled(true);
    btnLogin.setEnabled(false);
    btnLogoff.setEnabled(true);
  }


  /**
   *  This method sets object properties to what the should be when
   *  the client is not connected.  Added in phase 5.
   */
  void notConnected() 
  {
    listOfClients.removeAll();
    menuFileLogin.setEnabled(true);
    menuFileLogoff.setEnabled(false);
    menuEditFwd.setEnabled(false);
    menuEditUnFwd.setEnabled(false);
    menuEditBlock.setEnabled(false);
    menuEditUnblock.setEnabled(false);
    menuViewIBlock.setEnabled(false);
    menuViewWhoBlocks.setEnabled(false);
    btnLogin.setEnabled(true);
    btnLogoff.setEnabled(false);
  }


  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    clientText.append(message + "\n");
  }
  
  /**
   * This method is called when the observers are notified of a
   * change.
   *
   * @param obj The object being observed.
   * @param arg The argument passed along to the observers.
   */
  public void update(Observable obj, Object arg)
  {
    // Added in phase 5: Retrieves text from the text field and sends
    // it to the server. 
    if (arg.equals("#enter"))
    {
      client.handleMessageFromClientUI(clientInput.getText());
      clientInput.setText("");
      return;
    }

    // Sets the buttons to the connected state (more are available)
    if (arg.equals("#connected"))
    {
      connected();
      return;
    }
    
    // Sets the buttons to the disconnected state
    // (fewer are available)
    if (arg.equals("#disconnected"))
    {
      notConnected();
      return;
    }

    if (arg.equals("#fwd"))
    {
      String selectedClient = "";
      
      selectedClient = listOfClients.getSelectedItem();

      try
      {
        // The substring is included since the format in the list box
        // is '<loginID> - <channel>'
        client.handleMessageFromClientUI("#fwd "
          + selectedClient.substring(0, selectedClient.indexOf("-") - 1));
      }
      catch(NullPointerException e)
      {
        // Handle situation when nothing is selected
        display("You must select a user from the list before "
          + "using this menu command.");
        return;
      }
      return;
    }
    
    if (arg.equals("#unblock"))
    {
      String selectedClient = "";
      
      selectedClient = listOfClients.getSelectedItem();
      
      try
      {
        // The substring is included since the format in the list box
        // is '<loginID> - <channel>'
        client.handleMessageFromClientUI("#unblock "
          + selectedClient.substring(0, selectedClient.indexOf("-") - 1));
      }
      catch(NullPointerException e)
      {
        // Handle situation when nothing is selected
        client.handleMessageFromClientUI("#unblock");
        return;
      }
      return;
    }
    
    if (arg.equals("#block"))
    {
      String selectedClient = "";
    
      selectedClient = listOfClients.getSelectedItem();
      
      try
      {
        // The substring is included since the format in the list box
        // is '<loginID> - <channel>'
        client.handleMessageFromClientUI("#block "
          + selectedClient.substring(0, selectedClient.indexOf("-") - 1));
      }
      catch(NullPointerException e)
      {
        // Handle situation when nothing is selected
        display("You must select a user to block from the list before "
          + "using this menu command.");
        return;
      }
      return;
    }
    
    if (arg.equals("#clear"))
    {
      clientText.setText("");
      return;
    }
    
    if (arg.toString().startsWith("#add"))
    {
      listOfClients.addToList(arg.toString().substring(5));
      return;
    }
    
    if (arg.toString().startsWith("#remove"))
    {
      listOfClients.removeFromList(arg.toString().substring(8));
      return;
    }

    if (arg.equals("#echo")) 
    {
      clientInput.setEchoChar((char)STAR);
      return;
    } 
    
    if (arg.equals("#noecho")) 
    {
      newClientInput();
      return;
    }

    if (arg.toString().startsWith("#"))
      return;

    display(arg.toString());
  }

  /**
   * This is a work around for a problem with text fields in
   * UNIX. The problem is once an echo character is set, it can't be taken
   * off, therefore a new instance of TextField must be created.
   */
  void newClientInput() 
  {
    remove(clientInput);
    clientInput = new TextField();
    add(clientInput);
    clientInput.setBounds(10,60,380,32);
    clientInput.addKeyListener(new ClientInputKeyAdapter(client));
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI. 
   * Login removed in phase 3 as the server will prompt the user 
   * for it. 
   *
   * @param args[0] The host name to connect to.
   * @param args[1] The port to connect on.  Added in phase 2.
   */
  public static void main(String[] args) 
  {
    String host = "";
    int port = 0;
    
    try
    {
      host = args[0];
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
    }
    
    try
    {
      port = Integer.parseInt(args[1]);
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT;
    }
    ObservableClient oclient = new ObservableClient(host, port);
    ClientGUI chat= new ClientGUI(oclient);
  }
}
//End of ConsoleChat class
