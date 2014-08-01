

package animation;

import animation.VisualElement;
import animation.NodeMark;
import animation.Link;
import java.awt.Graphics;
import java.awt.FontMetrics;
import java.awt.Color;
import math.Vector;
import util.Colour;

public class Node extends VisualElement {

    static final int UP = 1;
    static final int DOWN = 0;

    int m_addr;
    int m_nodeID;
    int m_mcastGroup;
    NodeMark m_nodemark;
    String m_label;
    int m_shape;
    int m_state;
    Vector m_coords;
    Colour m_colour;

    class LinkPair {
      Node other_node;
      Link link;
    }
    java.util.Vector m_links;
  
    
    public Node() {
      m_addr=m_nodeID=m_mcastGroup=m_shape=m_state=0;
      m_coords=new Vector();
      m_nodemark=null;
      m_links=new java.util.Vector();
      m_colour=new Colour();
    }

   
    public void addLink(Node other,Link link) {
      LinkPair l=new LinkPair();

      l.other_node=other;
      l.link=link;

      m_links.addElement(l);
    }

    public Link getLink(int other_id) {

      for (java.util.Enumeration e=m_links.elements();e.hasMoreElements();) {
	LinkPair l=(LinkPair)e.nextElement();
	if (l.other_node.getNodeID()==other_id) return l.link;
      }

      return null;
    }

    public void setAddress(int addr) {
        m_addr=addr;
    }

    public int getAddress() {
        return m_addr;
    }


    public void setStatus(int status) {
        m_state=status;
    }


    public void setNodeID(int id) {
      m_nodeID=id;
    }

    public int getNodeID() {
      return m_nodeID;
    }
  
    public void setX(double x) {
      m_coords.m_value[0]=x;
    }

    public double getX() {
      return m_coords.m_value[0];
    }

    public void setY(double y) {
      m_coords.m_value[1]=y;
    }

    public double getY() {
      return m_coords.m_value[1];
    }

    public void setColour(Colour c) {
      m_colour=c;
    }

    public void move(double dx,double dy) {
      m_coords.translate(dx,dy);
    }


    public void draw(Graphics g,FontMetrics metrics) {

	g.setColor(m_colour.getAWTColor());
	g.drawOval((int)(m_coords.m_value[0])-7,
		   (int)(m_coords.m_value[1])-7,14,14);

	String s=(new Integer(m_nodeID).toString());
	g.setColor(Color.black);
	g.drawString(s,(int)(m_coords.m_value[0])-(metrics.stringWidth(s)>>1),
		       (int)(m_coords.m_value[1])+(metrics.getHeight()>>1));
    }



    public void update(double time) {

    }

  
    public String toString() {
      return "Node A:"+m_addr+" ID:"+m_nodeID+" S:"+m_shape+" "+m_coords;
    }
}

