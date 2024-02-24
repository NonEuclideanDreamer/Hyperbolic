
//******************************************************************
//Hyperbolic/Point.java
//@author Non-Euclidean Dreamer
//Represents one point in Hyperbolic Space with coordinates x,y,z of Minkowski Space.
//*****************************************************************
import java.awt.Color;
import java.util.Random;



public class Point
{
	final static double exactness=0.0005;
	final static int black=Color.black.getRGB();

	public double x;  
	public double y; 
	public double z; 
	public int color; // Determines the color of the point as an rgb-value

	public static boolean still; //if true, "isIn" checks whether the Point is visible for the Observer in the Image-class, else it checks if it's within Observer.range of zero
	
	//****************************************************
	// constants
	//****************************************************
	public static final Point zero=new Point(0,0,0);
	
	
	
	//******************************************
	//Constructor: Set null coordinates
	//******************************************
	public Point ()
	{
		x=0;
		y=0;
		z=0;
		color=black;
	}
	
	//******************************************
	//Constructor: Set chosen initial values, with default color black
	//******************************************
	public Point (double ix, double iy, double iz)
	{
		x=ix;
		y=iy;
		z=iz;
		color=black;
		}
	//******************************************
	//Constructor: Set chosen initial values, including color
	//******************************************
	public Point (double ix, double iy, double iz, int icolor)
	{
		x=ix;
		y=iy;
		z=iz;
		color=icolor;
	}
	
	//*******************************************
	//Random Point (not uniform)
	//*******************************************
	public static Point random()
	{
		Random rand=new Random();
		return new Point(Math.tan(rand.nextDouble(Math.PI)-Math.PI/2),Math.tan(rand.nextDouble(Math.PI)-Math.PI/2),Math.tan(rand.nextDouble(Math.PI)-Math.PI/2));
	}
	
	//******************************************
	// Returns the x-coordinate of this point
	//******************************************
	public double getX()
	{
		return x;
	}
	
	//******************************************
	// Returns the y-coordinate of this point
	//******************************************
	public double getY()
	{
		return y;
	}

	//******************************************
	// Returns the z-coordinate of this point
	//******************************************
	public double getZ()
	{
		return z;
	}
	
	//********************************************
	// Returns Color of the Point as an rgb-value
	//********************************************
	public int getColor() 
	{
		
		return color;
	}
	
	
	//******************************************
	// Sets the phi coordinate to chosen value,
	// allowing out-of-range numbers
	//******************************************
	public void setX(double newx)
	{
		x=newx;
	}
	
	//********************************************
	// Sets the r coordinate to chosen value
	//********************************************
	public void setY(double newy)
	{ 
		
			y=newy;
	}

	//*******************************************
	// Sets the theta coordinate to chosen value,
	// warning from out-of-range numbers
	//*******************************************
	public void setZ(double newZ)
	{ 
		z=newZ;
	}
	
	//*********************************************
	//Sets the given color
	//*********************************************
	public void setColor(int c)
	{
		color=c;
	}

	
	//*******************************************
	//Checks for equality with another point (not color)
	//*******************************************
	public boolean equals (Point point)
	{   
		if (Math.abs(x-point.getX())<=exactness)
		
		{
		 if (Math.abs(y-point.getY())<=exactness)
			{
				if (Math.abs(z-point.getZ())<=exactness)
				{
					return true;
				}
				
			}
		}
			return false;
	}
	
	
	//********************************************
	// Returns the distance to another point 
	//*********************************************
	public double distance(Point point)
	{	
		if (equals(point))
		{
			return 0;
		}
		else
		{
			Minkowski u=toMinkowski(),
					v=point.toMinkowski();
			return Minkowski.acosh(-u.hdot(v));
		}
	}


	
	//*******************************************
	// Returns the point in Minkowski SpaceTime
	//*******************************************
	public Minkowski toMinkowski()
	{
		
		Minkowski min=new Minkowski (x,y,z,Math.sqrt(1+x*x+y*y+z*z),color);
		return min;
	}
	
	//********************************************
	// Calculates the angle between the two points
	// from the point of view
	//********************************************
	public double angle(Point point1, Point point2)
	{
		double leg1, leg2, base;
		leg1=distance(point1);
		leg2=distance(point2);
		base=point1.distance(point2);
		if (leg1==0 || leg2==0 || (Double.isInfinite(leg1) && Double.isInfinite(leg2)))
		{
			return 0;
		}
		else
		return Math.acos((-Math.cosh(base)+Math.cosh(leg1)*Math.cosh(leg2))/Math.sinh(leg1)/Math.sinh(leg2));
	}

