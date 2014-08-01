package fileio.event;


import fileio.event.Event;
import animation.VisualElement;
import animation.Queue;
import util.Colour;
import util.Input;
import util.Debug;

import java.io.Reader;
import java.io.IOException;

/**
    This class is instantiated when a queue event is found.  It will read its
    attributes and finally create a new Queue object for use by the animation.

    @author Steven Vischer
    @version 1.0
*/
public class QueueEvent extends Event {

    int m_src;
    int m_dest;

    public QueueEvent() {
      m_src=m_dest=0;
    }

/**
    The read() method will read the flags for this queue event and set its
    member variable to the correct values.
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

	      case 's': m_src=Input.getInteger(reader); break;
	      case 'd': m_dest=Input.getInteger(reader); break;
          default: break;
            }
      }
        }
    catch(IOException e) {
            Debug.out.println("Failure");
        }
    }

/**
    Method called by the Scheduler during the Scheduler to get an instance
    of this queue for use during the animation.

    @return newq the newly created queue
*/
    public VisualElement returnElement() {
        Queue newq=new Queue();

        newq.setSource(m_src);
        newq.setDestination(m_dest);

        return newq;
    }
}