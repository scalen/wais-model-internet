

import java.applet.*;
import java.awt.Panel;
import iface.UserInterface;
import iface.AppletInterface;
import iface.AppletDebug;
import util.DebugListener;
import util.Debug;
import util.Preferences;

public class MainApplet extends Applet {
    AppletInterface m_iface;

    public void init() {
      
      DebugListener m_debug=new AppletDebug();
      Debug.out.attach(m_debug);

      String filename=getParameter("TraceFileURL");
      if (filename==null) {
	Debug.out.println("Parameter TraceFileURL missing!");
	return;
      }

      if ((Preferences.rgb_database=getParameter("RGBDatabase"))==null) {
        Debug.out.println("Parameter RGBDatabase missing!");
	return;
      }
      
      m_iface=new AppletInterface(getSize(),"bongo");

      add((Panel)m_iface);

      m_iface.loadFile(filename);
    }


    public void run() {

    }
}
