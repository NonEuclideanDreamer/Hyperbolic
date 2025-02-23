//********************************************************************
// Hyperbole/Line.java
// @author Non-Euclidean Dreamer
// Represents one geodesic in H^3 by a Point a normalized Minkowski Vector as direction and a speed
//**********************************************************************
package Hyperbolic;

import java.awt.Color;
import java.util.Random;



public class Line 
{

	Point start;
	Minkowski direction;
	double[] color;
	double speed;
	

	//**************************************************
	// Constructor: Set null Geodesic
	//**************************************************
	public Line()
	{
		start=new Point();
		direction=new Minkowski();
		
		speed=0;
	}
		
	//****************************************************
	// Constructor: Set chosen values
	//****************************************************
	public Line(Point st, Minkowski dir, double sp) 
	{
		start=st;
		direction=dir;
		speed=sp;
		Color c=new Color(st.getColor());
		color=new double[] {c.getRed(),c.getGreen(),c.getBlue()};
		
	}
	
	//******************************************************
	// Returns a random geodesic ToDo: make more uniform
	//******************************************************
	public static Line random()
	{
		Line out=new Line();
		Random random=new Random();
		double phi=random.nextDouble()*2*Math.PI-Math.PI, r=Math.tan(random.nextDouble()*Math.PI/2),
				theta=Math.asin(random.nextDouble()*Math.PI-1);
		Point origin=new Point(phi,r,theta);
		out.setOrigin(origin);
		Minkowski momentum=Minkowski.random();
		momentum=origin.toMinkowski().normComp(momentum).normalize();
		out.direction=momentum;
		
		return out;
	}

	//*****************************************************
	// Returns the position at t=0
	//******************************************************
	public Point getOrigin()
	{
		return start;
	}
	
	
	//*****************************************************
	// Returns the direction at t=0
	//*****************************************************
	public Minkowski getMomentum()
	{
		return direction;
	}
	
	//********************************************************
	// Set s as origin, letting direction & color as is
	//********************************************************
	public void setOrigin(Point s)
	{
		start=s;
		//color=s.getColor();
	}
	
	//***************************************************************
	// Sets the direction to the given vector todo: make sure the vector is in th tangent space
	//***************************************************************
	public void setMomentum(Minkowski vector)
	{
		direction=vector;
	}
	
	//*******************************************************
	// Sets the (initial) Color of the Geodesic
	//*****************************************************
	public void setColor(Color color2)
	{
		start.setColor(color2.getRGB());
		setOrigin(start);
		color=new double[] {color2.getRed(),color2.getGreen(),color2.getBlue()};
	}
	
	//*******************************************************
	// Sets the (initial) Color of the Geodesic
	//*****************************************************
	public void setColor(double[] color1, int color2)
	{
		start.setColor(color2);
		setOrigin(start);
		color=color1;
	}
	
	//************************************************************************* unfinished
	//gives the midpoint of the line chain with given lengths that gets closest to given point, starting at the origin and ending at position(1)
	//*************************************************************************
	/*public Spherpoint(double l1,double l2, Spherpoint direction)
	{
		double d=vector0.norm();
		double x=Math.atan((Math.cos(l2)-Math.cos(d)*Math.cos(l1))/(Math.cos(l1)*Math.sin(d)));
		Spherpoint p;
	}*/
	

	
	//****************************************************
	// Gets the location at time t 
	//****************************************************
	public Point location(double t)
	{
		//System.out.println("direction of line has length"+direction.hdot(direction));
		Minkowski origin=start.toMinkowski();
		double speed=speed();
		Minkowski Eloc=origin.times(Math.cosh(speed*t));
		Eloc=Eloc.add(direction.times(Math.sinh(speed*t)));
		Point location= Eloc.toHyperbole();
		
		//System.out.println("speed*t="+speed*t+"= dist="+start.distance(location));
		location.setColor(start.getColor());
		return location;
	}
	
	//****************************************************
	// Gets the speed of the geodesic
	//****************************************************
	public double speed()
	{
		return speed;
	}
	
	//******************************************************
	// Gets the direction at time t
	//******************************************************
	public Minkowski momentum(double t)
	{
		return transport(t).getMomentum();
	}
	

	
	//*******************************************************
	// Calculates the angle between this geodesic and the one from the Origin to the given point
	//*******************************************************
	public double angle(Point point)
	{
	
		double out= start.angle(location(1), point);
		return out;
	}

	//*******************************************************
	//transforms the line by the given matrix
	//*******************************************************
	public Line transform(double[][] transformation)
	{
		Point origin=(start).transform(transformation);
		Minkowski momentum=direction.copy();
		momentum.transform(transformation);
		return new Line(origin,momentum,speed);
	}
	
	//******************************************************
	// Calculates the angle between this geodesic and the one with the same origin and the given momentum
	//******************************************************
	public double angle(Minkowski momentum)
	{
		return start.angle(location(1), start.translate(momentum,1));
	}
	
	
	//************************
	//Copies start & direction
	//************************
	public Line copy()
	{
		Line line=new Line(new Point(getOrigin().getX(),getOrigin().getY(),getOrigin().getZ()),getMomentum().copy(),speed);
		line.setColor(color,start.getColor());
		return line;
	}
	
	//*******************************************************
	// Same geodesic, same speed but different start
	//*******************************************************
	public Line transport(double time)
	{
		Point point=location(time),
				destination=location(time+1);
		return point.geodesic(destination);
	}
	
	//*******************************************************
	// Transports the given line with coinciding origin along this line by time t
	//*******************************************************
	public Line transport(Line line, double t)
	{
		Line out=new Line(location(t), line.getMomentum().transport(t, this),line.speed);
		return out;
	}
	
	//********************************************************
	// Returns the color array
	//*******************************************************
	public double[] getColor() 
	{
		return color;
	}

	public void setSpeed(double sp) 
	{
		speed=sp;
	}
	
	
	
}
