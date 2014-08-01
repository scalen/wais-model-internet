
package animation.layout;


import java.util.Dictionary;
import java.awt.Dimension;

/**
   GraphLayoutManager describes the minimum interface and common constructor
   for a layout manager that takes care of arranging the static network 
   components (link, node) on the screen.
   This class closely follows the Strategy design pattern.
*/
public abstract class GraphLayoutManager {

    protected Dictionary m_nodes;
    protected java.util.Vector m_links;
    protected Dimension m_screen;

    /**
       The constructor simply takes the nodes and links as parameters.
       @param nodes a dictionary of all nodes, indexed by node address
       @param links a vector of links
    */
    public GraphLayoutManager(Dictionary nodes,java.util.Vector links,
			      Dimension screensize) {
      m_nodes=nodes;
      m_links=links;
      m_screen=screensize;
    } 


    /**
       doLayout is supposed to be overridden by subclasses to do the actual
       layout.
       @param iterations the number of times the layout should be applied
    */
    public abstract void doLayout(int iterations);

}
