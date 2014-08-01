

package fileio;

import java.util.Vector;
import fileio.event.Event;

/**
   FileIO is the interface for reading and scanning tracefiles.
   
   @author Christian Nentwich
   @version 1.0
*/
public interface FileIO {


    /**
       Open trace file for playing and get ready to have nextEvent called. This
       will open the file and return.
       This may only be called after prescanFile!

       @param filename the file to open
       @see prescanFile
    */
    void openFile(String filename);


    /**
       Close the tracefile.
    */
    void closeFile();


    /**
       prescanFile will open the tracefile given and pick out all initiali-
       sation events (those which have a timestamp of '*'). It will return 
       a Vector of fully initialised VisualElements.

       @param filename the file to pre-scan
       @return a Vector of VisualElements.
    */
    Vector prescanFile(String filename);


    /**
       nextEvent will return the next event in the trace file. The time 
       parameter may be ignored but may be used in the future.
       All events with a timestamp of '*' will be skipped.

       @param time reserved for future use
       @return the next Event
    */
    Event nextEvent(double time);

}
