//Hyperbolic/Image.png
//author: Non-Euclidean Dreamer
// Prints an image of a hyperbolic

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.Random;

public class Image 
{

static Color color=randomColorMatt(70);// new Color(71,13,41);//
static double faintness=6,range=8;
static int filling=3, black=Color.black.getRGB(), denseness=4, dotsize=20,
width=2560*5,height=1440*5;//1080,height=1080;
static boolean empty=false;
static String format="png",name= "honey668";
static BufferedImage image;
static Observer eye;
static double[][] transformation=Minkowski.idmatrix;
static double[][] zBuffer;
public static void main(String[] args)
{ 
	Observer.range=faintness;
	Observer.dotsize=dotsize;  
	Observer.format="png";
	Honeycomb.empty=empty;  
	Honeycomb.filling=filling;  
	Honeycomb.denseness=denseness;double sqrt=Math.sqrt(4);
	Point.still=false;


	double phi=Math.PI/2-0.5, psi=Math.PI/2-0.4;
	Line line=new Line(Point.zero,new Minkowski(Math.cos(phi),Math.sin(phi)*Math.cos(psi),Math.sin(phi)*Math.sin(psi),0),sqrt);
	Minkowski right=new Minkowski(-Math.sin(phi),Math.cos(phi)*Math.cos(psi),Math.cos(phi)*Math.sin(psi),0).transport(-1, line);
	line=line.transport(-1);
	eye=new Observer(line,right,width,height,Math.PI/3);
	

	int m=4,n=6,k=12,l=6;
		Observer.range=faintness;
		image=eye.setup(color.getRGB()); 
		zBuffer=eye.InitiateBuffer();
		Observer.background=color;
		// new int[] {m,n,k,k,m,n},new int[] {0,0,0,0},//symm
		// new int[] {8,8,8,6,6,6},new int[] {0,1,1,1}, //pyramid
		//new int[] {m,n,k,l,m,n},new int[] {0,1,1,0}// one differs
		//new int[] {m,m,k,l,n,n},new int[] {0,1,1,2} //squeeze
		Honeycomb comb=Polychoron.modify("rect",5,3,4,faintness);//rectify regular tiling
		//Polychoron.tilingnce(5,3,5,faintness);//regular tiling n-gons, c meeting per corner, e per edge
		//Polychoron.archimedean("pyramid", new int[] {8,8,6,6,3,4,6,4},new int[] {0,1,1,2,2},faintness); 

		Observer.range=range;
		zBuffer=eye.InitiateBuffer();   
		comb.drawFaces(eye, image, zBuffer,  color);
 
		eye.finish(image, name); 
		image=eye.setup(color.getRGB()); 

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
