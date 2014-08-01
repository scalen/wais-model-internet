
package fileio.event;


import fileio.event.Event;
import animation.VisualElement;
import util.Input;
import util.Debug;

import java.io.Reader;
import java.io.IOException;



public class GroupEvent extends Event {

    String m_groupname="";
    int m_groupaddr=0;//group id
    int m_nodeaddr=0;//node to leave/join the group
    int m_state=0;//join/leave group


    public void read(Reader reader) {
    int i;
    int ch;

     try {

       while (true) {
	  
	 ch=Input.skipSpaces(reader);
	  
	 if (ch==-1) return;
	 if (ch!='-') return;
	 
	 ch=reader.read();

	 switch ((char)ch) {

	   case 'n': m_groupname=Input.getString(reader); break;

	   case 'i': m_groupaddr=Input.getInteger(reader); break;

	   case 'a': Input.skipSpaces(reader);
//GK	             m_state=JOIN; // JOIN is no defined
		     break;
                       
	   case 'x': Input.skipSpaces(reader);
//GK	             m_state=LEAVE;
		     break;


	   case 'o': m_nodeaddr=Input.getInteger(reader); break;

	    default: Debug.out.println("Not a valid character: "+(char)ch);
	 }
       }
     }
     catch(IOException e) {
       Debug.out.println("Failure");
     }
    }

    public VisualElement returnElement() {
      return null;
    }



}
