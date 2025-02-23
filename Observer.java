import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

//******************************************************************
// Observer.java
// @author Non-Euclidean Dreamer
// Represents an observer in Hyperbolic Space with the geodesic as the viewing direction,
// *right as the right border of the image and a,b, width and height of the image.
//********************************************************************

public class Observer
{
	Line viewPoint; //The Point of view, including the direction
	public Minkowski right; //marks which direction is right in the picture. (Should not be parallel to viewPoint)
	Line rightward; //Geodesic from viewPoint to the right(,up)
	public int height, //Height of the picture in pixels
		 width; //Width of the picture in pixels
	public double angleRange; //How far can one see
	public static double range=5;
	public double screenDistance;//how far in front of the observer is the screen
	public static double dotsize=5; 
	public double exactness=0.5*dotsize/width, shield=0.1;
	public static String format="png";
	public static boolean flatscreen=true; //if false, the screen is cylindrical, bent left-right
	
	public static Color background;
	

	
	//*********************************************************************
	// Constructor: Random observer, image data given
	//*********************************************************************
	public Observer(int width0, int height0, double angleRange0)
	{
		
		Random random=new Random();
		double  x=random.nextDouble()-1/2, y=random.nextDouble()-1/2,
				z=random.nextDouble()-1/2, w=random.nextDouble()-1/2, xr=random.nextDouble()-1/2, yr=random.nextDouble()-1/2,
				zr=random.nextDouble()-1/2, wr=random.nextDouble()-1/2;
		Point origin=Point.random();
			
		Minkowski momentum=new Minkowski(x,y,z,w);
		momentum=origin.toMinkowski().normComp(momentum).normalize();
		viewPoint=new Line(origin,momentum,1);
		right=new Minkowski(xr,yr,zr,wr);
		right=viewPoint.getOrigin().toMinkowski().normComp(right);//Assure thet right is in the tangential space
		right=viewPoint.getMomentum().normComp(right);//Assure right is normal to viewingdirection
		right=right.normalize();
		rightward=new Line(viewPoint.getOrigin(),right,1);
		height=height0;
		width=width0;
		angleRange=angleRange0;
		screenDistance=((double) width)/2/Math.tan(angleRange/2);
		if(!flatscreen) screenDistance=( height)/2.0/Math.tan(angleRange/2/width*height);
		System.out.println("screenDistance="+screenDistance);
		exactness=0.5*dotsize/width;
	}
	
	//*************************************************************************
	//Get the rightward geodesic
	//************************************************************
	public Line getRightward()
	{
		return rightward;
	}
	
	//*****************************************
	// Where is the observer?
	//*************************************
	public Point position()
	{
		return viewPoint.start;
	}
	//*******************$***$
	// watching direction
	//*********************************
	public Minkowski direction()
	{
		return viewPoint.direction;
	}
	
	public Line getForward()
	{
		return viewPoint;
	}

	
	//*************************************************************************
	//Get the upward geodesic or downward
	//*************************************************************************
	public Line getUpward()
	{
		double[][] rot=Minkowski.rotationmatrix(position().toMinkowski(),direction(), Math.PI/2);
		Minkowski up=right.copy().times(-1);up.transform(rot);
		return new Line(viewPoint.getOrigin(),up,1);
	}
	
	//*********************************************************************
	// Constructor: Sets the given values
	//*********************************************************************
	public Observer(Line pov, Minkowski right0,  int width0,int height0, double angle)
	{
		viewPoint=pov;
		right=viewPoint.getMomentum().normComp(right0);
		right=viewPoint.getOrigin().toMinkowski().normComp(right);
		right.normalize();
		rightward=new Line(viewPoint.getOrigin(),right,1);
		height=height0;
		width=width0;
		angleRange=angle;
		screenDistance=((double) width)/2/Math.tan(angleRange/2);
		if(!flatscreen)
			screenDistance=( height)/2.0/Math.tan(angleRange/2/width*height);
		exactness=0.5*dotsize/width;
	}

	//************************************************************************
	// Set up zBuffer
	//************************************************************************
	public double[][] InitiateBuffer()
	{
		double[][] zBuffer=new double[height][width];
		for (int i=0; i<width; i++)
		{
			for (int j=0; j<height; j++)
				zBuffer[j][i]=range;
		}
		return zBuffer;	
	}
	
	//*********************************************************************
	// Make a copy
	//*********************************************************************
	public Observer copy()
	{
		return new Observer(viewPoint.copy(),right.copy(),width,height, angleRange);
	
	}
	
