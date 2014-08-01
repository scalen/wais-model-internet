package animation;

import animation.*;
import animation.layout.*;

import iface.UserInterface;
import iface.VisualiserPanelObserver;
import iface.VisualiserPanel;

import fileio.*;
import fileio.event.*;

import math.*;

import util.Preferences;
import util.Debug;

import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;
import java.util.Enumeration;


/**
    This class handles the main loop in the animation.  It coordinates the
    creation of elements at the correct time and controls the Thread for the
    animation.

    @author Christian Nentwich
    @author Steven Vischer
    @version 1.0
*/
public class Scheduler implements Animation, Runnable {

    private double m_time;                // The current time in seconds
    private double m_timeinc;             // Time increment in seconds

    private UserInterface m_iface;
    private Thread m_runthread;           // Animation update thread
    private VisualiserPanel m_panel;      // The panel to draw onto

    private java.util.Vector m_elements;  // Dynamic elements (packets, etc.)
    private java.util.Hashtable m_nodes;
    private java.util.Hashtable m_queues;
    private java.util.Vector m_links;

    private FileIO m_fileio;              // Trace file reader
    private int m_filestatus;             // One of the constants below 
    private final static int FILE_CLOSED = 0;
    private final static int FILE_OPENED_RUN = 1;
    private final static int FILE_EOF = 2;

    private String m_filename;            // Current tracefile name

    private GraphLayoutManager m_layout;  // The layout manager currently used

    private boolean m_seekflag;           // Flag for the thread if it needs
                                          // to seek forward

    /**
       The only constructor. It will simply initialise all variables to
       default. You have to pass the user interface that controls this 
       animation as an argument.
       @param iface the UserInterface object that controls the animation.
    */
    public Scheduler(UserInterface iface) {
      m_time=0.0;
      m_timeinc=0.025;
      m_iface=iface;
      m_fileio=new TraceFileReader();
      m_runthread=null;
      m_filestatus=FILE_CLOSED;
      m_filename=null;
      m_elements=null;

      m_nodes=new java.util.Hashtable();
      m_queues=new java.util.Hashtable();
      m_links=new java.util.Vector();
      m_seekflag=false;
    }


    /**
       This function can (and must) be used to attach a panel that the
       elements can draw onto.
       @param panel the panel to draw onto
    */
    public void attach(VisualiserPanel panel) {
      if (m_panel==null)
      m_panel=panel;
    }


    /**
       loadFile will prescan the file given by its filename and extract the
       static network structure. It will autolayout and draw the network on
       the screen. It will also leave the file open for later playback.
       @param filename the name of the trace-file to be loaded.
    */
    public void loadFile(String filename) {

      // Reset the time

      m_time=0.0;
      m_iface.setTimeBar(m_time);

      // Get rid of any old elements

      m_elements=null;
      m_nodes=new java.util.Hashtable();
      m_queues=new java.util.Hashtable();
      m_links=new java.util.Vector();


      // Start reading

      TraceFileReader tr=new TraceFileReader();

      m_elements=tr.prescanFile(filename);
     
      Debug.out.println(m_elements.size());

      // Sort nodes, links and queues into their real arrays. This is not 
      // proper OOP (check out the if-else bit) but serves the purpose

      for (Enumeration e=m_elements.elements();e.hasMoreElements();) {
	VisualElement element=(VisualElement)e.nextElement();

	if (element instanceof Node)
	m_nodes.put(new Integer(((Node)element).getNodeID()),element);
	else
	if (element instanceof Link)
	m_links.addElement(element);	
	else
	if (element instanceof Queue) 
        m_queues.put(new Integer(((Queue)element).getSource()),element);
      }


      // Go through all the links, find their corresponding end-node elements
      // and attach them

      for (Enumeration e=m_links.elements();e.hasMoreElements();) {
	Link l=(Link)e.nextElement();
	if (l==null) continue;

	// !! TODO !! Check if either node does not exist. If so, offer a 
	// choice to disable the link or cancel loading.

	Node s=(Node)m_nodes.get(new Integer(l.getSource()));
	Node d=(Node)m_nodes.get(new Integer(l.getDestination()));

	l.setSourceNode(s);
	l.setDestNode(d);
	  
	s.addLink(d,l);
	d.addLink(s,l);

      }


      Debug.out.println("");
      Debug.out.println("Nodes: "+m_nodes.size());
      Debug.out.println("Links: "+m_links.size());

      //GK - added  by Gabi Kliot
      boolean USE_EXACT_LAYOUT = true;
      if(USE_EXACT_LAYOUT){
          m_layout=new ExactLayoutManager(m_nodes,m_links,
              		new Dimension(m_panel.getWidth(),
              	        	m_panel.getHeight()));
          m_layout.doLayout(1);
          m_panel.re_paint();
      }
      else{
          // Do a random layout and afterwards a force autolayout

          m_layout=new RandomLayoutManager(m_nodes,m_links,
				       new Dimension(m_panel.getWidth(),
						     m_panel.getHeight()));
          m_layout.doLayout(1);
          m_panel.re_paint();



          // Now create a force layout manager

          m_layout=new ForceLayoutManager(m_nodes,m_links,
				      new Dimension(m_panel.getWidth(),
				      m_panel.getHeight()),m_panel);
          m_layout.doLayout(Preferences.layout_initial);
      }
      
      m_filename=filename;
      m_filestatus=FILE_CLOSED;
      m_elements=new java.util.Vector();
    }



