
package iface;

//import com.sun.java.swing.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Graphics;

import iface.SwingInterface;
import util.DebugListener;
import util.Preferences;


public class SwingDebug extends JFrame implements DebugListener, 
						  ActionListener,
						  Runnable
{

    SwingInterface m_iface;
    JTextArea m_text;
    JButton b_clear,b_hide;
    JToggleButton b_sticky;
    Thread m_thread;
    boolean m_endthread;

    Point m_iface_pos=new Point();
    Dimension m_iface_size=new Dimension();


    public SwingDebug(SwingInterface iface) {
      super("Debug Console");

      m_iface=iface;

      setSize(Preferences.debug_width,Preferences.debug_height);
      
      buildInterface();

      updatePosition();

      m_endthread=true;
      m_thread=null;
    }



    public void finalize() {
      stopThread();
    }

    public void println(String s) {
      m_text.append(s+"\n");
    }


    private void buildInterface() {
      getContentPane().setLayout(new BorderLayout());

      JToolBar tb=new JToolBar();

      b_clear=new JButton(new ImageIcon("resources/debug_clear.gif"));
      b_hide=new JButton(new ImageIcon("resources/debug_close.gif"));
      b_sticky=new JToggleButton(new ImageIcon("resources/debug_sticky_off.gif"),
				 Preferences.debug_is_sticky);
      b_sticky.setMargin(new Insets(0,0,0,0));
      b_sticky.setSelectedIcon(new ImageIcon("resources/debug_sticky.gif"));

      b_clear.addActionListener(this);
      b_hide.addActionListener(this);
      b_sticky.addActionListener(this);

      tb.add(b_clear);
      tb.add(b_hide);
      tb.add(b_sticky);

      getContentPane().add(tb,BorderLayout.NORTH);

      m_text=new JTextArea();
      getContentPane().add(new JScrollPane(m_text),BorderLayout.CENTER);
    }


    public void hideme() {

      setVisible(false);

      // Turn off sticky thread if it's running
      stopThread();
    }


    public void showme() {

      // Sticky window thread
      if (Preferences.debug_is_sticky) startThread();

      setVisible(true);
    }


    public void actionPerformed(ActionEvent ae) {
      if (ae.getSource()==b_hide) m_iface.hideDebugConsole();
      else
      if (ae.getSource()==b_clear) {
	m_text.selectAll();
	m_text.cut();
      }
      else
      if (ae.getSource()==b_sticky) {
	Preferences.debug_is_sticky=b_sticky.isSelected();
	if (!Preferences.debug_is_sticky) stopThread();
	else
	startThread();
      }
    }
  
    private void startThread() {
      if (m_thread!=null) stopThread();

      m_endthread=false;
      m_thread=new Thread(this);
      m_thread.start();
    }

    private void stopThread() {
      if (m_thread!=null) {
	try {
	  m_endthread=true;
	  m_thread.join();
	}
	catch (InterruptedException e) {}

	m_thread=null;
      }
    }


    public void paint(Graphics g) {
      updatePosition();
      super.paint(g);
    }


    public synchronized void updatePosition() {

      if (!Preferences.debug_is_sticky) return;

      Point new_iface_pos=m_iface.getLocationOnScreen();
      Dimension new_iface_size=m_iface.getSize();

      if (!new_iface_pos.equals(m_iface_pos) || 
	  new_iface_size.width!=m_iface_size.width) {
	m_iface_pos=new_iface_pos;
	m_iface_size=new_iface_size;
	
	setBounds(m_iface_pos.x+m_iface_size.width+2,
		  m_iface_pos.y,
		  this.getSize().width,
		  this.getSize().height);	  
      }
    }

    public void run() {

      while (!m_endthread) {

	updatePosition();

	try {
	  Thread.sleep(Preferences.debug_update_timeout);
	} 
	catch (InterruptedException e) {}

      }

    }

}

