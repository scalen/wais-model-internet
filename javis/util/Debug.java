
package util;

import util.DebugListener;
import util.DebugOut;

/**
   Debug is a class used to channel all debugging messages in one direction.
   All the real work is done by DebugOut, this was done in order to make it
   useable like System.out
   @see DebugOut
*/
public class Debug {

    public static DebugOut out;
}

