

import iface.UserInterface;
import iface.SwingInterface;
import util.Preferences;


public class Main {

    public static void main(String args[]) {
      Preferences.load();

      UserInterface i=new SwingInterface();

      Preferences.save();
    }

  
}
