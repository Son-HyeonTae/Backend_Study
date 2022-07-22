// This file contains material supporting the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

package drawpad;

class PadWindowAdapter extends java.awt.event.WindowAdapter 
{
  DrawPad adaptee;

  PadWindowAdapter(DrawPad adaptee) 
  {
    this.adaptee = adaptee;
  }

  public void windowClosing(java.awt.event.WindowEvent e) 
  {
    adaptee.dispose();
  }
}
