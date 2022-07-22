// This file contains material supporting the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

package drawpad;

public class PadMotionAdapter extends java.awt.event.MouseMotionAdapter 
{
  DrawPad adaptee;

  public PadMotionAdapter(DrawPad adaptee) 
  {
    this.adaptee = adaptee;
  }

  public void mouseDragged(java.awt.event.MouseEvent e) 
  {
    adaptee.handleDrag(e);
    adaptee.dragged = true;
  }
}
