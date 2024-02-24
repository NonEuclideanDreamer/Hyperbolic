//***********************************************************************************
//Hypebolic/Video.java
//author: Non-Euclidean Dreamer
// Generates image-files that can be put together to a video of moving in a honeycomb
//***********************************************************************************

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.Random;

public class Video 
{
	static int	impermin=720,
		denseness=6,filling=5,dotsize=20, //drawingspecs
			width=2560/2,height=1440/2,
			black=Color.black.getRGB(),
		start=0;//where does the enumeration of the files start
	static boolean empty=false;//Is the middle of polygons not drawn?	
	static double faintness=4,range=6; 
	static String name= "edge",//"tetrahedron",//
			format="png";
	static Color color=randomColorMatt(70); //new Color(71,19,56);//


public static void main(String[] args)
{ 
	Observer.dotsize=dotsize;  
	Observer.format="png";
	Cellcomplex.empty=empty;  
	Cellcomplex.filling=filling;  
	Cellcomplex.denseness=denseness;
	Honeycomb.denseness=denseness;
	Honeycomb.filling=Cellcomplex.filling;
	Honeycomb.empty=Cellcomplex.empty;
	double sqrt=Math.sqrt(4);


	
	//\Bend observer to my will*/
	Line	line;
	Minkowski right=new Minkowski(0,0,1,0);
	Observer eye;
	Point.still=false;
	
	DecimalFormat df=new DecimalFormat("0000"); 
	
	Honeycomb honey=Polychoron.tilingnce(7,3,5,faintness);//(4,4,3,5);//

	Observer.range=range;//8;//
	Observer.background=color;

	for(int i=0;i<2*impermin;i++)
	{ 
		//set up observer: psi changes the slope if its path
			double phi=i*Math.PI/impermin+0*Math.PI, psi=Math.PI/5;//Math.PI/3;//
			line=new Line(Point.zero,new Minkowski(Math.cos(phi),Math.sin(phi)*Math.cos(psi),Math.sin(phi)*Math.sin(psi),0),sqrt);
			right=new Minkowski(-Math.sin(phi),Math.cos(phi)*Math.cos(psi),Math.cos(phi)*Math.sin(psi),0).transport(-1, line);
			line=line.transport(-1);
			eye=new Observer(line,right,width,height,Math.PI/3);
		
	//setup image
		BufferedImage image=eye.setup(color.getRGB());
		double[][] zBuffer=eye.InitiateBuffer();

		System.out.print(i+". ");
		
		
		honey.drawFaces(eye, image, zBuffer,  color);
	//honey.drawEdges(eye, image, zBuffer, color);
	//	honey.drawFaces(eye, image, zBuffer, color);

		eye.finish(image, name+df.format(i+start));
	
	}
}
public static Color randomColorMatt(int degree)
{
	Random rand=new Random();
	int red=rand.nextInt(256);
	int green=rand.nextInt(Math.min(256, 2*degree)-Math.abs(128-red))+Math.max(0, red-degree);
	int blue=Math.max(0, Math.max(red-degree, green-degree))+rand.nextInt(Math.min(256+degree-red, Math.min(256+degree-green,Math.min(red+256-degree, Math.min(red-green+2*degree, Math.min(green+256-degree, green-red+2*degree))))));
	return new Color(red,green,blue);
}
}
