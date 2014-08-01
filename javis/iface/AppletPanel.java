

package iface;

import iface.VisualiserPanelObserver;
import java.awt.Panel;
import java.awt.Canvas;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Graphics;

/**
   AppletPanel provides an implementation of the visualiserpanel interface for
   the AWT system. It does it by interitance, rather than by reference
   which would be possible as well..
*/
class AppletPanel extends Canvas implements VisualiserPanel {

    private VisualiserPanelObserver m_observer;
    private Image m_image=null;
    private Dimension m_imagesize=null;
    private Dimension m_preferred;

    /**
       The constructor will forward to the AWT Panel constructor
    */
    public AppletPanel(Dimension preferred) {
      super();

      m_observer=null;
      m_preferred=preferred;
    }


    /**
       Return the width of this panel.
       @return width
    */
    public int getWidth() {
      return m_preferred.width;
    }

    /**
       Return the height of this panel.
       @return height
    */
    public int getHeight() {
      return m_preferred.height;
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
       Override the Panel paint method to forward to our observer
    */
    public void paint(Graphics g) {
      update(g);
    }


    /**
       Override the Panel update method, we don't need any clearing
    */
    public void update(Graphics g) {

      if (m_image==null) {
	System.err.println("Creating image");
	m_image=createImage(m_preferred.width,m_preferred.height);
	if (m_image==null) System.err.println("OOPS");
      }

      if (m_observer!=null) {
 	if (m_image!=null)
        m_observer.paint(m_image.getGraphics());
	//	m_observer.paint(g);
      }

      if (m_image!=null)
      g.drawImage(m_image,0,0,getWidth(),getHeight(),this);      
    }


    /**
       Override the Panel getPreferredSize method. Request as much space as
       we can from the layout manager.
    */
    public Dimension getPreferredSize() {
      return m_preferred;
    }

    /**
       Forward any repaint calls to the panel repaint function.
    */
    public void re_paint() {
      this.repaint();
    }
}

