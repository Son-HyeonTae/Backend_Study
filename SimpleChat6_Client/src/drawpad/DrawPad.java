// This file contains material supporting the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

package drawpad;

/**
* Class DrawPad -- An instance of this class creates a window where users can
*  draw. The objects aren't directly printed to the window but rather sent to
*  the Observable who sends them to all Observers.
*
* @author Dr Timothy C. Lethbridge
* @author Paul Holden
* @author Fran&ccedil;ois B&eacute;langer
* @version August 2000
*/

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DrawPad extends Frame 
{
  //Class variables *************************************************
  
  /**
   * The maximum time allowed in between clicks for a double click 
   * (in miliseconds).
   */
  public static final int DOUBLE_CLICK_TIME = 400;
  
  /**
   * The maximum mouse mouvement allowed in between double click 
   * clicks (in pixels).
   */
  public static final int DOUBLE_CLICK_MOVE = 10;
  
  // INSTANCE VARIABLES *****************************************************

  /**
  * A reference to the OpenDrawPad instance that created this instance
  */
  OpenDrawPad openDrawPad;

  /**
  * Records the time of the last click
  */
  long lastClick = 0;

  /**
  * Flag indicating if the pad should be cleared
  */
  boolean clearPad = false;
  
  /**
  * Flag indicating if the cursor is being dragged
  */
  boolean dragged = false;

  /**
  * The coords of the last printed points
  */
  Point lastPt = new Point();
  
  /**
   * The coords of the first click in a possible double click. Set to 
   * (-20, -20) to avoid having the user being able to draw a line
   * before clicking at least 3 times on the pad. (-20, -20) will never
   * be a valid point.
   */
  Point firstPt = new Point(-20, -20);

  /**
  * List of all the coordinates
  */
  Vector points = new Vector();

  /**
  * The menu items
  */
  MenuBar menu = new MenuBar();
  Menu menuFile = new Menu("File");
  MenuItem menuFileClear = new MenuItem("Clear");
  MenuItem menuFileClose = new MenuItem("Close");

// CONSTRUCTORS ***********************************************************

  /**
   * @param openDrawPad   OpenDrawPad: An instance of the OpenDrawPad that
   * created this instance
   */
  public DrawPad(OpenDrawPad openDrawPad) 
  {
    this.openDrawPad = openDrawPad;

    // These listeners will wait for single clicks and dragging
    addMouseListener(new PadMouseAdapter(this));
    addMouseMotionListener(new PadMotionAdapter(this));
    addWindowListener(new PadWindowAdapter(this));

    // These listeners will listen for menu clicks
    menuFileClear.addActionListener(new PadClearAdapter(this));
    menuFileClose.addActionListener(new PadCloseAdapter(this));

    // Create the menu
    menuFile.add(menuFileClear);
    menuFile.add(menuFileClose);
    menu.add(menuFile);
    setMenuBar(menu); // Add the menu to the window

    // Set the window properties
    setTitle("Drawing Pad");
    setBackground(Color.white);
    setBounds(200,200,350,350);
    setVisible(true);
  }

  // INSTANCE METHODS *******************************************************


  /**
   * Called by the listeners when a mouse event is detected.
   *
   * @param MouseEvent   The event that occured
   */
  public void handleClick(MouseEvent e) 
  {
    if ((Math.abs((new Date()).getTime() - lastClick) < DOUBLE_CLICK_TIME)
       && (Math.abs(e.getX() - firstPt.x) < DOUBLE_CLICK_MOVE)
       && (Math.abs(e.getY() - firstPt.y) < DOUBLE_CLICK_MOVE))
    {
      openDrawPad.notifyAllObservers("#send #linedraw"
        + lastPt.x + "," + lastPt.y + "," + firstPt.x + ","
        + firstPt.y);
      
      lastPt = new Point(firstPt.x, firstPt.y);
    }
    else
    {
      openDrawPad.notifyAllObservers("#send #linedraw" 
        + e.getX() + "," + e.getY() + "," + e.getX() + ","
        + e.getY());
      
      lastPt = new Point(firstPt.x, firstPt.y);
      firstPt = new Point(e.getX(), e.getY());
        
      lastClick = (new Date()).getTime();
    }
  }
  
  /**
   * Called by the listeners when a mouse motion event is detected.
   *
   * @param MouseEvent   The event that occured
   */
  public void handleDrag(MouseEvent e) 
  {
    // If this condition is not specified, a fast drag will make the first drag
    // point treated as a line. Therefore a line will appear joining the
    // last point and the first point of the drag. The dragged is set to true
    // after the cursor has been dragged, not on its first click
    if (dragged)
    {
      openDrawPad.notifyAllObservers("#send #linedraw" 
        + lastPt.x + "," + lastPt.y + "," + e.getX() + "," 
        + e.getY());
        
      lastPt = new Point(e.getX(), e.getY());
    }
    else
    {
      openDrawPad.notifyAllObservers("#send #linedraw" 
        + e.getX() + "," + e.getY() + "," + e.getX() + ","
       + e.getY());
        
      lastPt = new Point(e.getX(), e.getY());
    }
  }
  
  public void handleRelease(MouseEvent e)
  {
    firstPt = new Point(e.getX(), e.getY());
  }
      
  /**
   * Called when the screen must be cleared.
   *
   * @param newValue   boolean: The value the clearPad var will take
   */
  public void setClearPad(boolean newValue) 
  {
    clearPad = newValue;
    repaint();
  }


  /**
   * This class overrids the update method in the Frame
   * class. Called when a repaint() command is used. The method was
   * overridden to stop the screen from refreshing everytime a new
   * object would be drawn, therefore the flash is eliminated.
   *
   * @param g   Graphics: The graphics instance
   */
  public void update (Graphics g) 
  {
    if (clearPad) 
    {
      // Erase all the coordinates
      points.removeAllElements(); // Clear the points vector
      g.setColor(Color.white); // Set the color to white

      // Get screen size and paint a white rectangle that size to
      // erase the drawpad. Used to prevent errors in some operating
      // systems when attempting to draw something larger than the
      // actual screen size
      Toolkit theKit = getToolkit();
      Dimension screenSize = theKit.getScreenSize();
      g.fillRect(-1,-1,screenSize.width, screenSize.height);

      g.setColor(Color.black); // Change the color back to black
      clearPad = false;
    }
    paint(g);
  }


  /**
   * This class overrids the paint method in the Frame
   * class. Used to draw to the Frame
   *
   * @param g   Graphics: The graphics instance
   */
  public void paint (Graphics g) 
  {
    try 
    {
      // Flag indicating if the point is part of a line
      Boolean isLine = null;

      // Flag indicating if this is the first point being painted
      boolean firstPT = true;

      // Create Iterators of the x and y coordinates
      Iterator IteratorPts = points.iterator();

      // Draws the objects
      while (IteratorPts.hasNext()) 
      {
        // Gets a set of coordinates
        Point coords = (Point)IteratorPts.next();
        Point coords2 = (Point)IteratorPts.next();

        // If the coordinates are part of a line
        g.drawLine(coords.x, coords.y, coords2.x, coords2.y);
      }
    } 
    catch (Throwable t) {}
  }
} // End of DrawPad class