	//********************************************
	// Calculates the Geodesic to the point
	//********************************************
	public Line geodesic(Point destination)
	{
		Minkowski w= destination.toMinkowski(), v=toMinkowski();
		double dot=v.hdot(w),
				t=1/Math.sqrt(Math.pow(dot,2)-1),
				s=dot*t;
		Minkowski u=v.times(s).add(w.times(t));//System.out.println("direction norm="+u.hdot(u));
		Line geodesic=new Line(this.copy(),u,distance(destination));
		return geodesic;
	}
	
	//**********************************
	// middlepoint between this and that
	//**********************************
	public Point middle(Point that)
	{
		return geodesic(that).location(0.5);
	}
	
		
	//********************************************
	// Gives a point in the direction of the vector
	//********************************************
	public Point translate(Minkowski direction,double speed)
	{
		Line line=new Line(this,direction,speed);
		return line.location(1);
	}
	
	//*********************************************
	// Maps the point with a linear transformation in E4
	//*********************************************
	public Point transform(double[][] matrix)
	{
		Minkowski vector=toMinkowski();
		vector.transform(matrix);
		Point point=vector.toHyperbole();
		point.color=color;
		return point;
	}


	public Point copy()
	{
		Point point=new Point(getX(),getY(),getZ(),getColor());
		return point;
	}
	
	//******************************************
	// Prints the Coordinates of the point going to a new line after
	//******************************************
	public void println()
	{
		System.out.println("point: ("+x+", "+y+", "+z+")");
	}
	
	//******************************************
	// Prints the Coordinates of the point 
	//******************************************
	public void print()
	{
		System.out.print("point: ("+x+", "+y+", "+z+")");
	}
		
	
	//Gives the color as in "Imagecoordinates" as a Color object
	public Color standardcolor()
	{
		return new Color((int)((Math.atan(x)+Math.PI/2)*255.9/Math.PI),(int)((Math.atan(y)+Math.PI/2)*255.9/Math.PI),(int)((Math.atan(z)+Math.PI/2)*255.9/Math.PI));
	}
	
	//Gives the color as in "Imagecoordinates" as an rgb-array
	public double[] stcolor()
	{
		
		return new double[] {((Math.atan(x)+Math.PI/2)*255.9/Math.PI),((Math.atan(y)+Math.PI/2)*255.9/Math.PI),((Math.atan(z)+Math.PI/2)*255.9/Math.PI)};
	}
	
	//********************************************
	// Gives the direction to the given point
	//********************************************
	public Minkowski direction(Point point)
	{
		return geodesic(point).getMomentum();
	}
	
	//*********************************************************
	//which of the given points is next to the current location?
	//**********************************************************
	public int nextTo(Point[] point)
	{
			int k=0;
			double dist=distance(point[0]),dist2;
			for(int i=1;i<point.length;i++)
			{
				dist2=distance(point[i]);
				if(dist2<dist)
				{
					k=i;
					dist=dist2;
				}
			}
			return k;
	}
	
	//auxiliary methods
	
	//*****************************************************************
	//Gives a linear comb. of the colors, with a times the first color
	//******************************************************************
	public static Color combine(Color color1, Color color2, double a)
	{
		if((a>=1))return color1;
			
		//a=a-(int)a;
		int red, green, blue;
		red=(int) (color1.getRed()*a+color2.getRed()*(1-a));
		green=(int) (color1.getGreen()*a+color2.getGreen()*(1-a));
		blue=(int) (color1.getBlue()*a+color2.getBlue()*(1-a));
		try{Color color=new Color(red,green,blue);return color;}catch(java.lang.IllegalArgumentException e) {
			System.out.println("Error: a="+a);
		}
		return color2;
	}

	public int nextTo(Point[] point, int i, int[][] nb) 
	{
		int k=i;
		double dist=distance(point[i]),dist2;
		for(int j=0;j<nb[i].length;j++)
		{
			dist2=distance(point[nb[i][j]]);
			if(dist2<dist)
			{
				k=nb[i][j];
				dist=dist2;
			}
		}
		return k;
	}
	
	public  boolean isIn()
	{
		if(still) 
		{
			setColor(Image.color.getRGB());
			return Image.eye.drawPoint(Image.image, Image.zBuffer, this)<Observer.range;
		}
		else return distance(zero)<Polychoron.faintness;
	}

}


