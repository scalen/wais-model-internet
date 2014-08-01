
package iface;

import iface.UserInterface;
import iface.SwingPanel;
import iface.SwingSplash;
import iface.SwingPreferences;
import iface.SwingDebug;
import iface.SwingAbout;

import animation.Animation;
import animation.Scheduler;

import util.Preferences;
import util.Debug;

//import com.sun.java.swing.*;
//import com.sun.java.swing.event.*;
//import com.sun.java.swing.preview.*;
//import com.sun.java.swing.text.*;
//import com.sun.java.swing.preview.filechooser.*;
import javax.swing.*;
import javax.swing.event.*;
//import javax.swing.preview.*;
import javax.swing.text.*;
import javax.swing.filechooser.*;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.*;
import java.util.Hashtable;



public class SwingInterface extends JFrame implements UserInterface,
  ActionListener, ChangeListener, DocumentListener {
  
    private JToolBar m_buttonbar,m_layoutbar;
    private JButton b_play,b_stop,b_fastfwd,b_relayout,b_seek;
    private JButton b_shake;
    private JLabel m_time;
    private JSlider m_timeinc;
    private JTextField m_tension,m_iterations;
    private SwingPanel m_drawpanel;
    private Animation m_animation;
    private SwingDebug m_debug;
  
    private JCheckBoxMenuItem c_debug;

    public SwingInterface() {

      // Set title and size

      super("Javis 0.2");
      setSize(Preferences.window_width,Preferences.window_height);


      // Build interface

      buildMenuBar();
      buildInterface();
      
      setVisible(true);


      // Create animation object

      m_animation=new Scheduler(this);
      m_drawpanel.attach(m_animation);

      m_animation.setTimeInc(0.001);


      if (Preferences.splash_delay>0) {

        // Display our nice splash screen

        // Wait a few milliseconds to make sure we get the right window 
	// position

	try {
	  Thread.sleep(300);
	} catch (InterruptedException e) {}


	// Start splashscreen thread
	
	Thread m_thread=new Thread(new SwingSplash(Preferences.splash_delay,
						   getLocationOnScreen()));
	m_thread.start();
	try {
	  m_thread.join();
	}
	catch (InterruptedException e) {}
      }

      m_debug=new SwingDebug(this);
      Debug.out.attach(m_debug);
    }


    public void setTimeBar(double time) {
      m_time.setText((new Double(time)).toString());
    }


    public void hideDebugConsole() {
      c_debug.setState(false);
      m_debug.hideme();
    }


    public void showDebugConsole() {
      c_debug.setState(true);
      m_debug.showme();
    }


    private void buildMenuBar() {

      JMenuBar m_bar=new JMenuBar();


      // "File" menu

      JMenu m_file=new JMenu("File");
      m_file.setMnemonic('F');
      JMenuItem m_open=new JMenuItem("Open...",new ImageIcon("resources/open.gif"));
      JMenuItem m_quit=new JMenuItem("Quit");
      m_open.setMnemonic('O');
      m_quit.setMnemonic('Q');
      m_open.addActionListener(this);
      m_quit.addActionListener(this);

      m_file.add(m_open);
      m_file.add(new JSeparator());
      m_file.add(m_quit);
      


      // "Options" menu

      JMenu m_options=new JMenu("Options");
      m_options.setMnemonic('O');

      JMenuItem m_pref=new JMenuItem("Preferences...");
      m_pref.setMnemonic('P');
      m_pref.addActionListener(this);
      m_options.add(m_pref);


      // "View" menu

      JMenu m_view=new JMenu("Window");
      m_view.setMnemonic('W');

      c_debug=new JCheckBoxMenuItem("Debug Console");

      c_debug.addActionListener(this);

      m_view.add(c_debug);


      // "Help" menu

      JMenu m_help=new JMenu("Help");

      JMenuItem m_about=new JMenuItem("About...");
      m_about.addActionListener(this);
      m_help.add(m_about);
      

      m_bar.add(m_file);
      m_bar.add(m_options);
      m_bar.add(m_view);
      m_bar.add(m_help);


      setJMenuBar(m_bar);
    }


    private void buildInterface() {

      getContentPane().setLayout(new BorderLayout());

      // Create components

      m_buttonbar=buildToolBar();

      m_layoutbar=buildLayoutBar();

      m_drawpanel=new SwingPanel();
      m_drawpanel.setBorder(BorderFactory.createLoweredBevelBorder());


      // Compose the whole screen: toolbars and drawing panel 

      getContentPane().add(m_buttonbar,BorderLayout.NORTH);
      getContentPane().add(m_layoutbar,BorderLayout.SOUTH);
      getContentPane().add(m_drawpanel,BorderLayout.CENTER);

      addWindowListener(new WindowAdapter() {
	public void windowClosing(WindowEvent we) {
	  dispose();
	  System.exit(0);
	}});
    }


    private JToolBar buildToolBar() {

      // Make a toolbar to add things to 

      JToolBar toolbar=new JToolBar();
      toolbar.setFloatable(false);
      toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));

      // Add buttons

      b_play=new JButton(new ImageIcon("resources/play.gif"));
      b_stop=new JButton(new ImageIcon("resources/stop.gif"));
      b_fastfwd=new JButton(new ImageIcon("resources/ff.gif"));
      b_seek=new JButton(new ImageIcon("resources/seek.gif"));
      b_seek.setEnabled(false);

      b_play.setToolTipText("Play forward");
      b_stop.setToolTipText("Stop playing");
      b_fastfwd.setToolTipText("Fast forward");
      b_seek.setToolTipText("Seek to next event");

      b_play.setMargin(new Insets(2,2,2,2));
      b_stop.setMargin(new Insets(2,2,2,2));
      b_fastfwd.setMargin(new Insets(2,2,2,2));
      b_seek.setMargin(new Insets(2,2,2,2));

      b_play.addActionListener(this);
      b_stop.addActionListener(this);
      b_fastfwd.addActionListener(this);
      b_seek.addActionListener(this);

      JPanel bpanel=new JPanel();
      bpanel.setLayout(new BoxLayout(bpanel,BoxLayout.X_AXIS));

      bpanel.add(b_stop);
      bpanel.add(b_play);
      //bpanel.add(b_fastfwd);
      bpanel.add(b_seek);

      bpanel.setBorder(BorderFactory.createEtchedBorder());


      // Time counter 

      JPanel timepanel=new JPanel();
      timepanel.setLayout(new BorderLayout());
      m_time=new JLabel("0.0000");
      m_time.setToolTipText("Current time");
      timepanel.add(m_time,BorderLayout.CENTER);
      timepanel.setBorder(BorderFactory.createCompoundBorder(
			    BorderFactory.createEtchedBorder(),
			    BorderFactory.createEmptyBorder(0,10,0,10)));


      // Time increment slider

      m_timeinc=new JSlider(JSlider.HORIZONTAL,100,700,400); 
      m_timeinc.setPaintTicks(true);
      m_timeinc.setMajorTickSpacing(100);
      m_timeinc.setMinorTickSpacing(50);
      m_timeinc.setPaintLabels(true);
      Hashtable slidevalues=new Hashtable();
      slidevalues.put(new Integer(100),new JLabel("1us"));
      slidevalues.put(new Integer(400),new JLabel(".1ms"));
      slidevalues.put(new Integer(700),new JLabel("1s"));
      m_timeinc.setLabelTable(slidevalues);
      m_timeinc.setBorder(BorderFactory.createEtchedBorder());
      m_timeinc.setToolTipText("Time increment");
      m_timeinc.addChangeListener(this);

      
      // Add everything to the toolbar

      toolbar.add(bpanel);

      timepanel.setPreferredSize(new Dimension(125,54));
      toolbar.add(timepanel);

      toolbar.add(m_timeinc);
      toolbar.add(Box.createGlue());

      return toolbar;
    }

  
    private JToolBar buildLayoutBar() {

      // Create a toolbar

      JToolBar toolbar=new JToolBar();
      toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
      toolbar.setFloatable(false);
      
      // Add autolayout picture

      toolbar.add(new JLabel(new ImageIcon("resources/autolayout.gif")));


      // Box of buttons

      JPanel layoutButtonBox=new JPanel();
      layoutButtonBox.setBorder(BorderFactory.createEtchedBorder());
      b_relayout=new JButton("Redo");
      b_shake=new JButton("Shake");
      b_relayout.setToolTipText("Repeat autolayout");
      b_shake.setToolTipText("Shake the network a bit");
      b_relayout.addActionListener(this);
      b_shake.addActionListener(this);
      layoutButtonBox.add(b_relayout);
      layoutButtonBox.add(b_shake);

      toolbar.add(layoutButtonBox);


      // Box of parameters (Force, tension)

      JPanel parameterBox=new JPanel();
      parameterBox.setBorder(BorderFactory.createEtchedBorder());

      m_tension=new JTextField("20",4);
      m_iterations=new JTextField("10",4);
      m_tension.getDocument().addDocumentListener(this);
      m_iterations.getDocument().addDocumentListener(this);
      m_tension.setToolTipText("Contracting force on edges");
      m_iterations.setToolTipText("Number of times to layout");

      parameterBox.add(new JLabel("Tension"));
      parameterBox.add(m_tension);
      parameterBox.add(new JLabel("Iterations"));
      parameterBox.add(m_iterations);
      toolbar.add(parameterBox);

      return toolbar;
    }


    public void stateChanged(ChangeEvent ce) {
      JSlider source=(JSlider)ce.getSource();

      if (source.getValueIsAdjusting()==false) {
	double value=(double)source.getValue();

	Debug.out.println("Slider value: "+value);
	/*
	if (value<=500000.0) {
	  value=(value*2.0)/1001.0+1.0;
	  value=value/1000000.0;
       }
	else {
	  value=value-500000.0;
	  value=(value*999.0)/500.0+1000.0;
	  value=value/1000000.0;
	  }*/

	value=value/100.0-1;
	value=Math.pow(10,value)/1000000.0;
	Debug.out.println("Increment: "+value*1000000.0+" usec");

        m_animation.setTimeInc(value);
       
      }
    }

    public void actionPerformed(ActionEvent ae) {

      if (ae.getSource()==b_play) {
	m_animation.startPlaying();

	b_seek.setEnabled(true);
	b_relayout.setEnabled(false);
	b_shake.setEnabled(false);

	return;
      }
      else
      if (ae.getSource()==b_relayout) {
	m_animation.reLayout();
      }
      if (ae.getSource()==b_shake) {
	m_animation.shake();
      }
      else
      if (ae.getSource()==b_seek) {
        m_animation.seekNext();
      }
      else
      if (ae.getSource()==b_stop) {

	m_animation.stopPlaying();

	b_seek.setEnabled(false);
	b_relayout.setEnabled(true);
	b_shake.setEnabled(true);

	return;
      }
      else
      if (ae.getSource()==c_debug) {
	if (c_debug.getState()==true)
	m_debug.showme();
	else
	m_debug.hideme();
      }
      else
      if (ae.getSource()==m_tension) {
	System.err.println("Testio!");
      }
      else
      if (ae.getActionCommand().equals("Quit")) {
	dispose();

	System.exit(0);
      }
      else
      if (ae.getActionCommand().equals("Preferences...")) {
	SwingPreferences sp=new SwingPreferences(this);
	sp.show();
      }
      else
      if (ae.getActionCommand().equals("About...")) {
	SwingAbout sa=new SwingAbout(this);
	sa.show();
      }
      else
      if (ae.getActionCommand().equals("Open...")) {
	JFileChooser jfc=new JFileChooser(".");
	
	class JavisFilter extends FileFilter {
	  public boolean accept(java.io.File f) {
	    return f.isDirectory() || f.getName().endsWith(".jvs") ||
	           f.getName().endsWith(".nam") || 
	           f.getName().endsWith(".jvs.gz") || 
	           f.getName().endsWith(".nam.gz");
	  }
	  public String getDescription() {
	    return "Javis/NAM trace file";
	  }
	}
	
	jfc.setFileFilter(new JavisFilter());
	int res=jfc.showOpenDialog(this);
	if (res==0 && jfc.getSelectedFile()!=null) {
	  m_animation.loadFile(jfc.getSelectedFile().getAbsolutePath());
	}

      }
    }


    public void changedUpdate(DocumentEvent e) {}

    public void insertUpdate(DocumentEvent e) {
      removeUpdate(e);
    }

    public void removeUpdate(DocumentEvent e) {

      // Tension parameter has been changed

      if (e.getDocument()==m_tension.getDocument()) {
	try {
	  String str=m_tension.getDocument().getText(0,
		       m_tension.getDocument().getLength());
	  int newtension=Integer.parseInt(str);

	  Preferences.force_tension=newtension;

	  Debug.out.println("New tension: "+newtension);
	}
	catch (Exception exc) {}
      }

      // Iterations parameter has been changed

      if (e.getDocument()==m_iterations.getDocument()) {
	try {
	  String str=m_iterations.getDocument().getText(0,
		       m_iterations.getDocument().getLength());
	  int newiter=Integer.parseInt(str);

	  Preferences.layout_iteration=newiter;

	  Debug.out.println("New iterations: "+newiter);
	}
	catch (Exception exc) {}
      }
    }

}

