// This file contains material supporting the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import drawpad.OpenDrawPad;
import java.util.*;

/**
 * This class is used to start the DrawPad application without any client or
 * server.  It provides no facilities for quitting the application and is meant
 * to be used to demonstrate the use of DrawPad.
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @version August 2001
 */
public class StartDraw extends Observable implements Observer
{
  /**
   * The update method is used to send messages back to the DrawPad in order for
   * it to actually draw the design the mouse motion created.  This is done by
   * notifying the DrawPad, which observes an instance of this class.  It is 
   * called when the DrawPad notifies its observer, which is an instance of this
   * class.
   *
   * @param obs The Observable instance which sent the message. 
   * @param obj The message sent to the observers.
   */
  public void update(Observable obs, Object obj)
  {
    if (!(obj instanceof String))
      return;
    
    String msg = (String)obj;
    
    if (msg.startsWith("#send"))
    {
      setChanged();
      notifyObservers(msg.substring(6));
    }
  }
  
  /**
   * This method is responsible for the creation of the instances of this class
   * and of DrawPad.  It calls the Constructor of the OpenDrawPad class with the
   * same instance of this class as both its parameters.  Execution of this method
   * must be terminated manually.
   */
  public static void main(String[] args)
  {
    System.out.println("To stop execution of this application, you must");
    System.out.println("terminate it manually.  There is no code to stop");
    System.out.println("execution currently implemented.");
    
    StartDraw start = new StartDraw();
    new OpenDrawPad(start, start);
  }
} 
