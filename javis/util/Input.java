
package util;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.IOException;

import util.Debug;


public class Input{

    public static StringBuffer getLine(BufferedReader reader) 
      throws IOException {

      String s=reader.readLine();
      
      if (s==null)
      return new StringBuffer();

      StringBuffer sb=new StringBuffer(s);
      sb.append('\n');
      return sb;
    
    }
  /*    public static StringBuffer getLine (BufferedReader reader) 
      throws IOException {

      StringBuffer s=new StringBuffer();
      int ch=' ';
      boolean first=true;

      while (true) {
	ch=reader.read();

      	if ((char)ch=='\n' || ch==-1)
        break;

	first=false;

	s.append((char)ch);
      }

      if (!first || ch!=-1)
      s.append('\n');
      
      return s;
    }
  */
    static public int skipSpaces (Reader reader) {
        int ch=' ';
        try {
                ch=reader.read();
                while ((char) ch == ' ') ch=reader.read();
        } 
        
        catch(IOException e) {
            Debug.out.println("Failure");
        }
        return ch;
    }



    static public int getInteger(Reader reader) {
        
        int ch=' ';
        String digits= new String();
        
        try {
            
            ch= skipSpaces(reader);
            
            while((char)ch!=' ' && (char)ch!='\n' && ch!=-1) {
                digits+=(char)ch;
                ch=reader.read();
            }

	    try {
	      ch=Integer.parseInt(digits);   
	    } 
	    catch (NumberFormatException e) {
	      Debug.out.println("Warning, could not convert "+digits+
				 " to a number!");
	      ch=0;
	    }

        }
        catch(IOException e) {
            Debug.out.println("Failure");
        }

        return ch;
    }



    static public double getDouble(Reader reader) {
        
        int ch=' ';
        String digits= new String();
        double res=0.0;

        try {
            
            ch= skipSpaces(reader);
            
            while((char)ch!=' ' && (char)ch!='\n' && ch!=-1) {
                digits+=(char)ch;
                ch=reader.read();
            }
            res=Double.valueOf(digits).doubleValue();
        }
        catch(IOException e) {
            Debug.out.println("Failure");
        }

        return res;
    }


    static public String getString(Reader reader) {
        
        String str=new String();
        int ch=' ';
        try {
            ch= skipSpaces(reader);
            while ((char)ch!=' ' && (char)ch!='\n' && ch!=-1) {
                str+=(char)ch;
                ch=reader.read();
            }
        }
        catch (IOException e) {
            Debug.out.println("Failure");
        }
        
        return str;
    }
}



