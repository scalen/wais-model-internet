
package fileio.event;

import fileio.event.Event;
import animation.VisualElement;
import animation.Packet;
import util.Colour;
import util.Input;
import util.Debug;

import java.io.Reader;
import java.io.IOException;

import fileio.FileIO;


public class PacketEvent extends Event {

    long m_size;
    int m_src;
    int m_dest;
    Colour m_colour;
    int m_convid;
    int m_packetid;
    String m_type;
    int m_eventType;

    public static final int HOP=1;
    public static final int ENQUE=2;
    public static final int DEQUE=3;
    public static final int RECV=4;
    public static final int DROP=5;
    public static final int S_ENQUE=6;
    public static final int S_DEQUE=7;
    public static final int S_DROP=8;

    public PacketEvent(char eventchar) {
        m_size=0;
        m_src=0;
        m_dest=0;
        m_colour=new Colour("black");
        m_convid=0;
        m_packetid=0;
        m_type="";

	switch (eventchar) {
  	  case 'h': m_eventType=HOP; break;
  	  case '+': m_eventType=ENQUE; break;
  	  case '-': m_eventType=DEQUE; break;
	  case 'r': m_eventType=RECV; break;
 	  case 'd': m_eventType=DROP; break;
	   default: m_eventType=0;
	}
    }

    public void setEventType(int type) {
        m_eventType= type;
    }

    public int getEventType () {
        return m_eventType;
    }

    public void read(Reader reader) {

        int  ch;
        String str=new String();

        try {

	  while (true) {

            ch=Input.skipSpaces(reader);

	    if (ch==-1) return;
	    if (ch!='-') return;

	    ch=reader.read();

	    switch ((char)ch) {

	      case 's': m_src=Input.getInteger(reader); break;

	      case 'd': m_dest=Input.getInteger(reader); break;

	      case 'p': m_type=Input.getString(reader); break;

	      case 'e': m_size=Input.getInteger(reader); break;

	      case 'c': m_convid=Input.getInteger(reader);
		        m_colour.setColour(m_convid);
			break;

	      case 'i': m_packetid=Input.getInteger(reader); break;

	      case 'a': //m_colour.setColour(Input.getInteger(reader));
		        break;

	      case 'x': {
		          while ((char)ch!='}' && (char)ch!='\n')
			  ch=reader.read();
			  break;
	                }

              case 'f':
	      case 'm':
	      case 'S': // Haven't a clue what this is..
			{ int dummy=Input.getInteger(reader); break; }

	      case 'y': // No clue either
			{ while ((char)ch!='}' && (char)ch!='\n')
		          ch=reader.read();
			  break;
			}

	      default: Debug.out.println("Not a valid character: "+(char)ch); break;
	    }
	  }
	}
        catch(IOException e) {
            Debug.out.println("Failure");
        }
    }



    public VisualElement returnElement() {
        Packet newpacket= new Packet();

        newpacket.setSource(m_src);
        newpacket.setDestination(m_dest);
        newpacket.setSize(m_size);
        newpacket.setPacketColour(m_colour);
        newpacket.setConvid(m_convid);
        newpacket.setPacketid(m_packetid);
        newpacket.setType(m_type);

        return newpacket;

    }

    public int getSource() {
        return m_src;
    }

    public int getDest() {
	return m_dest;
    }

    public int getPacketID() {
        return m_packetid;
    }

    public long getSize() {
        return m_size;
    }

    public String toString() {
      return "P"+m_eventType+": "+m_time+" "+m_src+" "+m_dest+" "+
	        m_packetid+" "+m_size;
    }
}

