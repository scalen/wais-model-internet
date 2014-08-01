                                    
package fileio.event;


import fileio.event.Event;
import animation.VisualElement;
import animation.Node;
import util.Colour;
import util.Input;
import util.Debug;

import java.io.Reader;
import java.io.IOException;

/**
    This class handles Node events, reading them from the Trace file and
    creating the corresponding node.
    @author Steven Vischer
*/
public class NodeEvent extends Event {

    int m_addr;
    int m_dest;
    int m_state;
    int m_src;
    Colour m_colour;
    Colour m_oldcolour;

    //GK - added  by Gabi Kliot
    double m_X;
    double m_Y;

    /**
       The constructor is simply to set the correct initial values.
    */
    public NodeEvent() {
      m_addr=m_src=m_dest=0;
      m_X = m_Y = 0; // GK
      m_state=1;
      m_colour=new Colour();
      m_oldcolour=new Colour();
    }


    public int getNodeID() {
      return m_src;
    }

    public Colour getColour() {
      return m_colour;
    }


    /**
       The read function reads all the flags associated with this node event.
    */
    public void read(Reader reader) {
        int i;
	int ch;
	String digits=new String();

        try {

	  while (true) {

	    ch=Input.skipSpaces(reader);

	    if (ch==-1) return;
            if (ch!='-') return;
            
            ch=reader.read();

            switch((char)ch) {

	      case 'a': m_addr=Input.getInteger(reader); break;
	      case 's': m_src=Input.getInteger(reader); break;
	      case 'S': {
		          ch=Input.skipSpaces(reader);
			  if((char)ch=='U') m_state=1;
			  else m_state=0;
			  while(ch!=' ' && ch!='\n') {
			    ch=reader.read();
			  }
			  break;
	                }
	      case 'v': {
		          ch=Input.skipSpaces(reader);
			  while(ch!=' ' && ch!='\n') {
			    ch=reader.read();
			  }
			  // Create shape here
			  break;
                        }

	      case 'c': {
			  String colour=Input.getString(reader).trim();
      			  m_colour.setColour(colour);
			  break;
                        }
	      
	      //GK - added  by Gabi Kliot
	      case 'x': m_X =Input.getDouble(reader); break; // GK
	      case 'y': m_Y =Input.getDouble(reader); break; // GK

	       default: break;
	    }
	  }
        }
        catch(IOException e) {
            Debug.out.println("Failure");
        }
    }


    /**
       Create a new node and return it for the animation.
       @return newnode the created node
    */
    public VisualElement returnElement() {
        Node newnode=new Node();

        newnode.setAddress(m_addr);
        newnode.setStatus(m_state);
	newnode.setNodeID(m_src);
	newnode.setColour(m_colour);

	//GK
	newnode.setX(m_X);
	newnode.setY(m_Y);
	
	Debug.out.println("NEW: "+newnode);

        return newnode;
    }

    /**
       This method is used for displaying details of the node in the debugging
       window.
    */
    public String toString() {
      return "N: "+m_time+" "+m_src+" "+m_addr+" "+m_state;
    }


}

