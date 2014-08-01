
package animation;

import iface.VisualiserPanelObserver;


/**
   Animation is the main interface for the animation package. At the moment,
   it is only implemented by Scheduler and defines all the functions needed
   for proper network visualisation :).
   @author Christian Nentwich
   @version 0.1
*/
public interface Animation extends VisualiserPanelObserver {

    /**
       Instruct the Animation class to load a file and prepare to start
       playing.
       @param filename the name of the trace file to load
    */
    public void loadFile(String filename);


    /**
       Tell the animation to start playing.
    */
    public void startPlaying();


    /**
       Tell the animation to stop playing - to pause to be exact.
    */
    public void stopPlaying();


    /**
       Set the current time in the animation, this can be used from interface
       components, etc.
       @param time the current time to set
    */
    public void setTime(double time);


    /**
       Get the current time from the animation.
       @return the current time in the animation.
    */
    public double getTime();


    /**
       Tell the animation to skip all processing and go forward to the next
       event. Very useful if there is a long idle period.
     */
    public void seekNext();

    /**
       Set the time increment rate of the animation, also useable from inter-
       faces, etc.
       @param timeinc the new time increment
    */
    public void setTimeInc(double timeinc);


    /**
       Get the current time increment rate of the animation.
       @return the current time increment rate
    */
    public double getTimeInc();


    /**
       Tell the animation to re-layout the graph of the network on the screen.
    */
    public void reLayout();

    /**
       Tell the animation to shake the graph on the screen a bit. Useful if
       autolayout gets stuck.
    */
    public void shake();
}
