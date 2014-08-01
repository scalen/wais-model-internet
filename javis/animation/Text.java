package animation;

import animation.VisualElement;
import math.Vector;
import util.Colour;

import java.awt.*;
import java.awt.Graphics;
import java.awt.FontMetrics;

/**
    This is class that deals with the text that the user has entered in the 
    trace file to be displayed.

    @author Steven Vischer
*/
public class Text extends VisualElement {

    String m_text;
    double expiry;

    public Text() {
        m_text="";
    }


    /**
       These functions are called when the TextEvent object is creating this
       instantiation.  They set the values for the string and the expiry time
    */
    public void setText(String text) {
        m_text=text;
    }

    public void setExpiry(double exp) {
        expiry=exp;
    }

    /**
       This draws the String of text onto the panel and ensures that the font 
       is set back to 8 point for the labels on the nodes.
       @param m_text the string to be displayed
    */
    public void draw(Graphics g,FontMetrics metrics) {
        g.setColor(Color.black);
        g.setFont(new java.awt.Font("SansSerif",java.awt.Font.PLAIN,16));
        g.drawString(m_text,10,20);
        g.setFont(new java.awt.Font("SansSerif",java.awt.Font.PLAIN,8));
    }

    public void update(double time) {}

    /**
       This method ascertains whether the string being displayed has reached 
       its expiry time.  If so, it returns false.
       @param expiry the time at which the text should cease to be displayed
    */
    public boolean isValid(double time) {
        return time<expiry;
    }
}
