import java.awt.Color;
import java.awt.image.BufferedImage;

//**************************************************************************************
// <hyperbol/Cellcomplex.java
// author @Non-Euclidean Dreamer
// Creates the 2-Cellcomplex by the given set of vertices and edge- and side combinations .
//Finite Cellcomplexes only. Infinite->Honeycomb
//**************************************************************************************

public class Cellcomplex 
{
	Point[] vertex;
	Point[] center;
	int[][] edgepoint,
		sidepoint;
	
	//draw config
	public static boolean gap=false, empty=false;
	public static int filling=5;
	public static double radius=Math.PI/180, denseness=26, perforation=radius;
	public static boolean vertices=false;//Shall we draw vertices as spheres?
	
	//****************************************
	// Constructor
	//****************************************
	public Cellcomplex(Point[] vertex0, Point[] center0, int[][] edgepoint0, int[][] sidepoint0)
	{
		vertex=vertex0;
		center=center0;
		edgepoint=edgepoint0;
		sidepoint=sidepoint0;
	}
	
	//*****************************************
	// Gives you an array of the edges' centers
	//*****************************************
	public Point[] edgemiddles()
	{
		Point[] out=new Point[edgepoint.length];
		for(int i=0;i<edgepoint.length;i++)
		{
			out[i]=vertex[edgepoint[i][0]].middle(vertex[edgepoint[i][1]]);
		}
		return out;
	}
	
	//**********************************************************
	//Draws the Cellcomplex with given rotation
	//**********************************************************
	public void draw(Observer eye,BufferedImage image,double[][] zBuffer,double[][] transformation, Color background, double blurRatio)
	{
		int i,j,k;
		double l;
		Line line;
		Line[] tocenter;
		Color color;
		
		System.out.print("vertices...");

		if(vertices)
			for(i=0;i<vertex.length;i++)
			{
				vertex[i].setColor(Point.combine(vertex[i].transform(transformation).standardcolor(),background,blurRatio).getRGB());
				eye.drawPoint(image, zBuffer, vertex[i]);//, radius, perforation
			}
		
		if(gap)
		{
			System.out.println("edges...");
			for (i=0;i<edgepoint.length;i++)
			{
				line=vertex[edgepoint[i][0]].geodesic(vertex[edgepoint[i][1]]);
				line.setColor((Point.combine(line.location(0.5).transform(transformation).standardcolor(), background, blurRatio)));
				eye.drawLine(image, zBuffer, line, 1);
			}
		}
		System.out.print("faces");
		for(i=0;i<center.length;i++)
		{
			System.out.print(i+".");
			color=(Point.combine(center[i].transform(transformation).standardcolor(), background, blurRatio));
			tocenter=new Line[sidepoint[i].length];
			for(j=0;j<sidepoint[i].length;j++)
			{
				tocenter[j]=vertex[sidepoint[i][j]].geodesic(center[i]);
			}
			
			for(j=0;j<sidepoint[i].length;j++)
			{
				k=j-1; if(k<0){k=sidepoint[i].length-1;}
				
				for (l=1.0/denseness;l<1.0/filling;l=l+1.0/denseness)
				{
					line=tocenter[k].location(l).geodesic(tocenter[j].location(l));
					line.setColor(color);
					eye.drawLine(image, zBuffer, line, 1);
						
					if(empty==false)
					{
						line=tocenter[k].location(1-l).geodesic(tocenter[j].location(1-l));
						line.setColor(color);
						eye.drawLine(image, zBuffer, line, 1);
					}
					
				}
			}
		}
	}


	//****************************
	// For regular faces: creates the center array
	//*****************************
	static Point[] centers(Point[] vertex,int[][]sidepoint)
	{
		int n=sidepoint.length,
			l=sidepoint[0].length;
		boolean even=(l%2==0);
		Point[]c=new Point[n];
		if(even)
		{
			for (int i=0;i<n;i++)
			{
				System.out.print(i);
				c[i]=vertex[sidepoint[i][0]].geodesic(vertex[sidepoint[i][l/2]]).location(0.5);
				print(sidepoint[i]);
				System.out.println("s="+vertex[sidepoint[i][0]].distance(vertex[sidepoint[i][1]]));
			}
		}
		else
		{
			for (int i=0;i<n;i++)
			{
				System.out.print(i);
				Line line=vertex[sidepoint[i][1]].geodesic(vertex[sidepoint[i][0]].geodesic(vertex[sidepoint[i][2]]).location(0.5));
				double length=asinh(Math.sinh(vertex[ sidepoint[i][0]].distance(vertex[ sidepoint[i][1]])/2)/Math.sin(Math.PI/l));
				c[i]=line.location(length/line.speed());
				print(sidepoint[i]);
				System.out.println("s="+vertex[sidepoint[i][1]].distance(vertex[sidepoint[i][0]]));
			}
		}
	
	return c;
	
	}

