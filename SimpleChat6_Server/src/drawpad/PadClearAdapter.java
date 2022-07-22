// This file contains material supporting the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

package drawpad;

public class PadClearAdapter implements java.awt.event.ActionListener 
{
  DrawPad adaptee;

  public PadClearAdapter(DrawPad adaptee) 
  {
    this.adaptee = adaptee;
  }

  public void actionPerformed(java.awt.event.ActionEvent e) 
  {
    adaptee.setClearPad(true);
    adaptee.repaint();
  }
}
