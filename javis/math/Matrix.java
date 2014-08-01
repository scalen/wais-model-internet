package math;

import math.Vector;

/**
   A mathematical matrix class. Member variables are public to guarantee fast
   access when it is needed.

   @author Christian Nentwich
   @author Steven Vischer
   @version 0.1 02/12/1998
*/

public class Matrix {

    /**
       The contents of the matrix.
    */
    public double m_values[][];

    /**
       Default constructor, initialises the matrix and sets it to the identity
       matrix
    */
    public Matrix() {
        m_values=new double[3][3];
        this.identity();
    }


    /**
       This function will clear the matrix (set all elements to 0)
    */
    public void clear() {
        int i,j;
        for(i=0; i<3; i++) {
            for(j=0; j<3; j++) {
                m_values[i][j]=0;
            }
        }
    }


    /**
       Function to set the matrix to identity, i.e. all elements 0 but elements
       on the diagonal are 1.
    */
    public void identity() {
        int i,j;
        for(i=0; i<3; i++) {

            for(j=0; j<3; j++) {
                if(i==j) m_values[i][j]=1;
                else m_values[i][j]=0;
            }
        }
    }


    /**
       Calculate the determinant of this matrix.
       @return the determinant
    */
    public double determinant() {
      return m_values[0][0]*m_values[1][1]*m_values[2][2]+
	     m_values[0][1]*m_values[1][2]*m_values[2][0]+
	     m_values[0][2]*m_values[1][0]*m_values[2][1]-
	     m_values[0][2]*m_values[1][1]*m_values[2][0]-
	     m_values[0][1]*m_values[1][0]*m_values[2][2]-
	     m_values[0][0]*m_values[1][2]*m_values[2][1];
    }


    /**
       Invert the matrix.
    */
    public void invert() {

    }


    /**
       Translate the matrix. This will add the arguments to the rightmost
       column of the matrix to translate it.

       @param dx the offset to translate horizontally
       @param dy the offset to translate vertically
    */
    public void translate(double dx,double dy) {
      m_values[0][2]+=dx;
      m_values[1][2]+=dy;
    }



    /**
       This will scale the matrix, i.e. multiply its diagonal by these two
       values.

       @param sx amount to scale in x direction
       @param sy amount to scale in y direction
     */
    public void scale(double sx,double sy) {
	m_values[0][0]*=sx;
	m_values[1][1]*=sy;
    }


    /**
       Rotate the matrix using a specified angle. This will create a rotation
       matrix from the angle and then multiply this matrix times the rotation
       matrix.

       @param angle the angle to rotate
       @see #getRotationMatrix
     */
    public void rotate(double angle) {

      Matrix r=getRotationMatrix(angle);

      this.mulMatrixPre(r);
    }


    /**
       This function will generate a rotation matrix from the specified angle
       and return it.

       @param angle the amount to rotate
       @return the rotation matrix
    */
    public static Matrix getRotationMatrix(double angle) {
      Matrix rot=new Matrix();

      rot.m_values[0][0]=Math.cos(angle);
      rot.m_values[0][1]=Math.sin(angle);
      rot.m_values[1][0]=-Math.sin(angle);
      rot.m_values[1][1]=Math.cos(angle);

      return rot;
    }


    /**
       Mutiply this matrix times another matrix and store the result in this
       matrix.

       @param other the matrix to multiply by
    */
    public void mulMatrixPre(Matrix other) {
        
        Matrix res=new Matrix();

	// Multiply matrices into new matrix

        for(int i=0; i<3; i++) {
            
            for(int j=0; j<3; j++) {

	        res.m_values[i][j]=0.0;
                
                for(int x=0; x<3; x++) 
                res.m_values[i][j]+=m_values[i][x]*other.m_values[x][j];
                    
            }
        }

	// Assign result to ourselves
	this.m_values=res.m_values;
    }


    /**
       Multiply another matrix by this matrix and store the result in this
       matrix.

       @param other the matrix to multiply by
    */
    public void mulMatrixPost(Matrix other) {
        
        Matrix res=new Matrix();

	// Multiply matrices into new matrix

        for(int i=0; i<3; i++) {
            
            for(int j=0; j<3; j++) {

	        res.m_values[i][j]=0.0;
                
                for(int x=0; x<3; x++) 
                res.m_values[i][j]+=other.m_values[i][x]*m_values[x][j];
                    
            }
        }

	// Assign result to ourselves
	this.m_values=res.m_values;        
    }


    /**
       Return a copy of this matrix
       @return the copied matrix
    */
    public Matrix copy() {
      Matrix res=new Matrix();

      for (int i=0;i<3;i++)
      for (int j=0;j<3;j++)
      res.m_values[i][j]=m_values[i][j];

      return res;
    }


    /**
       For debugging, support conversion to a string
    */
    public String toString() {
      
      return "("+m_values[0][0]+" "+m_values[0][1]+" "+m_values[0][2]+")\n"+
	     "("+m_values[1][0]+" "+m_values[1][1]+" "+m_values[1][2]+")\n"+
	     "("+m_values[2][0]+" "+m_values[2][1]+" "+m_values[2][2]+")\n";
    }
}