	//****************************
	//Print int-array to terminal
	//****************************
	static void print(int[] is) 
	{
		System.out.print("{");
		for(int i=0;i<is.length;i++)
		{
			System.out.print(is[i]+",");
		}
		System.out.println("}");
	}
	
	public static double[] toDouble(Color col)
	{
		return new double[] {col.getRed(),col.getGreen(),col.getBlue()};
	}

	//********************************************************
	// Returns minimal distance from each vertex to the others
	//********************************************************
	public double[] vertexDistances() 
	{
		int n=vertex.length;
		double[]out=new double[n];
		for(int i=0;i<n;i++)
		{
			double k=2*Math.PI,l;
			for(int j=0;j<n;j++)if(i!=j)
			{
				l=vertex[i].distance(vertex[j]);
				if(l<k)
				{
					k=l;
				}
			}
			out[i]=k;
		}
			
				
		return out;
	}
	
	public int[][] neighbourList()
	{
		int n=edgepoint.length*2/vertex.length;System.out.println(n+" edges per vertex");
		int[][]out=new int[vertex.length][n];
		for(int i=0;i<vertex.length;i++)
		{
			int c=0, k=0;
			while(c<n && k<edgepoint.length)
			{
				int j=0;
				if (edgepoint[k][j]!=i)j++;
				if(edgepoint[k][j]==i)
				{
					out[i][c]=edgepoint[k][1-j];
					c++;
				}
				k++;
			}
		}
		return out;
	}
	
	
	//************************************
	//Draw the vertices of the Cellcomplex
	//************************************
	public void drawVertices(Observer eye,BufferedImage image,double[][] zBuffer,double[][] transformation, Color background)
	{
		for(int i=0;i<vertex.length;i++)
		{
			vertex[i].setColor(vertex[i].transform(transformation).standardcolor().getRGB());
			vertex[i].println();
			eye.drawPoint(image, zBuffer, vertex[i]);
		}
	}
	
	//************************************
	//Draw the edges of the Cellcomplex
	//************************************
	public void drawEdges(Observer eye,BufferedImage image,double[][] zBuffer,double[][] transformation, Color background)
	{
		Line line;
		for(int i=0;i<edgepoint.length;i++)
		{
			line=vertex[edgepoint[i][0]].geodesic(vertex[edgepoint[i][1]]);
			line.setColor((line.location(0.5).transform(transformation).standardcolor()));
			eye.drawLine(image, zBuffer, line, 1);
		}
	}
	
	//************************************
	//Draw the faces of the Cellcomplex
	//************************************
	public void drawFaces(Observer eye,BufferedImage image,double[][] zBuffer,double[][] transformation, Color background)
	{
		Line[] tocenter;
		Line line;
		for(int i=0;i<center.length;i++)
		{
			System.out.print(i+".");
			Color color=center[i].transform(transformation).standardcolor();
			tocenter=new Line[sidepoint[i].length];
			for(int j=0;j<sidepoint[i].length;j++)
			{
				tocenter[j]=vertex[sidepoint[i][j]].geodesic(center[i]);
			}
			
			for(int j=0;j<sidepoint[i].length;j++)
			{
				int k=j-1; if(k<0){k=sidepoint[i].length-1;}
				
				for (double l=1.0/denseness;l<1.0/filling;l=l+1.0/denseness)
				{
					line=tocenter[k].location(l).geodesic(tocenter[j].location(l));
					line.setColor(color);
					eye.drawLine(image, zBuffer, line, 1);
						
					if(empty==false)
					{
						line=tocenter[k].location(1-l).geodesic(tocenter[j].location(1-l));
						line.setColor(color);
						eye.drawLine(image, zBuffer, line, 1);
					}
					
				}
			}
		}
	}
	
	/*int[][]sideedge()
	{
		
	}*/
	
	//*****************************
	//inverse of sinus hyperbolicus
	//*****************************
	static double asinh(double x)
	{
		return Math.log(x+Math.sqrt(x*x+1));
	}
}