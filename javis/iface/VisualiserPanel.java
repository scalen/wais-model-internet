

package iface;

import iface.VisualiserPanelObserver;

/**
   VisualiserPanel specifies the minimum interface for a Panel that can be 
   used with the visualiser.
   @author Christian Nentwich
   @version 0.1
*/
public interface VisualiserPanel {

    /**
       Return the width of this panel.
       @return width
    */
    public int getWidth();


    /**
       Return the height of this panel.
       @return width
    */
    public int getHeight();



    /**
       Attach an observer that will paint on the panel. It is left to the
       implementation whether multiple observers are allowed.
       @param observer the observer to attach.
    */
    public void attach(VisualiserPanelObserver observer);


    /**
       Detach an observer from the panel.
       @param observer the observer to remove.
    */
    public void detach(VisualiserPanelObserver observer);


    /**
       Tell this panel to repaint itself. The name had to be chosen to be
       different from the AWT function repaint()!
    */
    public void re_paint();

}
