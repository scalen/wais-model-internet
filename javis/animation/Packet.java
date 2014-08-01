

package animation;

import animation.VisualElement;
import math.Vector;
import util.Colour;

import java.awt.Graphics;
import java.awt.FontMetrics;

/**
   Packet is the class that represents a network packet in Javis.
*/
public class Packet extends VisualElement {

    // Values for m_status. These determine how the packet is being drawn

    public final static int PACKET_FREE = 0;
    public final static int PACKET_QUEUED = 1;
    public final static int PACKET_DROPPED = 2;

    int m_status;             // One of the constants above

    long m_size;              // Packet size in bytes
    int m_convid;             // Conversation ID. Used for colouring
    int m_packetid;           // Packet ID (from IP)

    double m_leaveTime;       // Time when the packet left its source node
    double m_arriveTime;      // Time when the packet should arrive
    double m_transmitTime;    // Time it takes for the whole packet to go on
                              // the link (depends on packet size and bandw.)

    String m_type;            // Packet type (e.g. 'tcp')
    Colour m_colour;

    int m_src;                // Node where the packet left
    int m_dest;               // Node the packet is heading for
 
    double m_timeInc;

    Vector m_start;       // Start position when the packet left

    Vector m_front;       // Vectors with the front and tail coordinates of
    Vector m_tail;        // the packet, used in animation

    Vector m_direction;   // Distance vector between source and dest. node
    Vector m_perp;        // Perpendicular vector to direction, normalised

    int m_polygon_x[];    // Polygon points for drawing
    int m_polygon_y[];
    int m_polygon_points; // Number of points in the polygon

    Link m_link;          // The link this packet is currently on

    // -- The following are only used for animation when the packet is
    // -- dropping !
    final static int ROTATION_PHASES = 40;
    final static double ROTATION_PACKET_SIZE = 4.0;

    static double m_dropanim_x[]=null;
    static double m_dropanim_y[]=null;

    int m_dropanim_phase;


    /**
       Default constructor. Simply initialises everything.
    */
    public Packet() {
        m_front=new Vector();
	m_tail=new Vector();
        m_direction=new Vector();
	m_start=new Vector();
	m_perp=new Vector();
        m_colour= new Colour("black");

	m_polygon_x=new int[5];
	m_polygon_y=new int[5];
	m_polygon_points=4;

        m_leaveTime=m_arriveTime=m_transmitTime=0;

        m_status=PACKET_FREE;

        m_size=0;
        m_convid=0;
        m_packetid=0;

        m_type="";

        m_src=0;
        m_dest=0;

	m_dropanim_phase=0;
	precalculateAnimation();
    }
  

    /**
       A private helper function that will calculate the static animation 
       tables first time it is called.
    */
    private void precalculateAnimation() {
      // If it has been done before, just return
      if (m_dropanim_x!=null) return;

      m_dropanim_x=new double[ROTATION_PHASES*4];
      m_dropanim_y=new double[ROTATION_PHASES*4];

      // Preset first frame of the animation

      m_dropanim_x[0]=-ROTATION_PACKET_SIZE;
      m_dropanim_y[0]=-ROTATION_PACKET_SIZE;
      m_dropanim_x[1]=ROTATION_PACKET_SIZE;
      m_dropanim_y[1]=-ROTATION_PACKET_SIZE;
      m_dropanim_x[2]=ROTATION_PACKET_SIZE;
      m_dropanim_y[2]=ROTATION_PACKET_SIZE;
      m_dropanim_x[3]=-ROTATION_PACKET_SIZE;
      m_dropanim_y[3]=ROTATION_PACKET_SIZE;

      // Calculate the remaining frames

      double angle=0.0;
      for (int i=1;i<ROTATION_PHASES;i++) {

	for (int j=0;j<4;j++) {
	  m_dropanim_x[i*4+j]=Math.cos(angle)*m_dropanim_x[j]-
	                      Math.sin(angle)*m_dropanim_y[j];
	  m_dropanim_y[i*4+j]=Math.sin(angle)*m_dropanim_x[j]+
	                      Math.cos(angle)*m_dropanim_y[j];
	}

	angle+=(2.0*Math.PI)/(double)ROTATION_PHASES;
      }
    }

  
    /**
       Draw the packet on the panel given its graphics context.
    */
    public void draw(Graphics g,FontMetrics metrics) {
	switch (m_status) {
 	  case PACKET_FREE:
       case PACKET_DROPPED: g.setColor(m_colour.getAWTColor());
       	                    g.fillPolygon(m_polygon_x,m_polygon_y,
					  m_polygon_points); break;
	}
    }