	//***********************************************
	//Print location & Focus
	//***********************************************
	public void println()
	{
		Point focus=focus(), origin=viewPoint.getOrigin();
		System.out.println("observer ("+(origin.getX())+", "+
				(origin.getY())+", "+
				(origin.getZ())+") looking at ("+(focus.getX())+", "
				+(focus.getY())+", "+(focus.getZ())+")");
	}
	
	//*********************************************************************
	// Set up the graphic
	//*********************************************************************
	public BufferedImage setup(int background)
	{
		BufferedImage image= new BufferedImage(width,height,BufferedImage.TYPE_3BYTE_BGR);
		for (int xPixel=0; xPixel<width; xPixel++)
		{
			for (int yPixel=0; yPixel<height;yPixel++)
			{
				image.setRGB(xPixel,yPixel,background);
			}
		}
		
		println();
		
		return image;
	}
	
	//*********************************************************************
	// Draws a point, keeping track of the pixel 
	//*********************************************************************
	public double drawPoint(BufferedImage image, double[][] zBuffer, Point point)
	{	
		double depth=0, radial, alpha;// depth into the image; radial and alpha the spherical coordinates of the direction

			
		if (point.equals(position()))
		{
			for (int i=0; i<width; i++)
			{
				for (int j=0; j<height; j++)
				{
					if (zBuffer[j][i]>2*Math.PI)
					{
						zBuffer[j][i]=2*Math.PI;
						image.setRGB(i,j,point.getColor());
					}
				}
			}
			return 0;
		}

			
		if(flatscreen) 
		{
			int xPixel, yPixel;
			Minkowski direction=position().direction(point);
			radial=Math.abs(getForward().angle(point));
			depth=point.distance(position());
			
			if (radial>Math.PI/2)
			{
				return range;
			}
			alpha=Math.signum(position().toMinkowski().det(direction(), right, direction))*getRightward().angle(direction.hnormcomp(getForward()).hnormalize()); //think about sign!
			xPixel=width/2+(int) Math.round((Math.cos(alpha)*screenDistance*Math.tan(radial)));
			yPixel=height/2-(int) Math.round((Math.sin(alpha)*screenDistance*Math.tan(radial)));
			double pointradius=(Math.abs(dotsize/Math.sinh(depth)))+0.1;
		//	System.out.println("depth="+depth+", angle="+alpha+", radial="+radial+", pointradius="+pointradius);
		//		System.out.println("Point at "+xPixel+","+yPixel);
			int color=combine( point.getColor(),background,depth);
			if(yPixel!=height/2)// || xPixel!=width/2)
			for(int i=yPixel-(int)pointradius;i<yPixel+pointradius;i++)
			{
				double r= Math.sqrt(Math.pow(pointradius,2)-Math.pow(yPixel-i,2));
				
				for(int j=xPixel-(int)r;j<xPixel+r;j++)
			//Check if the point is inside the image
					if (i>=0 && j>=0 && i<height && j<width)
					{
		
						// Check if the pixel is already used closer
						
						if(zBuffer[i][j]>depth)
						{
							zBuffer[i][j]=depth;
							// Draw the pixel
							image.setRGB(j,i, color);
						}
					}
			}
			if (xPixel>=0 && yPixel>=0 && xPixel<height && yPixel<width)
				return depth;
			else return range;
		}
		else //not debugged the refactor yet...
		{
			int xPixel, yPixel, pi=(int)(width/angleRange*Math.PI);
			Minkowski direction=position().direction(point);
			Minkowski horComp=direction().paraComp(direction).add(right.paraComp(direction));
			Line eyeline=position().geodesic(point);	
			double hangle=Math.signum(Math.cos(getRightward().angle(horComp)))*getForward().angle(horComp),
					vangle=Math.signum(position().toMinkowski().det(direction(), right, direction))*eyeline.angle(horComp);
				
			depth=point.distance(position());
				
			xPixel=(int)Math.round(hangle/angleRange*width+width/2);
			yPixel=height/2-(int)Math.round(screenDistance*Math.tan(vangle));
			int altX=xPixel+pi;if(altX>width)altX=xPixel-pi;
			
			double pointradius=(int)(Math.abs(dotsize/Math.sin(depth)));
			int color=point.getColor();
			for(int i=yPixel-(int)pointradius;i<yPixel+pointradius;i++)
			{
				double r= Math.sqrt(Math.pow(pointradius,2)-Math.pow(yPixel-i,2));int k;
				
				for(int j=xPixel-(int)r;j<xPixel+r;j++)	
				//Check if the point is inside the image
				{		
					if(j>width-1)k=j-2*pi;else if(j<0)k=j+2*pi;else k=j;
					
					if (i>=0 && k>=0 && i<height && k<width)
					{
			
						// Check if the pixel is already used closer
						if(zBuffer[i][k]>depth)//test whether that hits the right Pixel!
						{
							zBuffer[i][k]=depth;
							// Draw the pixel
							image.setRGB(k,i,color);
						}
					}
				}
			}
			depth=2*Math.PI-depth;
				
			for(int i=-yPixel-(int)pointradius;i<-yPixel+pointradius;i++)
			{
				double r= Math.sqrt(Math.pow(pointradius,2)-Math.pow(yPixel-i,2));int k;
				
				for(int j=altX-(int)r;j<altX+r;j++)
				{
					if(j>width-1)k=j-2*pi;else if(j<0)k=j+2*pi;else k=j;//Check if the point is inside the image
					
					if (i>=0 && k>=0 && i<height && k<width)
					{
			
						// Check if the pixel is already used closer
				
						if(zBuffer[i][k]>depth)//test whether that hits the right Pixel!
						{
							zBuffer[i][k]=depth;
							// Draw the pixel
							image.setRGB(k,i,color);
						}
					}
				}
			}
		}
		return depth;
	}
	private int combine(int color, Color b, double depth) 
	{
		double ratio=depth/range;
		if(ratio>1)return b.getRGB();
		Color c=new Color(color);
	//	System.out.println("range="+range+", color("+(int)(c.getRed()*(1-ratio)+b.getRed()*ratio)+","+(int)(c.getGreen()*(1-ratio)+b.getGreen()*ratio)+","+(int) (c.getBlue()*(1-ratio)+b.getBlue()*ratio)+")");
		return new Color((int)(c.getRed()*(1-ratio)+b.getRed()*ratio),(int)(c.getGreen()*(1-ratio)+b.getGreen()*ratio),(int) (c.getBlue()*(1-ratio)+b.getBlue()*ratio)).getRGB();
	}

