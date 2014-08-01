
package iface;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
//import com.sun.java.swing.*;
import javax.swing.*;

public class SwingAbout extends JDialog implements ActionListener {

    private JButton b_ok;

    public SwingAbout(JFrame owner) {
      super(owner,"About Javis",true);
      
      setBounds(owner.getLocationOnScreen().x+owner.getSize().width/2-200/2,
                owner.getLocationOnScreen().y+owner.getSize().height/2-200/2,
                200,200);

      buildInterface();
    }


    private void buildInterface() {
      getContentPane().setLayout(new BorderLayout());

      JPanel textpanel=new JPanel();
      textpanel.setLayout(new FlowLayout(FlowLayout.CENTER));

      textpanel.add(new JLabel("Javis 0.2"));
      textpanel.add(new JLabel("Your favourite network"));
      textpanel.add(new JLabel("animator was written by"));
      textpanel.add(new JLabel("(c)1998 Denis Hanson"));
      textpanel.add(new JLabel("Christian Nentwich"));
      textpanel.add(new JLabel("Aleksandar Nikolic"));
      textpanel.add(new JLabel("Steven Vischer"));

      b_ok=new JButton("Ok");
      b_ok.addActionListener(this);
      getContentPane().add(b_ok,BorderLayout.SOUTH);
      getContentPane().add(textpanel,BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent ae) {
      this.setVisible(false);
      this.dispose();
    }

}
    


