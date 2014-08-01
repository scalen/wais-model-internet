

package fileio.event;

import animation.VisualElement;
import java.io.Reader;

/**
    This is the abstract class for events from which all other events
    are extended.  It defines the interface expected for all event classes
    and that they must all have a time variable.

    @author Christian Nentwich
*/
public abstract class Event {

    protected double m_time;  // Each event must know its time of occurrence


    public void setTime(double time) { 
      m_time=time;
    }


    public double getTime() {
      return m_time;
    }

    /**
      Each event must be able to read its own attributes from the trace file
      @param reader a BufferedReader passed to the event by the 
      TraceFileReader class
    */
    public abstract void read(Reader reader);
  
    /**
       Events must be able to return a new instance of the element associated
       with the event.
    */
    public abstract VisualElement returnElement();

}