    /**
       startPlaying will create a new animation thread and start the update
       loop. Of course, a file has to be loaded first and you must not be
       playing already.
    */
    public void startPlaying() {

      // Open file if it's not open yet

      if (m_filestatus==FILE_CLOSED) {

	if (m_filename==null) return;
	
	m_fileio.openFile(m_filename);
	m_filestatus=FILE_OPENED_RUN;
      }


      // Thread is running at the moment, return quietly

      if (m_runthread!=null) return;


      // Otherwise start it

      m_runthread=new Thread(this,"Animation Thread");

      m_runthread.start();
    }


    /**
       stopPlaying will kill the animation thread and stop playback almost
       immediately. The thread has to be running otherwise it'll just return.
    */
    public void stopPlaying() {

      // No thread running
      if (m_runthread==null) return;

      // Stop the thread and join it
      m_runthread.stop();
      try {
        m_runthread.join();
      } catch(InterruptedException e) {}

      m_runthread=null;
    }



    /**
       Override the current time in the animation.
       @param time the new current time in seconds
    */
    public void setTime(double time) {
      m_time=time;
    }


    /**
       Returns the current time in the animation in seconds.
       @return the current time in seconds
    */
    public double getTime() {
      return m_time;
    }

    /**
       Set the curren time increment of the animation (the amount of time that
       is being added every frame).
       @param timeinc the new time increment in seconds
    */
    public void setTimeInc(double timeinc) {
      m_timeinc=timeinc;
    }


    /**
       Return the current time increment of the animation (the amount of 
       time that is being added every frame).
       @return the current time increment in seconds
    */
    public double getTimeInc() {
      return m_timeinc;
    }


    public void seekNext() {
      // Don't seek if the animation is not running
      if (m_runthread==null) return;
      
      m_seekflag=true;
    }
  

    /**
       Tell the Scheduler to rearrange the network layout on the screen. This
       function will start a thread to do the layout and return control
       immediately.
    */
    public void reLayout() {

      // Don't do autolayout if the animation is running
      if (m_runthread!=null) return;

      // Inner thread class to run the layout

      class LThread implements Runnable {
	public void run() {

	  for (int i=0;i<Preferences.layout_iteration;i++) {
	    m_layout.doLayout(5);
	    m_panel.re_paint();

	    try {
	      Thread.sleep(Preferences.fps);
	    } catch(InterruptedException e) {}

	  }
	}
      }

      // Start layout thread (TODO: Join the thread when it's done!!)

      Thread t=new Thread(new LThread());
      t.start();
    }

  

