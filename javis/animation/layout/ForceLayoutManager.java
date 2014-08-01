
package animation.layout;

import animation.layout.GraphLayoutManager;
import java.util.Dictionary;
import java.util.Enumeration;
import java.awt.Dimension;
import animation.Node;
import animation.Link;
import iface.VisualiserPanel;
import util.Debug;
import util.Preferences;

/**
   ForceLayoutManager provides an implementation of an autolayout mechanism
   shown in the JDK 1.1.7 demo directory. It's basically cut&paste&hack but
   works.
*/
public class ForceLayoutManager extends GraphLayoutManager{

    VisualiserPanel m_panel;

    /**
       Redraw the panel after doing that many iterations.
    */
    public final static int REPAINT_THRESHHOLD = 5;

    /**
       The constructor simply takes the nodes and links as parameters.
       @param nodes a dictionary of all nodes, indexed by node address
       @param links a vector of links
       @param screensize the dimensions that can be allocated to nodes
       @param panel the current drawing panel (used for updating)
    */
    public ForceLayoutManager(Dictionary nodes,java.util.Vector links,
			      Dimension screensize,VisualiserPanel panel) {
      super(nodes,links,screensize);
      Debug.out.println(screensize);
      m_panel=panel;
    } 


    /**
       doLayout will apply a force optimisation layout a specified number
       of times, where the number of times is set in the constructor.
       This uses the stuff found in the JDK 1.1.7. However, in addition to
       that it centres the networkx.
       @param iterations the number of times to repeat the layout 
    */
    public synchronized void doLayout(int iterations) {

      for (int cnt=0;cnt<iterations;cnt++) {

        double edgelen=Preferences.force_tension;

	for (Enumeration e=m_links.elements();e.hasMoreElements();) {
	  Link l=(Link)e.nextElement();

	  Node n1=(Node)m_nodes.get(new Integer(l.getSource()));
	  Node n2=(Node)m_nodes.get(new Integer(l.getDestination()));

	  double vx=n2.getX()-n1.getX();
	  double vy=n2.getY()-n1.getY();
	  double len=Math.sqrt(vx*vx+vy*vy);
	  double f=(edgelen-len)/(len*3);
	  
	  double dx=f*vx;
	  double dy=f*vy;
	
	  n1.move(-dx,-dy);
	  n2.move(dx,dy);
	}
	

	for (Enumeration i=m_nodes.elements();i.hasMoreElements();) {
	  Node n1=(Node)i.nextElement();
	  double dx=0;
	  double dy=0;

	  for (Enumeration j=m_nodes.elements();j.hasMoreElements();) {
	    Node n2=(Node)j.nextElement();

	    // No self movement
	    if (n1==n2) continue;
	    
	    double vx=n1.getX()-n2.getX();
	    double vy=n1.getY()-n2.getY();
	    double len=vx*vx+vy*vy;
	    
	    if (len==0) {
	      dx+=Math.random();
	      dy+=Math.random();
	    }
	    else
	      if (len<100*100) {
		dx+=vx/len;
		dy+=vy/len;
	      }
	  }
	  
	  double dlen=dx*dx+dy*dy;
	  
	  if (dlen>0) {
	    dlen=Math.sqrt(dlen)/2;
	    
	    n1.move(dx/dlen,dy/dlen);
	  }
	}

	// Values to find minima and maxima
	double minx=10000,maxx=-10000,miny=10000,maxy=-10000;

	// Find minima and maxima
	for (Enumeration i=m_nodes.elements();i.hasMoreElements();) {
	  Node n=(Node)i.nextElement();

	  if (n.getX()<minx) minx=n.getX();
	  else
	  if (n.getX()>maxx) maxx=n.getX();

	  if (n.getY()<miny) miny=n.getY();
	  else
	  if (n.getY()>maxy) maxy=n.getY();
	}

	// Now scale network (leaves a border of 50 pixels)
	double scale_x=(m_screen.width-50)/(maxx-minx);
	double scale_y=(m_screen.height-50)/(maxy-miny);
	double move_x=m_screen.width/2-(minx+(maxx-minx)/2);
	double move_y=m_screen.height/2-(miny+(maxy-miny)/2);

	//Debug.out.println(minx+" "+maxx);
	//Debug.out.println(move_x);
	
	for (Enumeration i=m_nodes.elements();i.hasMoreElements();) {
	  Node n=(Node)i.nextElement();

	  double x=n.getX()-m_screen.width/2+move_x;
	  double y=n.getY()-m_screen.height/2+move_y;
	  
	  x=x*scale_x+m_screen.width/2;
	  y=y*scale_y+m_screen.height/2;

	  n.setX(x);
	  n.setY(y);
	}



      }
    }
}


