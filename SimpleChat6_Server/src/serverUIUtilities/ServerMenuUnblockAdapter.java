package serverUIUtilities;// Added in Phase 5import server.*;public class ServerMenuUnblockAdapter implements java.awt.event.ActionListener{  EchoServer adaptee;  public ServerMenuUnblockAdapter(EchoServer adaptee)   {    this.adaptee = adaptee;  }  public void actionPerformed(java.awt.event.ActionEvent e)   {    adaptee.notifyObservers("#unblock");  }}