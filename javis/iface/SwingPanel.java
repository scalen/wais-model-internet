

package iface;

import iface.VisualiserPanelObserver;
//import com.sun.java.swing.JPanel;
import javax.swing.JPanel;
import java.awt.Graphics;

/**
   SwingPanel provides an implementation of the visualiserpanel interface for
   the Swing class library. It does it by interitance, rather than by reference
   which would be possible as well..
*/
class SwingPanel extends JPanel implements VisualiserPanel {

    private VisualiserPanelObserver m_observer;


    /**
       The constructor will forward to the Swing JPanel constructor
    */
    public SwingPanel() {
      super();
      m_observer=null;
    }


    /**
       Return the width of this panel.
       @return width
    */
    public int getWidth() {
      return super.getWidth();
    }

    /**
       Return the height of this panel.
       @return height
    */
    public int getHeight() {
      return super.getHeight();
    }


    /**
       Attach an observer to this panel. Only one observer may be attach in
       this implementation.
    */
    public void attach(VisualiserPanelObserver observer) {
      if (m_observer==null) {
	m_observer=observer;
	m_observer.attach(this);
      }
    }


    /**
       Detach an observer.
    */
    public void detach(VisualiserPanelObserver observer) {
      if (m_observer==observer)
      m_observer=null;
    }


    /**
       Override the JPanel paintcomponent method to forward to our observer
    */
    public void paintComponent(Graphics g) {
      if (m_observer!=null)
      m_observer.paint(g);
    }

 
    /**
       Forward any repaint calls to the swing repaint function.
    */
    public void re_paint() {
      this.repaint();
    }
}
