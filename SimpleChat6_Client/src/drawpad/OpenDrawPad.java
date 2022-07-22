// This file contains material supporting the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

package drawpad;

import java.util.*;
import java.awt.*;

/**
 * Class OpenDrawPad -- An instance of this class can be created by any class
 * as long as it implements Observer and can give an instance of this class a
 * reference to its Observable (The instance it is observing). The purpose of
 * this class is to allow any observer to use the DrawPad class.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Paul Holden
 * @author Fran&ccedil;ois B&eacute;langer
 * @version August 2000
 */

public class OpenDrawPad extends Observable implements Observer 
{

  // INSTANCE METHODS ****************************************************

  /**
   * A reference to an instance of DrawPad
   */
  DrawPad drawPad;

  // CONSTRUCTORS ********************************************************

  /**
   * @param observable The "observable" (by way of an adapter) client or server
   * @param obs Observer: An Observer of the "Observable" (see 1st parameter)
   */
  public OpenDrawPad(Observable observable, Observer obs) 
  {
    observable.addObserver(this); // Add this to the Observers Observable
    addObserver(obs); // Add the Observer to this Observable
    drawPad = new DrawPad(this); // Create an instance of DrawPad
  }

  //Instance methods ************************************************

  /**
   * Sends a msg to all the Observers observing this instance
   *
   * @param msg   String: The msg to send to all observers
   */
  public void notifyAllObservers(String msg) 
  {
    setChanged();
    notifyObservers(msg);
  }

  /**
   *  This method overrids the update method in the Observer
   *  class. Called when the observable class notifies the observers
   *
   * @param Observable   The class that notified the observers
   * @param Object   The argument sent when observers were notified
   */
  synchronized public void update(Observable obj, Object arg) 
  {
    if (!(arg instanceof String))
      return;
      
    String msg = (String)arg;

    //The #linedraw command has the following format:
    //"#linedraw<x1>,<y1>,<x2>,<y2>
    
    if (msg.indexOf("#linedraw")==0) 
    {  
      //The location of the 3 commas.
      int comma1 = 0;
      int comma2 = 0;
      int comma3 = 0;
      
      comma1 = msg.indexOf(",");
      comma2 = msg.indexOf(",", comma1 + 1);
      comma3 = msg.indexOf(",", comma2 + 1);
      
      drawPad.points.addElement(new Point(
        Integer.parseInt(msg.substring(9, comma1)),
        Integer.parseInt(msg.substring(comma1 + 1, comma2))));
      
      drawPad.points.addElement(new Point(
        Integer.parseInt(msg.substring(comma2 + 1, comma3)),
        Integer.parseInt(msg.substring(comma3 + 1))));

      drawPad.repaint();
    }
  }
}
