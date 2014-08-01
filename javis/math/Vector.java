package math;


import math.Matrix;


/**
Vector implements a mathematical vector and functions to manipulate it. For
speed reasons, the coordinates are made public.

@author Christian Nentwich
@author Steven Vischer
@version 0.1 2/12/1998
*/

public class Vector {

    /**
       The array of 3 doubles that holds the coordinates
    */
    public double m_value[];


    /**
       Default constructor, initialise vector to (0,0), using clear()
       @see #clear
    */
    public Vector() {
        m_value=new double[3];
        this.clear();
    }


    public Vector(Vector other) {
	m_value=new double[3];
	for (int i=0;i<3;i++)
	m_value[i]=other.m_value[i];
    }

    /**
       Constructor to directly initialise the vector with two coordinates.
       @param x The x coordinate to be assigned to the vector
       @param y The y coordinate to be assigned to the vector
    */
    public Vector(double x,double y) {
        m_value[0] = x;
        m_value[1] = y;
        m_value[2] = 1;
    }


    /**
       Calculate the lenght of the vector.
       @return length of the vector
    */
    public double getLength() {
        return (Math.sqrt(m_value[0]*m_value[0] + m_value[1]*m_value[1]));
    }


    /**
       Normalise the vectors length to one.
    */
    public void normalise() {
        double size;
        size = this.getLength();
        if(size!=0) {
            m_value[0]=(m_value[0])/size;
            m_value[1]=(m_value[1])/size;
        }
        else m_value[0]=m_value[1]=0; 
    }


    /**
       Add another vector to this one.
       @param other The vector to be added
    */
    public void add(Vector other) {
        m_value[0]=m_value[0]+(other.m_value[0]);
        m_value[1]=m_value[1]+(other.m_value[1]);
    }


    /**
       Set the vector to (0,0)
    */
    public void clear() {
        for(int i=0; i<3; i++) {
            m_value[i] = 0;
        }
    }


    /**
	Translate thte vector by a given amount.
        @param dx amount to move in x direction
	@param dy amount to move in y direction
    */
    public void translate(double dx,double dy) {
	m_value[0]+=dx;
	m_value[1]+=dy;
    }


    /**
       Multiply by a matrix and store the result in here.
       @param m the matrix to multiply by
    */
    public void mulMatrix(Matrix m) {

      // TODO: Check for correctness, this is from the top of my mind (chris)
 
      double newvalues[]=new double[3];

      for (int i=0;i<3;i++) {
	newvalues[i]=0;

	for (int j=0;j<3;j++) 
	newvalues[i]+=m.m_values[i][j]*m_value[j];
      }
	
      // Assign new values
      m_value=newvalues;
    }


    public Vector copy() {
      Vector n=new Vector();
      for (int i=0;i<3;i++) n.m_value[i]=m_value[i];
      return n;
    }

    /**
       For debugging, support conversion to a string
    */
    public String toString() {
      
        return "("+m_value[0]+" "+m_value[1]+" "+m_value[2]+")";
    }
}

