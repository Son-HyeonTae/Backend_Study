// This file contains material supporting the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

package drawpad;

public class PadMouseAdapter extends java.awt.event.MouseAdapter 
{
  DrawPad adaptee;

  public PadMouseAdapter(DrawPad adaptee) 
  {
    this.adaptee = adaptee;
  }

  public void mouseReleased(java.awt.event.MouseEvent e) 
  {
    adaptee.dragged = false;
    adaptee.handleRelease(e);
  }

  public void mousePressed(java.awt.event.MouseEvent e) 
  {
    adaptee.handleClick(e);
  }
}
