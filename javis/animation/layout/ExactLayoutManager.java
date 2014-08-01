package animation.layout;

import java.util.Dictionary;
import java.util.Enumeration;
import java.awt.Dimension;

import animation.Node;

public class ExactLayoutManager extends GraphLayoutManager{

    double maxX, maxY;
    
    /**
       The constructor simply takes the nodes and links as parameters.
       @param nodes a dictionary of all nodes, indexed by node address
       @param links a vector of links
    */
    public ExactLayoutManager(Dictionary nodes,java.util.Vector links,
			       Dimension screensize) {
      super(nodes,links,screensize);
      maxX = maxY = 0;
    } 


    public void setMaxXMaxY() {
        for (Enumeration e=m_nodes.elements();e.hasMoreElements();) {
        	Node n=(Node)e.nextElement();
        	if(n.getX()>maxX){
        	    maxX = n.getX();
        	}
        	if(n.getY()>maxY){
        	    maxY = n.getY();
        	}
        }
        maxX += 5;
        maxY += 5;
    }
    
    /**
       doLayout in this class just goes through all the nodes and places
       them at their position, 
       @param iterations randomlayout will ignore this parameter
    */
    public void doLayout(int iterations) {
        setMaxXMaxY();
        
        for (Enumeration e=m_nodes.elements();e.hasMoreElements();) {
        	Node n=(Node)e.nextElement();
        
        	n.setX(n.getX() * m_screen.width/maxX);
        	n.setY(n.getY() * m_screen.height/maxY);
        }
    }
}
