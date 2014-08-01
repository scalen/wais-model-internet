
package animation;

import java.awt.*;
import animation.VisualElement;
//import math.Vector;
import java.util.Vector;

/**
    This class handles queues on the network.  Each queue corrseponds to a
    different node and will be displayed on the correct node.

    @author Steven Vischer
    @version 1.0
*/

public class Queue extends VisualElement {

    int m_src;
    int m_dest;
    int nodeX;  // x and y coordinates of the corresponding node
    int nodeY;  // for this queue
    Vector queuedPackets;

    public Queue() {
        nodeX=0;
        nodeY=0;
        queuedPackets=new Vector();
    }

    public int getSource() {
      return m_src;
    }

    public int getDest() {
      return m_dest;
    }

    public int getEndX() {
        int endx=nodeX+6;
        return endx;
    }

    public int getEndY() {
        int endy=nodeY-10-(8*(queuedPackets.size()-1));
        return endy;
    }
    /**
        Method called to add a packet to the queue.

        @param queuedPackets the Vector of packets currently in the queue.
    */
    public void enqueue(Packet p) {
      p.setStatus(Packet.PACKET_QUEUED);
      queuedPackets.addElement(p);
    }

    /**
        Method called to remove a packet from the queue.
    */
    public void dequeue(Packet p) {
      p.setStatus(Packet.PACKET_FREE);
      if(queuedPackets.size()>0) {
        for(int i=0; i<queuedPackets.size(); i++) {
            if(p.getID()==((Packet)queuedPackets.elementAt(i)).getID())
                queuedPackets.removeElementAt(i);
        }
      }
    }

    public void drop(Packet dp) {
        if(queuedPackets.size()>0) {
        for(int i=0; i<queuedPackets.size(); i++) {
            if(dp.getID()==((Packet)queuedPackets.elementAt(i)).getID())
                queuedPackets.removeElementAt(i);
        }
      }
    }

    /**
        This method is called to inform the queue of the position of its
        corresponding node.

        @param nodeX the x-coordinate of the source node
        @param nodeY the y-coordinate of the source node
    */
    public void setNodePosition(double x, double y) {
        nodeX=(int)x;
        nodeY=(int)y;
    }

    /**
        The draw() method for queues simply draws a column of squares above the
        corresponding node in the animation to represent the number of packets
        currently in the queue.
    */
    public void draw(Graphics g,FontMetrics metrics) {
        if(queuedPackets.size()>0) {
            g.setColor(Color.red);
            int coordX=nodeX+6;
            int coordY=nodeY-10;
            for(int i=0; i<queuedPackets.size(); i++) {
	        Packet packet=(Packet)queuedPackets.elementAt(i);
		g.setColor(packet.getPacketColour().getAWTColor());
                g.fillRect(coordX,coordY,6,6);
                coordY-=8;
            }
        }
        g.setColor(Color.black);
    }


    public void update(double time) {

    }

    /**
        These methods are called when creating a queue.
    */
    public void setSource(int source) {
        m_src=source;
    }

    public void setDestination(int dest) {
        m_dest=dest;
    }

    public Packet getQueuedPacket(int id) {
        for(int i=0; i<queuedPackets.size(); i++) {
            if(id==((Packet)queuedPackets.elementAt(i)).getID())
                return (Packet)queuedPackets.elementAt(i);
        }
        return null;
    }

}