/*
class LookAndFeelListener implements ActionListener {

    private JFrame m_frame;

    public LookAndFeelListener(JFrame frame) {
      m_frame=frame;
    }


    public void actionPerformed(ActionEvent ae) {
     
      UIManager.LookAndFeelInfo[] lfInf=UIManager.getInstalledLookAndFeels();

      for (int i=0;i<lfInf.length;i++) {
	if (lfInf[i].getName().equals(ae.getActionCommand())) { 
	  try {
	    UIManager.setLookAndFeel(lfInf[i].getClassName());
	    SwingUtilities.updateComponentTreeUI(m_frame);
	    m_frame.invalidate();
	    m_frame.validate();
	    m_frame.repaint();
	  }
	  catch (UnsupportedLookAndFeelException e) {
	    String l_message="Look and feel \""+lfInf[i].getName()+
	                     "\" not installed.";
	    JOptionPane.showMessageDialog(m_frame,l_message,"Look and Feel "+
					  "error",JOptionPane.ERROR_MESSAGE);
	  }
	  catch (Exception e) {
	    JOptionPane.showMessageDialog(m_frame,"Could not change look and "+
					  "feel!","Look and Feel error",
					  JOptionPane.ERROR_MESSAGE);
	  }
	}
      }
    }

}
*/
