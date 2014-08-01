

package iface;

import java.awt.Graphics;
import iface.VisualiserPanel;


/**
   This defines an interface that class that wish to paint on a panel of the
   user-interface have to implement.
   @author Christian Nentwich
   @version 0.1
*/
public interface VisualiserPanelObserver {

    /**
       Attach a visualiser panel to this observer.
       @param panel the panel to attach
    */
    public void attach(VisualiserPanel panel);

    /**
       This method must be overridden to draw the graphics.
       @param g the AWT graphics context
    */
    public void paint(Graphics g);

}
