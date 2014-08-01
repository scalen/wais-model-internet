
package fileio.event;

import fileio.event.Event;
import util.Colour;
import animation.VisualElement;
import java.io.Reader;


/**
    This class is associated with Nodemark events which mark a node to
    make it stand out.

    @author Alex Nikolic
    @version 1.0
*/
public class NodemarkEvent extends Event {

    String m_name;
    int m_address;
    Colour m_colour;
    Colour m_oldcolour;
    int m_shape;
    boolean m_expired;


    public void read(Reader reader) {

    }


    public VisualElement returnElement() {
      return null;
    }
}
