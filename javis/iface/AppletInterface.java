

package iface;

import iface.UserInterface;
import iface.AppletPanel;
import animation.Animation;
import animation.Scheduler;
import util.Debug;
import util.Preferences;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;


public class AppletInterface extends Panel implements UserInterface,
                                                      ActionListener {
    private AppletPanel m_panel;
    private Animation m_animation;
    private Button b_play,b_stop,b_seek;
    private Button b_relayout,b_shake;
    private Panel m_buttonpanel;
    private Panel m_layoutpanel;
    private Dimension m_appletsize;
    private String m_filename;
    private Label m_time;
    private TextField m_timeinc;
    private TextField m_tension,m_iterations;


    public AppletInterface(Dimension appletsize,String filename) {

      m_appletsize=appletsize;

      buildInterface();

      m_animation=new Scheduler(this);
      m_animation.setTimeInc(0.001);
      m_panel.attach(m_animation);

      m_filename=filename;
    }


    public void setTimeBar(double time) {
      m_time.setText((new Double(time)).toString());

    }


    private void buildInterface() {

      setLayout(new BorderLayout());

      // Create toolbar to hold buttonpanel + time counter

      Panel toolbar=new Panel();
      toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));

      // Create button panel 

      m_buttonpanel=new Panel();
      m_buttonpanel.setLayout(new FlowLayout(FlowLayout.LEFT));

      // Create button panel buttons

      b_play=new Button("Play");
      b_stop=new Button("Stop");
      b_seek=new Button("Seek");
  
      b_play.addActionListener(this);
      b_stop.addActionListener(this);
      b_seek.addActionListener(this);

      m_buttonpanel.add(b_stop);
      m_buttonpanel.add(b_play);
      m_buttonpanel.add(b_seek);

      b_seek.setEnabled(false);

      toolbar.add(m_buttonpanel);

      m_time=new Label("-.-------");
      toolbar.add(m_time);

      m_timeinc=new TextField("0.001",8);
      m_timeinc.addActionListener(this);
      toolbar.add(new Label("Increment"));
      toolbar.add(m_timeinc);

      // Create layout panel buttons

      m_layoutpanel=new Panel();
      m_layoutpanel.setLayout(new FlowLayout(FlowLayout.LEFT));

      Label layoutlabel=new Label("Auto-Layout");
      b_relayout=new Button("Redo");
      b_shake=new Button("Shake");

      b_relayout.addActionListener(this);
      b_shake.addActionListener(this);

      Label tensionlabel=new Label("Tension");
      m_tension=new TextField(Integer.toString(Preferences.force_tension),3);
      m_tension.addActionListener(this);

      Label iterlabel=new Label("Iterations");
      m_iterations=new TextField(Integer.toString(Preferences.layout_iteration),3);
      m_iterations.addActionListener(this);

      m_layoutpanel.add(layoutlabel);
      m_layoutpanel.add(b_relayout);
      m_layoutpanel.add(b_shake);
      m_layoutpanel.add(tensionlabel);
      m_layoutpanel.add(m_tension);
      m_layoutpanel.add(iterlabel);
      m_layoutpanel.add(m_iterations);

      // Create display panel

      
      add(toolbar,BorderLayout.NORTH);
      add(m_layoutpanel,BorderLayout.SOUTH);

      m_panel=new AppletPanel(new Dimension(m_appletsize.width,
					    m_appletsize.height-
					    40-40));



      add(m_panel,BorderLayout.CENTER);
      m_panel.setSize(m_panel.getPreferredSize());
      Debug.out.println(m_panel.getPreferredSize());      
    }

    public void loadFile(String filename) {
      m_animation.loadFile(filename);
    }

    public void actionPerformed(ActionEvent ae) {

      if (ae.getSource()==b_play) {
	b_seek.setEnabled(true);
	b_relayout.setEnabled(false);
	b_shake.setEnabled(false);

	m_animation.startPlaying();
      }
      else
      if (ae.getSource()==b_stop) {
	b_seek.setEnabled(false);
	b_relayout.setEnabled(true);
	b_shake.setEnabled(true);

	m_animation.stopPlaying();
      }
      else
      if (ae.getSource()==b_seek) {
	m_animation.seekNext();
      }
      else
      if (ae.getSource()==b_relayout) {
	m_animation.reLayout();
      }
      else
      if (ae.getSource()==b_shake) {
	m_animation.shake();
      }
      else
      if (ae.getSource()==m_tension) {
	String str=m_tension.getText();
	int newtens=Integer.parseInt(str);

	Preferences.force_tension=newtens;

	Debug.out.println("New tension: "+newtens);
      }
      else
      if (ae.getSource()==m_iterations) {
	String str=m_iterations.getText();
	int newiter=Integer.parseInt(str);

	Preferences.layout_iteration=newiter;

	Debug.out.println("New iterations: "+newiter);
      }
      else
      if (ae.getSource()==m_timeinc) {
	String str=m_timeinc.getText();
	double newinc=Double.valueOf(str).doubleValue();

	m_animation.setTimeInc(newinc);

	Debug.out.println("New increment: "+newinc);
      }
    }
}
