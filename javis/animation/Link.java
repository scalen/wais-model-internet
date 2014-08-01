
package animation;

import java.awt.Graphics;
import java.awt.FontMetrics;
import animation.VisualElement;
import math.Vector;
import util.Colour;
import util.Debug;

public class Link extends VisualElement {

    public static final int UP = 0;
    public static final int DOWN = 1;

    int m_src;
    int m_dest;
    double m_bandwidth;
    double m_delay;
    double m_length;
    int m_status;
    Colour m_colour;
    Node m_srcnode;
    Node m_destnode;

    public void setSourceNode(Node n) {
      m_srcnode=n;
    }

    public void setDestNode(Node n) {
      m_destnode=n;
    }

    public void setStatus(int status) {
      if (m_status==UP && status==DOWN) {
	m_colour=new Colour("red");
	Debug.out.println("Going down");
      }
      else
      if (m_status==DOWN && status==UP) {
	m_colour=new Colour("black");
      }

      m_status=status;
    }

    public void setSource(int src) {
        m_src=src;
    }

    public int getSource() {
        return m_src;
    }

    public double getBandwidth() {
        return m_bandwidth;
    }

    public void setDelay(double delay) {
        m_delay=delay;
    }

    public void setDestination(int dest) {
        m_dest=dest;
    }

    public int getDestination() {
        return m_dest;
    }

    public double getDelay() {
        return m_delay;
    }

    public double getPixelLength() {
        return Math.sqrt((m_destnode.getX()-m_srcnode.getX())*
			 (m_destnode.getX()-m_srcnode.getX())+
			 (m_destnode.getY()-m_srcnode.getY())*
			 (m_destnode.getY()-m_srcnode.getY()));
    }

    public void setBandwidth(double bandwidth) {
        m_bandwidth=bandwidth;
    }

    public void setColour(Colour c) {
        m_colour=c;
    }

    public void draw(Graphics g,FontMetrics metrics) {
        // Find the length of the link

        double dx=m_destnode.getX()-m_srcnode.getX();
        double dy=m_destnode.getY()-m_srcnode.getY();
        double len=(dx*dx+dy*dy);
        if (len==0) return;
        len=Math.sqrt(len);
        dx/=len; dy/=len;

	// Draw a line from slighly outside the node circles
        g.setColor(m_colour.getAWTColor());
        g.drawLine((int)(m_srcnode.getX()+dx*7),(int)(m_srcnode.getY()+dy*7),
	 	   (int)(m_destnode.getX()-dx*7),(int)(m_destnode.getY()-dy*7));
    }

    public void update(double time) {

    }

    public String toString() {
      return "Link S:"+m_src+" D:"+m_dest+" BW:"+m_bandwidth+" D:"+m_delay;
    }
}

