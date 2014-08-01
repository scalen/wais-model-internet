

package util;


public final class Shape {

    
    public final static int SQUARE = 1;

    public final static int HEXAGON = 2;

    public final static int CIRCLE = 3;


    public static int parseShape(String s) {

      // Remove white space
      s.trim();

      if (s.equals("circle")) return CIRCLE;

      if (s.equals("square")) return SQUARE;

      if (s.equals("hexagon")) return HEXAGON;

      return CIRCLE;
    }
}


