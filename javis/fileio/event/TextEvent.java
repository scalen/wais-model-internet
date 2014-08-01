package fileio.event;

import fileio.event.Event;
import animation.VisualElement;
import animation.Text;
import util.Colour;
import util.Input;
import util.Debug;

import java.io.Reader;
import java.io.IOException;

/**
    This is the class used in association with Text events.

    @author Steven Vischer
    @version 1.0
*/
public class TextEvent extends Event {
    String m_text;
    final double interval = 0.05;
            // The time for which the text remains on the screen

    public TextEvent() {
        m_text="";
    }

/**
    This method will read the text to be displayed.

    @param m_text the text to be displayed by the animation
*/
    public void read(Reader reader) {
        try {
            int ch=Input.skipSpaces(reader);
            if((char)ch=='-') ch=reader.read();
            if((char)ch=='\"') {
                ch=reader.read();

                while(ch!='\"') {
                    m_text+=(char)ch;
                    ch=reader.read();
                }
                Debug.out.println(m_text);
            }
        }
        catch(IOException e) {
            Debug.out.println("Error reading TextEvent");
        }
    }

/**
    This function will create a new instantiation of the Text class and
    return it to the Scheduler.

    @return newtext the newly created Text object
*/
    public VisualElement returnElement() {
        Debug.out.println("Text: " + m_text);
        Text newtext = new Text();
        newtext.setText(m_text);
        newtext.setExpiry(m_time+interval);

        return newtext;
    }
}