	//*********************************************************************
	// NOT REFACTORED YET Draws a point, keeping track of the pixel, in finite-speed universe, light traveled >revolutions*PI
	//*********************************************************************
		public void drawPoint(BufferedImage image, double[][] zBuffer, Point point, double d, Color background)
		{	
			double depth=0, radial, alpha,// depth into the image; radial and alpha the spherical coordinates of the direction

			reldepth=point.distance(position());depth=d;
			if(depth>range)return;
			if(flatscreen) 
			{
				
				int xPixel, yPixel;
				Minkowski direction=position().direction(point);
				radial=Math.abs(getForward().angle(point));
				
				//double reldepth=Math.abs(depth%Math.PI);
				if(reldepth<2*exactness||Math.PI-reldepth<2*exactness)//Point is here or antipodal->color everything
				{
					int color=Point.combine(new Color(point.getColor()), background, 1-depth/range).getRGB();
					for (int i=0; i<width; i++)
					{
						for (int j=0; j<height; j++)
						{
							if (zBuffer[j][i]>depth)
							{
								zBuffer[j][i]=depth;
								image.setRGB(i,j,color);
							}
						}
					}
					return;
				}
				if (radial>Math.PI/2)//peripheral
				{
					return;
				}
				alpha=Math.signum(position().toMinkowski().det(direction(), right, direction))*getRightward().angle(direction().normComp(direction)); //think about sign!
				xPixel=width/2+(int) Math.round((Math.cos(alpha)*screenDistance*Math.tan(radial)));
				yPixel=height/2-(int) Math.round((Math.sin(alpha)*screenDistance*Math.tan(radial)));
				double pointradius=(Math.abs(dotsize/Math.sin(reldepth)));
				
//	System.out.println("depth="+depth%Math.PI+", reldepth="+reldepth);
	
				int color=(Point.combine(new Color(point.color), background, 1-depth/range)).getRGB();

				for(int i=yPixel-(int)pointradius;i<yPixel+pointradius;i++)
				{
					double r= Math.sqrt(Math.pow(pointradius,2)-Math.pow(yPixel-i,2));
					
					for(int j=xPixel-(int)r;j<xPixel+r;j++)
				//Check if the point is inside the image
						if (i>=0 && j>=0 && i<height && j<width)
						{
			
							// Check if the pixel is already used closer
							
							if(zBuffer[i][j]>depth)//test whether that hits the right Pixel!
							{
								zBuffer[i][j]=depth;
								// Draw the pixel
								image.setRGB(j,i,color);
							}
						}
				}
			}
			else
			{
				int xPixel, yPixel, pi=(int)(width/angleRange*Math.PI);
				Minkowski direction=position().direction(point);
				Minkowski horComp=direction().paraComp(direction).add(right.paraComp(direction));
					Line eyeline=position().geodesic(point);
				double hangle=Math.signum(Math.cos(getRightward().angle(horComp)))*getForward().angle(horComp),
						vangle=Math.signum(position().toMinkowski().det(direction(), right, direction))*eyeline.angle(horComp);
				//	System.out.println("vangle="+vangle);
				depth=point.distance(position())+d*Math.PI;
				if(depth%Math.PI<5*exactness||depth%Math.PI>Math.PI-5*exactness)//Points blocking the view
				{
					int color=Point.combine(new Color(point.getColor()), background, 1-depth/range).getRGB();
					for (int i=0; i<width; i++)
					{
						for (int j=0; j<height; j++)
						{
							if (zBuffer[j][i]>depth)
							{
								zBuffer[j][i]=depth;
								image.setRGB(i,j,color);
							}
						}
					}
					return;
				}
				//	System.out.println(range);
				xPixel=(int)Math.round(hangle/angleRange*width+width/2);
				yPixel=height/2-(int)Math.round(screenDistance*Math.tan(vangle));
			//	System.out.println("yPixel="+yPixel+", screendistance="+screenDistance);
				int altX=xPixel+pi;if(altX>width)altX=xPixel-pi;
				
				double pointradius=(Math.abs(dotsize/Math.sin(depth)));
				int color=Point.combine(new Color (point.getColor()),background, Math.pow(255, -depth/range)).getRGB();
				for(int i=yPixel-(int)pointradius;i<yPixel+pointradius;i++)
				{
					double r= Math.sqrt(Math.pow(pointradius,2)-Math.pow(yPixel-i,2));int k;
					
					for(int j=xPixel-(int)r;j<xPixel+r;j++)	
					//Check if the point is inside the image
					{		
						if(j>width-1)k=j-2*pi;else if(j<0)k=j+2*pi;else k=j;
						
						if (i>=0 && k>=0 && i<height && k<width)
						{
				
							// Check if the pixel is already used closer
							if(zBuffer[i][k]>depth)//test whether that hits the right Pixel!
							{
								zBuffer[i][k]=depth;
								// Draw the pixel
								image.setRGB(k,i,color);
							}
						}
					}
				}
			}
			
		}
	//*********************************************************************
	// Draws a Geodesic up to t0, keeping track of the pixels
	//*********************************************************************
	public void drawLine(BufferedImage image, double[][] zBuffer, Line line, double t0)
	{
		double speed=line.speed();
		double t=0;
				int c=line.getOrigin().getColor();
		while( t<=t0)
		{
			Point location=line.location(t);
			location.setColor(c);
			drawPoint(image,zBuffer,location);
			t=t+exactness/speed;
		}
			
	}
		
