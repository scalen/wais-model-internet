
package animation.layout;

import animation.layout.GraphLayoutManager;
import java.util.Dictionary;
import java.util.Enumeration;
import java.awt.Dimension;
import animation.Node;

/**
   RandomLayoutManager provides and implementation of GraphLayoutManager that
   merely places nodes at random positions.
   This is primarily intended for testing.
*/
public class RandomLayoutManager extends GraphLayoutManager{

    /**
       The constructor simply takes the nodes and links as parameters.
       @param nodes a dictionary of all nodes, indexed by node address
       @param links a vector of links
    */
    public RandomLayoutManager(Dictionary nodes,java.util.Vector links,
			       Dimension screensize) {
      super(nodes,links,screensize);
    } 


    /**
       doLayout in this class just goes through all the nodes and gives them
       random positions.
       @param iterations randomlayout will ignore this parameter
    */
    public void doLayout(int iterations) {
      for (Enumeration e=m_nodes.elements();e.hasMoreElements();) {
	Node n=(Node)e.nextElement();

	n.setX(Math.random()*m_screen.width);
	n.setY(Math.random()*m_screen.height);
      }
    }
}
