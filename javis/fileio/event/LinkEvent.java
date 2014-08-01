
package fileio.event;


import fileio.event.Event;
import animation.VisualElement;
import util.Colour;
import java.io.Reader;
import java.io.IOException;
import animation.Link;
import util.Input;
import util.Debug;


/**
    LinkEvent is the class used to represent link events in the trace file
    @author Steven Vischer
    @version 1.0
*/
public class LinkEvent extends Event {

    int m_src;
    int m_dest;
    double m_bandwidth;
    double m_delay;
    int m_state;
    Colour m_colour;


    public LinkEvent() {
	m_src=0;
	m_dest=0;
	m_bandwidth=0;
	m_delay=0;
	m_state=Link.UP;
	m_colour=new Colour();
    }

    public int getSource() {
      return m_src;
    }

    public int getDestination() {
      return m_dest;
    }

    public int getStatus() {
      return m_state;
    }

    public Colour getColour() {
      return m_colour;
    }


    /**
       The 'read' function for LinkEvent.  This reads in all the flags for a 
       link event and sets this object's member variables to the correct 
       values.
    */
    public void read(Reader reader) {
        int i=0;
        int ch=0;
       
        try {

	  while (true) {
            ch=Input.skipSpaces(reader);

	    if (ch==-1) return;
	    if (ch!='-') return;
	    
            ch=reader.read();

	    switch((char)ch) {

	      case 's': m_src=Input.getInteger(reader); 
		        break;

	      case 'd': m_dest=Input.getInteger(reader); break;

	      case 'S': {
		          String s_string=Input.getString(reader);
			  if (s_string.trim().equals("UP"))
			  m_state=Link.UP;
			  else
			  if (s_string.trim().equals("DOWN"))
			  m_state=Link.DOWN;
	                }
	                break;

	      case 'c': {
			  String newcolour=Input.getString(reader).trim();
			  m_colour=new Colour(newcolour);
	                }
	                break;

	      case 'r': m_bandwidth=Input.getDouble(reader); break;
		        // DOESN'T WORK if bandwidth specified using
		        // byte parameters !!

	      case 'D': m_delay=Input.getDouble(reader); break;

	      case 'o': { // Skip orientation
		          ch=Input.skipSpaces(reader);
			  while(ch!=' ' && ch!='\n') {
			    ch=reader.read();
			  }
			  break;
	                }

              default: break;
	    }

	  }
        }
        catch(IOException e) {
	  Debug.out.println("Error!");
        }

        Debug.out.println(m_src + " " + m_dest + " " + m_bandwidth + " " + 
			  m_delay + " " + m_state);
    }


    /**
       This function returns a link object with the correct settings, ready 
       for use in the animation.
       @param newlink The link that is created and returned
    */
    public VisualElement returnElement() {
        Link newlink=new Link();

        newlink.setSource(m_src);
        newlink.setDestination(m_dest);
        newlink.setBandwidth(m_bandwidth);
        newlink.setDelay(m_delay);
        newlink.setStatus(m_state);
	newlink.setColour(m_colour);

        return newlink;

    }

    /**
       This is a simple little function used to display the details of the link
       for debugging purposes
    */
    public String toString() {
      return "L: "+m_time+" "+m_src+" "+m_dest+" "+m_state;
    }
}