	//*********************************************************************
	// NOT REFACTORED Draws a filled circle given by center, normal vector and radius
	//*********************************************************************
	public double fullCircle(BufferedImage image, double[][] zBuffer, Point center, Minkowski normal, double radius)
	{
		drawPoint(image,zBuffer,center);
		Minkowski central=center.toMinkowski();
		Minkowski normal0=central.normComp(normal);
		normal0=normal0.normalize();
		Minkowski right0=central.normComp(right);//random starting direction for drawing(in the circleplane)
		right0=normal0.normComp(right0);
		right0=right0.normalize();
		double dangle=exactness/Math.sin(radius);
		double[][] rot=Minkowski.rotationmatrix(central, normal0, dangle);
		double angle;
		for ( int k=3;k<100;k++)
		{
			angle=radius/k;
			Minkowski vector=right0.times(Math.sin(angle)).add(central.times(Math.cos(angle)));
			Point point=vector.toHyperbole();
			point.setColor(center.getColor());
			drawPoint(image, zBuffer, point);
		
			for(int i=1;i<2*Math.PI/dangle;i++)
			{
				vector.transform(rot);
				point=vector.toHyperbole();
				point.setColor(center.getColor());
				drawPoint(image, zBuffer, point);
			}
			angle=angle*1.2;
		}
		Minkowski vector=right0.times(Math.sin(0)).add(central.times(Math.cos(0)));
		Point point=vector.toHyperbole();
		point.setColor(center.getColor());
		double depth=drawPoint(image, zBuffer, point);
		return depth;
	}
		
