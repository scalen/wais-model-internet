
package util;

import util.Debug;
import util.DebugListener;

public class DebugOut {

    public static DebugListener m_listener=null;

    public static void attach(DebugListener listener) {
      m_listener=listener;
    }


    public static void println(String s) {
      if (m_listener!=null)
      m_listener.println(s);
    }


    public static void println(Object o) {
      println(o.toString());
    }


    public static void println(int i) {
      println(String.valueOf(i));
    }


    public static void println(double d) {
      println(String.valueOf(d));
    }
}

