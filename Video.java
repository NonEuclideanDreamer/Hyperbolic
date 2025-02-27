//***********************************************************************************
//Hypebolic/Video.java
//author: Non-Euclidean Dreamer
// Generates image-files that can be put together to a video of moving in a honeycomb
//***********************************************************************************
package Hyperbolic;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class Video 
{
	static int	impermin=720,
		denseness=6,filling=5,dotsize=20, //drawingspecs
			width=1080,height=1080,//2560,height=1440,//
			black=Color.black.getRGB(),
		start=0;//where does the enumeration of the files start
	static boolean empty=false;//Is the middle of polygons not drawn?	
	static double faintness=4,//4.75,//
			range=5;//6.25;//2; 
	static String name="tetrahedron",//"honey",//"honeycomb",// "edge",//
			format="png";
	static Color color=randomColorMatt(70); //new Color(166,162,98);//


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
	double sqrt=Math.sqrt(2);


	
	//\Bend observer to my will*/
	double phi=Math.PI/impermin+0*Math.PI, psi=Math.PI/3;//Math.PI/impermin/10*i;//
	Line	line=new Line(Point.zero,new Minkowski(Math.cos(phi),Math.sin(phi)*Math.cos(psi),Math.sin(phi)*Math.sin(psi),0),1),line0;
	Minkowski right=new Minkowski(0,-Math.sin(psi),Math.cos(psi),0).transport(-1, line);;
	Observer eye=new Observer(line,right,width,height,Math.PI/3);
	eye.moveForward(3.4);
	Point.still=false;
	
	DecimalFormat df=new DecimalFormat("0000"); 
	Honeycomb honey=Polychoron.archimedean("pyramid", new int[] {8,8,4,4,3,4,6,4},new int[] {0,1,1,2,2},  faintness);
					//Polychoron.tilingnce(4,4,3,6);//(7,3,5,faintness);//
					//Polychoron.modify("rect",6,3,3,faintness);//

	Observer.range=range;//*3.2114;//8;//
	Observer.background=color;
	ArrayList<int[]>cl=new ArrayList<int[]>();
	int counter=0,last=-1,cur;
	//int[] closest=new int[2*impermin];
	for(int i=0;i<2*impermin;i++)//for(int i=2334;i<2243+1000;i++)
	{ 
		//set up observer: psi changes the slope if its path
	//		double phi=i*Math.PI/impermin+0*Math.PI, psi=Math.PI/3;//Math.PI/impermin/10*i;//
		//	line=new Line(Point.zero,new Minkowski(Math.cos(phi),Math.sin(phi)*Math.cos(psi),Math.sin(phi)*Math.sin(psi),0),sqrt);
			//line0=line.transport(new Line(Point.zero, new Minkowski(-Math.sin(phi),Math.cos(phi)*Math.cos(psi),Math.cos(phi)*Math.sin(psi),0),sqrt),-1);//right=direction of movement
			//right=new Minkowski(0,-Math.sin(psi),Math.cos(psi),0).transport(-1, line);
			//line=line.transport(-1);
			//eye=new Observer(line0,right,width,height,Math.PI/3);
	//	Observer.range*=1.0005;
	//setup image
		BufferedImage image=eye.setup(color.getRGB());
		double[][] zBuffer=eye.InitiateBuffer();

		System.out.print(i+". ");  
		eye.println();
		
		cur= honey.closestFace(eye);//honey.drawFaces(eye, image, zBuffer,  color);
		if(cur==last)counter++;
		else
		{
			if(last>-1)
			cl.add(new int[] {honey.sidepoint.get(last).length,counter});
			last=cur;
			counter=1;
		}
	//honey.drawEdges(eye, image, zBuffer, color);
	//	honey.drawFaces(eye, image, zBuffer, color);
		
		//eye.finish(image, name+df.format(i+start));
		eye.moveForward(-0.007);
	}
	cl.add(new int[] {honey.sidepoint.get(last).length,counter});

	print(cl);
}
private static void print(ArrayList<int[]> cl)
{
	System.out.print("{");
	for(int[] i:cl)
	{
		print(i);
		System.out.print(",");
	}
	System.out.print("}");
}
private static void print(int[] closest) 
{
	System.out.print("{");
	for(int i=0;i<closest.length;i++)
	{
		System.out.print(closest[i]+",");
	}
	System.out.print("}");
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
