

package util;


/**
   DebugListener specifies the minimum interface for a class that wants to
   output debug messages received by the Debug class.
   @see Debug
*/
public interface DebugListener {

    /**
       Print the given string, in whatever form (e.g. on the console or in
       a window).
       @param s the string to print
    */
    public void println(String s);

}
