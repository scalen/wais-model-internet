package fileio.event;

import fileio.event.Event;
import animation.VisualElement;
import util.Colour;
import util.Input;
import util.Debug;

import java.io.Reader;
import java.io.IOException;

/**
    This class is used when the user has put stop events in the trace file.
    It does nothing other than know the time at which the halt in the animation
    occurs.

    @author Steven Vischer
    @version 1.0
*/
public class StopEvent extends Event {

    public StopEvent() {
    }

    public void read(Reader reader) {
    }

    public VisualElement returnElement() {

        return null;
    }
}