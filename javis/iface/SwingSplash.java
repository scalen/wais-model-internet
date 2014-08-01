

package iface;

//import com.sun.java.swing.*;
import javax.swing.*;
import java.awt.Point;
import java.awt.BorderLayout;


public class SwingSplash extends JWindow implements Runnable {
    private int m_timeout;
    private JPanel m_panel;

    SwingSplash(int timeout,Point parentpos) {

      super();
      
      setBounds(parentpos.x+300-324/2,parentpos.y+225-204/2,324,204);

      m_panel=new JPanel();
      m_panel.setLayout(new BorderLayout());

      m_panel.add(new JLabel(new ImageIcon("resources/javis.gif")));
      getContentPane().add(m_panel);

      setVisible(true);


      m_timeout=timeout;
    }
  /*
    public void paint(java.awt.Graphics g) {
      g.drawString("Javis",20,20);
    }
  */
    public void run() {
      int x=0;

      while (x<m_timeout) {

	try {
	  Thread.sleep(1000);
	}
	catch (InterruptedException e) {}

	x++;
      }

      dispose();
    }
}