	//*********************************************************************
	// NOT REFACTORED Draws a Sphere with the appointed Center and radius (Should be smaller PI/2) //In Progress!
	//*********************************************************************
	public double drawSphere(BufferedImage image, double[][] zBuffer, Point center, double radius, double perforation)
	{
		Minkowski vector=center.direction(position());
		if (radius>=center.distance(position()))
		{
			vector=vector.times(-1);//Assures the vector shows to the center of the Sphere, that is actually seen
		}
		if (getForward().angle(center)>Math.PI/2)
		{
			vector=vector.times(-1);
		}
		Minkowski central=center.toMinkowski();
		double[][] rot=Minkowski.rotationmatrix(vector, central, perforation);//rotates around the vector pointing to the observer
			
		Minkowski right0=vector.normComp(right);
		right0=right0.normalize();
		Line searchline=new Line(center,vector,1);
		drawPoint(image,zBuffer,searchline.location(radius));
			
		for(double rad=perforation; rad<=Math.PI/2;rad=rad+perforation)
		{
			Minkowski searchvector=vector.times(Math.cos(rad)).add(right0.times(Math.sin(rad)));
			drawPoint(image,zBuffer,center.translate(searchvector,(radius)));
			for(double angle=perforation;angle<2*Math.PI;angle=angle+perforation)
			{
				searchvector.transform(rot);
				drawPoint(image,zBuffer,center.translate(searchvector,(radius)));
			}
		}
		double depth=drawPoint(image,zBuffer,center);
		return depth;
	}
		
	//*********************************************************************
	//Save the finished image
	//*********************************************************************
	public void finish(BufferedImage image, String name)
	{
			/*JFrame frame= new JFrame("name");
			frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
			JPanel panel=new JPanel();
			panel.setBackground(background);
			panel.setPreferredSize(new Dimension(width,height));
			Graphics2D graphic=image.createGraphics();*/
		File outputfile = new File(name+"."+format);
		try 
		{
			ImageIO.write(image, format, outputfile);
		} 
		catch (IOException e) 
		{
				System.out.println("IOException");
				e.printStackTrace();
		}
			
			
	}
		
	//*********************************************************
	// Moves the Observer into the watching direction
	//*********************************************************
	public void moveForward(double time)
	{
		right=right.transport(time, getForward());
		getForward().transport(time);
		Line line=getForward().copy();
		line.setMomentum(right);
		setRightward(line);
	}
		
	//********************************************************
	// Moves the Observer to the right
	//********************************************************
	public void moveRightward(double time)
	{
		Minkowski momentum=direction().transport(time,getRightward());
		getRightward().transport(time);
		right=getRightward().getMomentum();
		getForward().setOrigin(getRightward().getOrigin());
		getForward().setMomentum(momentum);
	}
		
	//********************************************************
	// Moves the Observer along the line Does not make sense if line doesnt start at pov
	//********************************************************
	public void moveAlong(Line line,double time)
	{
		Minkowski momentum=direction().transport(time,line);
		right=right.transport(time, line);
		line.transport(time);
		getForward().setOrigin(line.getOrigin());
		getForward().setMomentum(momentum);
				
		getRightward().setOrigin(line.getOrigin());
		getRightward().setMomentum(right);
	}
		
	
	//****************************************
	// Gives you the point you are looking at 1unit from you
	//*******************************************
	public Point focus()
	{
		Point focus=getForward().location(1/getForward().speed());
		return focus;
	}


	//******************************************
	// Don't remember what I did here?
	//********************************************
	public void divfinish(BufferedImage image, String name, int i)
	{
		int width=image.getWidth()/i, height=image.getHeight();

		for(int n=0;n<i;n++)
		{
			File outputfile = new File(name+n+"."+format);
			BufferedImage im=new BufferedImage(width,height,BufferedImage.TYPE_3BYTE_BGR);
			for(int k=0;k<width;k++)
			{
				for(int l=0; l<height;l++)
				{
					im.setRGB(k, l, image.getRGB((n*width+k), l));
				}
			}
			try
			{
				ImageIO.write(im, format, outputfile);
			} catch (IOException e) {	System.out.println("IOException"); e.printStackTrace();}
		}				
	}
	
	

	//************************************************************************
	//sets right to the direction of line. line should start at the same point
	//************************************************************************
	public void setRightward(Line line) 
	{
		rightward=line;
		right=line.direction;
		
	}
	
	//***********************************************************************
	//Checks whether the distance is less than shield
	//***********************************************************************
	public boolean closeTo(Line spherline) 
	{
		return position().distance(spherline.getOrigin())<shield;
	}



	//**********************************
	//Turn rightward by the given angle
	//**********************************
	public void turn(double angle) 
	{
		Minkowski v1=direction().copy(),
			v2=right;
		viewPoint.setMomentum(v1.times(Math.cos(angle)).add(v2.times(Math.sin(angle))));
		setRightward(new Line(position(),v2.times(Math.cos(angle)).add(v1.times(-Math.sin(angle))),1));
	}
}
