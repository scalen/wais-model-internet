
package animation;

import java.awt.Graphics;
import java.awt.FontMetrics;


public abstract class VisualElement {


    public abstract void draw(Graphics g,FontMetrics metrics);


    public abstract void update(double time);


    public boolean isValid(double time) {
      return false;
    }
}