    /**
       Tell the Scheduler to 'shake' the network by moving the nodes around
       a bit, randomly. A thread will be started and control returns
       immediately.
    */
    public void shake() {

      // Don't shake if the animation is running
      if (m_runthread!=null) return;

      Debug.out.println("Shaking it!");

      // Inner thread class to shake the graph

      class LThread implements Runnable {
 
	public void run() {

	  for (int i=0;i<Preferences.layout_iteration;i++) {

	    Enumeration e=m_nodes.elements();
	    while (e.hasMoreElements()) {
	      Node curnode=(Node)e.nextElement();

	      // Generate a new position by moving the nodes a bit

	      double randx=curnode.getX()+(Math.random()*10.0)-5;
	      double randy=curnode.getY()+(Math.random()*10.0)-5;

	      // Clip coordinates to panel size

	      if (randx<10) randx=10;
	      else 
	      if (randx>m_panel.getWidth()-10) 
	      randx=m_panel.getWidth()-10;

	      if (randy<10) randy=10;
	      else 
	      if (randy>m_panel.getHeight()-10) 
	      randy=m_panel.getHeight()-10;

	      curnode.setX(randx);
	      curnode.setY(randy);
	    }

	    m_panel.re_paint();

	    try {
	      Thread.sleep(Preferences.fps);
	    } catch(InterruptedException exc) {}

	  }
	}
      }

      // Start layout thread (TODO: Join the thread when it's done!!)

      Thread t=new Thread(new LThread());
      t.start();
    }
    

    /**
       paint is called back by a panel to tell the Scheduler to redraw all
       its elements. It shouldn't be used for other purposes.
       @param g the graphics context to use for drawing.
    */
    public void paint(Graphics g) {

      // Fill background
      g.setColor(new Color(0xdddddd));
      g.fillRect(0,0,m_panel.getWidth(),m_panel.getHeight());

      g.setColor(Color.black);
   
      // Set font
      g.setFont(new java.awt.Font("SansSerif",java.awt.Font.PLAIN,8));

      // Get font settings
      java.awt.FontMetrics metrics=g.getFontMetrics();

      // Draw nodes

      for (Enumeration e=m_nodes.elements();e.hasMoreElements();) {
	Node n=(Node)e.nextElement();

	n.draw(g,metrics);
      }

      // Draw links

      for (Enumeration e=m_links.elements();e.hasMoreElements();) {
	Link l=(Link)e.nextElement();

	l.draw(g,metrics);
      }

      // Draw all the queues

      if (m_queues!=null) 
      for (Enumeration e=m_queues.elements();e.hasMoreElements();) {
	Queue q=(Queue)e.nextElement();

	q.draw(g,metrics);
      }


      // Draw dynamic elements (packets, etc.)

      if (m_elements!=null)
      for (Enumeration e=m_elements.elements();e.hasMoreElements();) {
	VisualElement ve=(VisualElement)e.nextElement();

	ve.draw(g,metrics);
      }

      g.setColor(Color.white);
    }

  
    /**
       handleEvent is used privately to dispatch events and updates elements.
       It admittedly shows rather bad design. This function needs redesigning.
       Lots of instanceof testing and casting is used. Events should maybe
       be handled by VisualElement classes themselves.
     */
    private void handleEvent(Event ev) {

      if (ev instanceof LinkEvent) {
	LinkEvent le=(LinkEvent)ev;

	// Find the correct link through one of the nodes involved

	Node s=(Node)m_nodes.get(new Integer(le.getSource()));
	if (s==null) return;

	Link l=s.getLink(le.getDestination());
	if (l==null) return;

	// Update status

	//l.setColour(le.getColour());
	l.setStatus(le.getStatus());
      }
      else
      if (ev instanceof TextEvent) {
        TextEvent te=(TextEvent)ev;

	// Just add a Text object to the elements

        Text t=(Text)te.returnElement();

        m_elements.addElement(t);
      }
      else 
      if (ev instanceof NodeEvent) {
	NodeEvent ne=(NodeEvent)ev;

	// Update the node colour

	Node n=(Node)m_nodes.get(new Integer(ne.getNodeID()));

	n.setColour(ne.getColour());
      }
      else
      if (ev instanceof StopEvent) {
        StopEvent se=(StopEvent)ev;

        this.stopPlaying();
      }
      else 
      if (ev instanceof PacketEvent) {
	PacketEvent pe=(PacketEvent)ev;

	// Packet events can be different kinds of events

	switch (pe.getEventType()) {

	case PacketEvent.ENQUE: { Queue q=(Queue)m_queues.get(new Integer(pe.getSource()));
	                          Node s=(Node)m_nodes.get(new Integer(pe.getSource()));
	                          // Queue not enable for display
	                          if (q==null) break;

				  // Add packet to the queue

				  q.setNodePosition(s.getX(),s.getY());
				  Packet p=(Packet)pe.returnElement();
				  q.enqueue(p);
	                        }
	                        break;

	case PacketEvent.DEQUE: {
	                          Queue q=(Queue)m_queues.get(new Integer(pe.getSource()));
	                          // Queue not enabled for display
	                          if (q==null) break;

				  Packet p=(Packet)pe.returnElement();
				  q.dequeue(p);
	                        }
	                        break;

	  case PacketEvent.HOP: Packet p=(Packet)pe.returnElement();
	                        Node s=(Node)m_nodes.get(new Integer(p.getSource()));
				Node d=(Node)m_nodes.get(new Integer(p.getDestination()));
				Link l=s.getLink(p.getDestination());

				if (l==null) return;

				// Calculate the direction the packet is moving

				Vector v=new Vector();
				v.m_value[0]=d.getX()-s.getX();
				v.m_value[1]=d.getY()-s.getY();

				// Set all the initial parameters of the packet

       				p.setStartPosition(s.getX(),s.getY());
				p.setDirection(v);
				p.setLink(l);
				p.setLeaveTime(pe.getTime());
				p.setTransmitTime((double)(pe.getSize()*8)/
						  l.getBandwidth());

				double t_time=l.getDelay()+
				  ((double)p.getSize()*8.0)/l.getBandwidth();

				p.setArriveTime(pe.getTime()+t_time);

				p.readyToGo();
				
				m_elements.addElement(p);
	                        
				break;

	 case PacketEvent.DROP: int packetID=pe.getPacketID();
	                        Queue q=(Queue)m_queues.get(new Integer(pe.getSource()));
				// Find out if the packet is dropping from a
				// queue or from a link..

				if (q!=null && q.getQueuedPacket(packetID)!=null) {
  				Packet dp=q.getQueuedPacket(packetID);
				  // Dropping from a queue

				  double endX=(double)q.getEndX();
				  double endY=(double)q.getEndY();
				  dp.setDropStart(endX,endY);
				  dp.setStatus(Packet.PACKET_DROPPED);
				  q.drop(dp);
				  m_elements.addElement(dp);
				}
				else {
				  Packet dp=null;

				  // Dropping from a link

				  for(int i=0; i<m_elements.size(); i++) {
                                    if(m_elements.elementAt(i) instanceof Packet) {
				      if(((Packet)m_elements.elementAt(i)).getID()==packetID)
					dp=(Packet)m_elements.elementAt(i);
                                    }
				  }
				  if (dp!=null)
				  dp.setStatus(Packet.PACKET_DROPPED);
				}
				break;
	}
	
      }
    }


