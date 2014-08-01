

package iface;

//import com.sun.java.swing.*;
//import com.sun.java.swing.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import util.Preferences;



public class SwingPreferences extends JDialog implements ActionListener {

    private JButton b_ok,b_cancel;
    private JTextField t_fps,t_splashdelay,t_initial;
    private JCheckBox c_splashoff;

    public SwingPreferences(JFrame owner) {

      // Open dialog an centre in application frame

      super(owner,"Preferences",true);
      setBounds(owner.getLocationOnScreen().x+owner.getSize().width/2-450/2,
		owner.getLocationOnScreen().y+owner.getSize().height/2-400/2,
		450,200);

      buildInterface();

    }

  
    private void buildInterface() {

      // Create edit panel

      JTabbedPane editpanel=new JTabbedPane();

      // Create animation dialog
      JPanel animation_panel=new JPanel();
      animation_panel.setLayout(new FlowLayout(FlowLayout.LEFT));

      JPanel fpspanel=new JPanel();
      fpspanel.setBorder(BorderFactory.createEtchedBorder());
      JLabel l_fps=new JLabel("Frame rate");
      t_fps=new JTextField((new Integer(1000/Preferences.fps)).toString(),4);
      fpspanel.add(l_fps);
      fpspanel.add(t_fps);

      JPanel alayout_panel=new JPanel();
      alayout_panel.setBorder(BorderFactory.createEtchedBorder());
      alayout_panel.add(new JLabel("Initial Autolayout Iterations"));
      t_initial=new JTextField(new Integer(Preferences.layout_initial).toString(),4);
      alayout_panel.add(t_initial);

      animation_panel.add(fpspanel);
      animation_panel.add(alayout_panel);
      animation_panel.setBorder(BorderFactory.createTitledBorder("Animation Settings"));

      // Create interface dialog
      JPanel iface_panel=new JPanel();
      iface_panel.setLayout(new FlowLayout(FlowLayout.LEFT));

      JPanel splashpanel=new JPanel();
      JLabel l_delay=new JLabel("Intro Screen Delay");
      t_splashdelay=new JTextField((new Integer(Preferences.splash_delay).
				    toString()),4);

      splashpanel.add(l_delay);
      splashpanel.add(t_splashdelay);
      splashpanel.setBorder(BorderFactory.createEtchedBorder());

      iface_panel.add(splashpanel);
      iface_panel.setBorder(BorderFactory.createTitledBorder("User Interface Settings"));



      // Add everything to tabbed pane

      editpanel.addTab("Animation",new ImageIcon("resources/animation_icon.gif"),animation_panel);
      editpanel.addTab("Interface",new ImageIcon("resources/interface_icon.gif"),iface_panel);


      
      // Create button panel

      JPanel buttonpanel=new JPanel();

      b_ok=new JButton("Ok");
      b_cancel=new JButton("Cancel");
      b_ok.addActionListener(this);
      b_cancel.addActionListener(this);

      buttonpanel.add(b_ok);
      buttonpanel.add(b_cancel);

      // Add everything to the dialog

      getContentPane().setLayout(new BorderLayout());
      getContentPane().add(editpanel,BorderLayout.CENTER);
      getContentPane().add(buttonpanel,BorderLayout.SOUTH);
    }


    public void actionPerformed(ActionEvent ae) {

      if (ae.getSource()==b_ok) {

	try {
	  String str=t_initial.getDocument().getText(0,
		     t_initial.getDocument().getLength());
	  Preferences.layout_initial=Integer.parseInt(str);
	}
	catch (Exception e) {}

	try {
	  String str=t_fps.getDocument().getText(0,
		     t_fps.getDocument().getLength());
	  Preferences.fps=1000/Integer.parseInt(str);
	}
	catch (Exception e) {}

	try {
	  String str=t_splashdelay.getDocument().getText(0,
		     t_splashdelay.getDocument().getLength());
	  Preferences.splash_delay=Integer.parseInt(str);
	}
	catch (Exception e) {}

	this.setVisible(false);
	this.dispose();
      }
      else if (ae.getSource()==b_cancel) {
	this.setVisible(false);
	this.dispose();
      }

    }

}







