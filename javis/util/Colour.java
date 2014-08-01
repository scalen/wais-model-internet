

package util;

import java.util.Hashtable;
import java.io.*;
import java.net.URL;

import util.Input;
import util.Debug;
import util.Preferences;

public class Colour {

    static Hashtable m_id_to_rgb=new Hashtable();
    static Hashtable m_name_to_rgb=null;

    java.awt.Color m_color;
    int m_rgb;


    public Colour() {
      m_rgb=0;
      if (m_name_to_rgb==null) initColours();
      m_color=new java.awt.Color(m_rgb);
    }


    public Colour(String name) {
      m_rgb=0;
      if (m_name_to_rgb==null) initColours();

      setColour(name);
    }


    public void setColour(String name) {
      Integer RGB=(Integer)m_name_to_rgb.get(name);
      if (RGB!=null)
      m_rgb=RGB.intValue();
      m_color=new java.awt.Color(m_rgb);
    }


    public void setColour(int id) {
      Integer RGB=(Integer)m_id_to_rgb.get(new Integer(id));
      if (RGB!=null)
      m_rgb=RGB.intValue();
      m_color=new java.awt.Color(m_rgb);
    }

    public void setColour (Colour cl) {
      m_rgb=cl.m_rgb;
      m_color=new java.awt.Color(m_rgb);
    }


    public int getColour() {
      return m_rgb;
    }

    public java.awt.Color getAWTColor() {
      return m_color;
    }

    public String getColourName() {
      return "foobar";
    }



    public static void parseNewColour(Reader reader) {
      int ch=0;
      int complete=0,id=0;
      String name="foobar";

      if (m_name_to_rgb==null) initColours();

      try {

	while (true) {
	  ch=Input.skipSpaces(reader);

	  if (ch==-1) return;
	  if (ch!='-') return;

	  ch=reader.read();

	  switch ((char)ch) {
	    case 'i': id=Input.getInteger(reader);
	              complete++;
		      break;

	    case 'n': name=Input.getString(reader);
	              name=name.toLowerCase();
	              complete++;	
		      break;
	  }

	  if (complete==2) break;
	}
      }
      catch(IOException e) {
	Debug.out.println("Error!");
      }

      if (complete==2) {
	Integer RGB=(Integer)m_name_to_rgb.get(name);
	int rgb;
	if (RGB==null) {
	  Debug.out.println("Warning: Colour not found in database: "+
			     name+"!");
	  rgb=0;
	}
	else
	rgb=RGB.intValue();

        m_id_to_rgb.put(new Integer(id),new Integer(rgb));
      }
    }

    private static void initColours() {
      m_name_to_rgb=new Hashtable();

      try {
	BufferedReader reader=null;

	if (Preferences.rgb_database.startsWith("http://")) {
	  URL url=new URL(Preferences.rgb_database);
	  reader=new BufferedReader(new InputStreamReader(url.openStream()));
	}
	else
	reader=new BufferedReader(new InputStreamReader(
		 new FileInputStream(Preferences.rgb_database)));

	String newline="";

	while (newline!=null) {

	  newline=reader.readLine();
	  if (newline==null) break;

	  // Strings containing the values for R,G,B, respectively. No harm
	  // intended with gstring :)

	  String rstring=newline.substring(0,3).trim();
	  String gstring=newline.substring(4,4+3).trim();
	  String bstring=newline.substring(8,8+3).trim();

	  try {
	    int r=Integer.parseInt(rstring);
	    int g=Integer.parseInt(gstring);
	    int b=Integer.parseInt(bstring);

	    String name=newline.substring(11,newline.length()).trim();
	    name=name.toLowerCase();

	    m_name_to_rgb.put(name,new Integer((r<<16)|(g<<8)|b));
	  }
	  catch (NumberFormatException e) { 
	    Debug.out.println("Could not translate colour: "+rstring+" "+
			      gstring+" "+bstring); 
	  }

	}
      }
      catch (IOException e) {
	Debug.out.println("Warning: An I/O error occured while reading "+
			  "resources/rgb.txt. Back to black.");
      }
						 
    }



}