    /**
       run contains the main animation loop and is the entry point for the
       animation thread. It is public because this class implements the
       Runnable interface but should not be called directly.
    */
    public void run() {
      Event ev=null;
      Debug.out.println(m_timeinc);

      do {

	// Handle any leftover events

	if (ev!=null && ev.getTime()<=m_time) {
	  handleEvent(ev);
	  ev=null;
	}

	// Grab new events

	if (ev==null)
	do {
	  ev=m_fileio.nextEvent(m_time);
	  if (ev==null) break;

	  if (ev.getTime()<=m_time)
	  handleEvent(ev);

	} while (ev.getTime()<=m_time);

	if (ev==null) break;


	// Simple update the time until the current time is greater than the
	// next event's time.

	while (m_time<ev.getTime()) {

	  m_time+=m_timeinc;
	  m_iface.setTimeBar(m_time);

	  if (m_elements!=null)
	  for (int i=0;i<m_elements.size();i++) {
	    VisualElement ve=(VisualElement)m_elements.elementAt(i);

	    ve.update(m_time);

	    if (!ve.isValid(m_time)) {
	      m_elements.removeElementAt(i);
	      i--;
	    }

	    if(ve instanceof Packet)
	      ((Packet)ve).setTimeInc(m_timeinc);
	  }

	  if (!m_seekflag) {

	    // Redraw the screen

	    m_panel.re_paint();

	    // Now wait a bit

	    try {
	      Thread.sleep(Preferences.fps);
	    }
	    catch (InterruptedException e) {}
	  }
	}

	m_seekflag=false;

      } while(ev!=null);

      m_filestatus=FILE_EOF;
      this.stopPlaying();
    }
}