    /**
       Update the position, etc. of the packet given the current time.
       @param time the current time in the animation in seconds
    */
    public void update(double time) {

        switch(m_status) {
 	  case PACKET_FREE: double front,tail;
	                    boolean clipped=false,draw_arrow=false;

			    // Find front of the packet in the time-scale
	                    if (time<m_arriveTime-m_transmitTime) {
	                      front=time-m_leaveTime;
			      draw_arrow=true;
			    }
			    else {
			      front=m_link.getDelay();
			      clipped=true;
			    }

			    // Find the tail of the packet
			    double t=time-m_transmitTime;
			    if (t<=m_leaveTime) {
			      tail=0.0;
			      clipped=true;
			    }
			    else
			    tail=t-m_leaveTime;

			    front=front*m_link.getPixelLength()/
			      m_link.getDelay();
			    tail=tail*m_link.getPixelLength()/
			      m_link.getDelay();

			    // Prevent packets from being pixel-sized and
			    // invisible by including a minimum length (4)

			    if (!clipped && Math.abs(front-tail)<1.0)
			    tail=front-4;

			    // Now create the polygon array

			    // See if we need a polygon with an arrow

			    if (draw_arrow && Math.abs(front-tail)>10) {
			      m_polygon_points=5;

			      // Scale the values to screen size

			      m_front.m_value[0]=m_start.m_value[0]+
				m_direction.m_value[0]*(front-6);
			      m_front.m_value[1]=m_start.m_value[1]+
				m_direction.m_value[1]*(front-6);

			      m_tail.m_value[0]=m_start.m_value[0]+
				m_direction.m_value[0]*tail;
			      m_tail.m_value[1]=m_start.m_value[1]+
				m_direction.m_value[1]*tail;

			      // Fill in the values

			      m_polygon_x[0]=(int)m_front.m_value[0];
			      m_polygon_x[1]=(int)m_tail.m_value[0];
			      m_polygon_x[2]=(int)(m_tail.m_value[0]+
						   m_perp.m_value[0]*8.0);
			      m_polygon_x[3]=(int)(m_front.m_value[0]+
						   m_perp.m_value[0]*8.0);
			      m_polygon_x[4]=(int)(m_start.m_value[0]+
						   m_direction.m_value[0]*
						   front+
						   m_perp.m_value[0]*4.0);

			      m_polygon_y[0]=(int)m_front.m_value[1];
			      m_polygon_y[1]=(int)m_tail.m_value[1];
			      m_polygon_y[2]=(int)(m_tail.m_value[1]+
						   m_perp.m_value[1]*8.0);
			      m_polygon_y[3]=(int)(m_front.m_value[1]+
						   m_perp.m_value[1]*8.0);
			      m_polygon_y[4]=(int)(m_start.m_value[1]+
						   m_direction.m_value[1]*
						   front+
						   m_perp.m_value[1]*4.0);
			    }
			    else {
			      m_polygon_points=4;

			      // Polygon without an arrow

			      // Scale the values to screen size

			      m_front.m_value[0]=m_start.m_value[0]+
				m_direction.m_value[0]*front;
			      m_front.m_value[1]=m_start.m_value[1]+
				m_direction.m_value[1]*front;

			      m_tail.m_value[0]=m_start.m_value[0]+
				m_direction.m_value[0]*tail;
			      m_tail.m_value[1]=m_start.m_value[1]+
				m_direction.m_value[1]*tail;

			      // Fill in the values

			      m_polygon_x[0]=(int)m_front.m_value[0];
			      m_polygon_x[1]=(int)m_tail.m_value[0];
			      m_polygon_x[2]=(int)(m_tail.m_value[0]+
						   m_perp.m_value[0]*8.0);
			      m_polygon_x[3]=(int)(m_front.m_value[0]+
						   m_perp.m_value[0]*8.0);
			      
			      m_polygon_y[0]=(int)m_front.m_value[1];
			      m_polygon_y[1]=(int)m_tail.m_value[1];
			      m_polygon_y[2]=(int)(m_tail.m_value[1]+
						   m_perp.m_value[1]*8.0);
			      m_polygon_y[3]=(int)(m_front.m_value[1]+
						   m_perp.m_value[1]*8.0);
			    }

			    break;

	  case PACKET_DROPPED: 
	                    m_tail.m_value[1]+=(m_timeInc*10000);
			    m_polygon_points=4;

			    // Copy the correct animation phase into our
			    // polygon

			    m_polygon_x[0]=(int)(m_dropanim_x[m_dropanim_phase+0]+m_tail.m_value[0]);
			    m_polygon_x[1]=(int)(m_dropanim_x[m_dropanim_phase+1]+m_tail.m_value[0]);
			    m_polygon_x[2]=(int)(m_dropanim_x[m_dropanim_phase+2]+m_tail.m_value[0]);
			    m_polygon_x[3]=(int)(m_dropanim_x[m_dropanim_phase+3]+m_tail.m_value[0]);
			    m_polygon_y[0]=(int)(m_dropanim_y[m_dropanim_phase+0]+m_tail.m_value[1]);
			    m_polygon_y[1]=(int)(m_dropanim_y[m_dropanim_phase+1]+m_tail.m_value[1]);
			    m_polygon_y[2]=(int)(m_dropanim_y[m_dropanim_phase+2]+m_tail.m_value[1]);
			    m_polygon_y[3]=(int)(m_dropanim_y[m_dropanim_phase+3]+m_tail.m_value[1]);
			    m_dropanim_phase=(m_dropanim_phase+4)%(ROTATION_PHASES*4);
	                    break;
        }
    }


