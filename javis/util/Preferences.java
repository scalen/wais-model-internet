
package util;

import java.io.*;

public class Preferences {

    // User Interface parameters
    public static int splash_delay = 2;

    public static int window_width = 640;
    public static int window_height = 500;

    public static int debug_width = 165;
    public static int debug_height = 250;
    public static int debug_update_timeout = 150;
    public static boolean debug_is_sticky = true;

    // Animation parameters
    public static int fps = 28;

    // Layout manager parameters
    public static int force_tension = 20;
    public static int layout_iteration = 10;
    public static int layout_initial = 20;


    /* ------------- UNSAVEABLE CONSTANTS. DO NOT SAVE! ------------- */

    public static String rgb_database = "resources/rgb.txt";

    /* ----------------------- THANKS A LOT :) ---------------------- */


    public static void load() {
      File prefFile = new File("javis.conf");
      BufferedReader prefReader = null;

      try {
	prefReader = new BufferedReader(new FileReader(prefFile));

	while (true) {
	  String line = prefReader.readLine();

	  // End of file
	  if (line == null) break;

	  line = line.trim();
	  if (line.startsWith("splash_delay"))
	  try{
	    splash_delay = Integer.parseInt(extractParameter(line));
	  }
	  catch(NumberFormatException e){}
	
	  if (line.startsWith("window_width"))
	  try{
	    window_width = Integer.parseInt(extractParameter(line)); 
	  }
	  catch(NumberFormatException e){}
	
	  if (line.startsWith("window_height"))
	  try{
	    window_height = Integer.parseInt(extractParameter(line)); 
	  }
	  catch(NumberFormatException e){}
	
	  if (line.startsWith("debug_width"))
	  try{
	    debug_width = Integer.parseInt(extractParameter(line)); 
	  }
	  catch(NumberFormatException e){}
	
	  if (line.startsWith("debug_height"))
	  try{
	    debug_height = Integer.parseInt(extractParameter(line)); 
	  }
	  catch(NumberFormatException e){}
	  
	  if (line.startsWith("debug_update_timeout"))
	  try{
	    debug_update_timeout = Integer.parseInt(extractParameter(line)); 
	  }
	  catch(NumberFormatException e){}

	  if (line.startsWith("debug_is_sticky"))
	  try{ 
	    if ((extractParameter(line).compareTo("true")==0))
	      debug_is_sticky = true;
	    else
	      debug_is_sticky = false;
	  }
	  catch(NumberFormatException e){} 
   
	  if (line.startsWith("fps"))
	  try{
	    fps = Integer.parseInt(extractParameter(line)); 
	  }
	  catch(NumberFormatException e){}

	  if (line.startsWith("force_tension"))
	  try{
	    force_tension = Integer.parseInt(extractParameter(line)); 
	  }
	  catch(NumberFormatException e){}

	  if (line.startsWith("layout_iteration"))
	  try{
	    layout_iteration = Integer.parseInt(extractParameter(line)); 
	  }
	  catch(NumberFormatException e){}

	  if (line.startsWith("layout_initial"))
	  try{
	    layout_initial = Integer.parseInt(extractParameter(line)); 
	  }
	  catch(NumberFormatException e){}
	}
  
	prefReader.close();
    
      }
      catch (FileNotFoundException e)
      {}
      catch(IOException e) {}
    }


    public static void save() {

      File prefFile = new File("javis.conf");
      BufferedWriter prefWriter = null;
      try {
	prefWriter = new BufferedWriter(new FileWriter(prefFile));
	prefWriter.write("splash_delay"+":"+splash_delay);
	prefWriter.newLine();
	prefWriter.write("window_width"+":"+window_width);
	prefWriter.newLine();
	prefWriter.write("window_height"+":"+window_height);
	prefWriter.newLine();
	prefWriter.write("debug_width"+":"+debug_width);
	prefWriter.newLine();
	prefWriter.write("debug_height"+":"+debug_height);
	prefWriter.newLine();
	prefWriter.write("debug_update_timeout"+":"+debug_update_timeout);
	prefWriter.newLine();
	prefWriter.write("debug_is_sticky"+":"+debug_is_sticky);
	prefWriter.newLine();
	prefWriter.write("fps"+":"+fps);
	prefWriter.newLine();
	prefWriter.write("force_tension"+":"+force_tension);
	prefWriter.newLine();
	prefWriter.write("layout_iteration"+":"+layout_iteration);
	prefWriter.newLine();
	prefWriter.write("layout_initial"+":"+layout_initial);
	prefWriter.newLine();
	prefWriter.newLine();
	prefWriter.close();
      }
      catch(FileNotFoundException e) {}
      catch(IOException e) {}
    }

    private static String extractParameter(String s){
      return ((s.substring(s.indexOf(":")+1)).trim());
    }
}
