
package fileio;


import fileio.FileIO;
import java.util.Vector;
import fileio.event.*;
import java.io.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.zip.GZIPInputStream;

import animation.*;
import util.Colour;
import util.Input;
import util.Debug;


public class TraceFileReader implements FileIO {

    BufferedReader m_reader=null;
    boolean m_warntcl=true;

    /**
       Open trace file for playing and get ready to have nextEvent called. This
       will open the file and return.
       This may only be called after prescanFile!

       @param filename the file to open
       @see prescanFile
    */
    public void openFile(String filename) {
        if (filename.startsWith("http://")) {
	  try {
	    URL url=new URL(filename);
	    
	    InputStream is=url.openStream();
	    if (filename.endsWith(".gz")) is=new GZIPInputStream(is);

	    m_reader=new BufferedReader(new InputStreamReader(is));
	  }
	  catch (IOException e) { 
	    return;
	  }
        }
	else {
	  try {
	    InputStream is=new FileInputStream(filename);

	    if (filename.endsWith(".gz")) is=new GZIPInputStream(is);

	    m_reader=new BufferedReader(new InputStreamReader(is));
	  }
	  catch (IOException e) {
	    return;
	  }
	}

	m_warntcl=true;
    }



    /**
       Close the tracefile.
    */
    public void closeFile() {
        try {
            m_reader.close(); // throws IOException;
        }
        catch(IOException e) { }

    }


    /**
       prescanFile will open the tracefile given and pick out all initiali-
       sation events (those which have a timestamp of '*'). It will return
       a Vector of fully initialised VisualElements.

       @param filename the file to pre-scan
       @return a Vector of VisualElements.
    */
    public Vector prescanFile(String filename) {

      Vector stampedEvents = new Vector();
      BufferedReader prescan = null;

      if (filename.startsWith("http://")) {
	try {
	  URL url=new URL(filename);
	  
	  InputStream is=url.openStream();
	  if (filename.endsWith(".gz")) is=new GZIPInputStream(is);
	  
	  prescan = new BufferedReader(new InputStreamReader(is));
	}
	catch (IOException e) { 
	  return null;
	}
      }
      else {
	try {
	  InputStream is=new FileInputStream(filename);
	  
	  if (filename.endsWith(".gz")) is=new GZIPInputStream(is);
	  
	  prescan=new BufferedReader(new InputStreamReader(is));
	}
	catch (IOException e) {
	  return null;
	}
      }


      try {

	while (true) {

	  StringBuffer buffer;
	  buffer=Input.getLine(prescan);

	  // Empty buffer, probably end of file
	  if (buffer.length()==0) break;

	  //Debug.out.println(buffer);
	  BufferedReader linereader=new BufferedReader(
					new StringReader(buffer.toString()));


	  // Get event type
	  int event_type=linereader.read();
	  int ch=' ';

	  if (event_type=='#' || event_type=='\n') continue;

	  // Check for -t
	  ch=Input.skipSpaces(linereader);
	  if (ch!='-') continue;
	  if ((char)linereader.read()!='t') continue;

	  // Get timestamp, if not '*' we're finished
	  ch=Input.skipSpaces(linereader);
	  if ((char)ch!='*') break;

	  Event newevent=null;

	  switch (event_type) {

 	    case 'l': newevent=new LinkEvent();
	              newevent.read(linereader);
		      break;

  	    case 'n': newevent=new NodeEvent();
	              newevent.read(linereader);
		      break;
		      
	    case 'q': newevent=new QueueEvent();
	              newevent.read(linereader);
		      break;

 	    case 'c': Colour.parseNewColour(linereader);
	              break;
	  }

	  if (newevent!=null)
	  stampedEvents.addElement(newevent.returnElement());

	}
      }
      catch(IOException e) {
	Debug.out.println("End of file");
      }

      return stampedEvents;
    }


    /**
       nextEvent will return the next event in the trace file. The time
       parameter may be ignored but may be used in the future.
       All events with a timestamp of '*' will be skipped.

       @param time reserved for future use
       @return the next Event
    */
    public Event nextEvent(double time) {
      try {

	while (true) {

	  StringBuffer buffer;
	  buffer=Input.getLine(m_reader);

	  // Empty buffer, probably end of file
	  if (buffer.length()==0) return null;

	  //Debug.out.println(buffer);
	  BufferedReader linereader=new BufferedReader(
					new StringReader(buffer.toString()));


	  // Get event type
	  int event_type=linereader.read();
	  int ch=' ';

	  if (event_type=='#') event_type=linereader.read();
	  if (event_type=='\n') continue;

	  // Check for -t
	  ch=Input.skipSpaces(linereader);
	  if (ch!='-') continue;
	  if ((char)linereader.read()!='t') continue;

	  // Get timestamp
	  ch=Input.skipSpaces(linereader);

	  String digits=new String();
	  while ((char)ch!=' ' && (char)ch!='\n') {
	    digits+=(char)ch;
	    ch=linereader.read();
	  }

	  // Skip * timestamp
	  if (digits.equals("*")) continue;

	  double e_time=Double.valueOf(digits).doubleValue();
	  Event result=null;
	  //	  Debug.out.println(e_time);

 	  switch ((char)event_type) {
	    case '$': result=new StopEvent();
	              result.read(linereader);
                      break;

	    case '?': result=new TextEvent();
	              result.read(linereader);
                      break;

  	    case 'l': result=new LinkEvent();
	              result.read(linereader);
		      break;

  	    case 'n': result=new NodeEvent();
	              result.read(linereader);
		      break;

 	    case 'h':
	    case 'r':
	    case '+':
	    case '-':
	    case 'd': result=new PacketEvent((char)event_type);
	              result.read(linereader);
		      break;

  	    case 'v': if (m_warntcl) {
	                Debug.out.println("WARNING: TCL expression found. "+
					   "TCL expressions cannot be "+
					   "evaluated. This warning will not "+
					   "be repeated!");
		        m_warntcl=false;
	              }
	              continue;


  	    default: Debug.out.println("Skipping unknown event type '"+
					(char)event_type+"'!"); continue;
	  }
	  
	  result.setTime(e_time);
	  return result;
	}
	
      }
      catch (IOException e) {
      }

      return null;
    }

}