    /**
       isValid will return false if the packet is not actually in use anymore,
       for example it has arrived at the destination.
       @param time the time at which the packet validity is to be tested
       @return false if the packet is not valid anymore
    */
    public boolean isValid(double time) {
        if(m_status==PACKET_DROPPED) return true;
        if(m_status==PACKET_QUEUED) return true;
        else return m_arriveTime>time;
    }


    /* 
       The following methods are utility functions to set various variables
       inside the Packet.
    */

    public void setStatus(int status) {
        m_status=status;
    }
    
    public void setSize(long size) {
        m_size=size;
    }

    public void setConvid (int convid) { 
        m_convid=convid;
    }
    
    public void setPacketid (int id) {
        m_packetid= id;
    }

    public void setType (String type) {
        m_type=type;
    }

    public void setPacketColour (Colour colour) {
        m_colour.setColour(colour);
    }

    public Colour getPacketColour() {
        return m_colour;
    }

    public void setLeaveTime (double time) {
        m_leaveTime= time;
    }

    public void setArriveTime (double time) {
        m_arriveTime=time;
    }

    public void setTransmitTime (double time) {
        m_transmitTime=time;
    }
    
    public void setSource (int src) {
        m_src=src;
    }
    
    public void setDestination (int dest) {
        m_dest=dest;
    }

    public void setLink(Link l) {
        m_link=l;
    }

    public void setStartPosition(double x,double y) {
        m_start.m_value[0]=x;
	m_start.m_value[1]=y;
    }

    public void setDropStart(double x, double y) {
        m_tail.m_value[0]=x;
        m_tail.m_value[1]=y;
    }

    public void setDirection(Vector v) {
        m_direction=v;
	m_direction.normalise();
    }

    public void setTimeInc(double inc) {   
        m_timeInc=inc;
    }


    public void readyToGo() {
        // Generate perpendicular vector

	m_perp.m_value[0]=-m_direction.m_value[1];
	m_perp.m_value[1]=m_direction.m_value[0];
	m_perp.normalise();

	// Move the start point outwards a bit

	m_start.m_value[0]+=m_perp.m_value[0]*3.0;
	m_start.m_value[1]+=m_perp.m_value[1]*3.0;
    }


    /* The following methods are userd to return the values of the object 
       variables */

    public int getStatus() {
        return m_status;
    }

    public int getSource() {
        return m_src;
    }

    public int getDestination() {
      	return m_dest;
    }   

    public long getSize() {
      return m_size;
    }

    public int getID() {
        return m_packetid;
    }

}














