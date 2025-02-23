//****************************************************
// Hyperbolic/Fractal.java
// author: Non-Euclidean Dreamer
// Main Class for drawing Hyperbolic Fractals
//***************************************************
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.Random;

public class Fractal
{

static double faintness=4,range=10;
static int impermin=1440,start=0,
			width=1080,height=1080;
static String format="png",name= "hyp";
static BufferedImage image;
static Observer eye;
static int[][] zBuffer;
static DecimalFormat df=new DecimalFormat("0000");
public static void main(String[] args)
{ 
	Observer.range=faintness;
	Observer.format="png";
	double sqrt=faintness*.5;
	Point.still=false;


	double phi=Math.PI/2-0.5, psi=Math.PI/2-.3;
	Line line=new Line(Point.zero,new Minkowski(Math.cos(phi),Math.sin(phi)*Math.cos(psi),Math.sin(phi)*Math.sin(psi),0),sqrt);
	Minkowski right=new Minkowski(-Math.sin(phi),Math.cos(phi)*Math.cos(psi),Math.cos(phi)*Math.sin(psi),0).transport(-1, line);
	line=line.transport(-1);
	eye=new Observer(line,right,width,height,Math.PI/3);
	

	int m=4,n=6,k=12,l=6;
		Observer.range=faintness;
		image=eye.setup(0); 
		zBuffer=new int[width][height];
		// new int[] {m,n,k,k,m,n},new int[] {0,0,0,0},//symm
		// new int[] {8,8,8,6,6,6},new int[] {0,1,1,1}, //pyramid 
		//new int[] {m,n,k,l,m,n},new int[] {0,1,1,0}// one differs
		//new int[] {m,m,k,l,n,n},new int[] {0,1,1,2} //squeeze
		Honeycomb comb=//Polychoron.modify("rect",5,3,4,faintness);//rectify regular tiling
		//Polychoron.tilingnce(5,3,5,faintness);//regular tiling n-gons, c meeting per corner, e per edge
		Polychoron.archimedean("pyramid", new int[] { 4,8,6,6,4,6},new int[] {0,1,0,1},faintness); 
		////System.out.println(comb.center.size()+" faces");
		Observer.range=range;
		for(int i=start;i<impermin+start;i++)
		{
			phi=i*Math.PI/impermin+0.01; //psi=0.01+i*Math.PI/impermin/10;//Math.PI/impermin/10*i;//
			line=new Line(Point.zero,new Minkowski(Math.cos(phi),Math.sin(phi)*Math.cos(psi),Math.sin(phi)*Math.sin(psi),0),sqrt);
			right=new Minkowski(-Math.sin(phi),Math.cos(phi)*Math.sin(psi),Math.cos(phi)*Math.cos(psi),0).transport(-1, line);//right=direction of movement
			//right=new Minkowski(Math.sin(phi),-Math.cos(phi)*Math.sin(psi),Math.cos(psi),0).transport(-1, line);
			line=line.transport(-1);
			eye=new Observer(line,right,width,height,Math.PI/4);	
			comb.fractaldraw(eye,image, zBuffer);
		eye.finish(image, name+df.format(i)+".png"); 
		image=eye.setup(0); 
		zBuffer=new int[width][height];
	//		comb.drawFaces(eye, image, eye.InitiateBuffer(),  color);	eye.finish(image, "real4.png"); //for drawing the actual tiling as well

		}
	
	

}

public static void draw(BufferedImage image2, int[][] zBuffer2) {
	for(int i=0;i<width;i++)
		for(int j=0;j<height;j++)
			image2.setRGB(i, j, color(zBuffer2[i][j]));
}
private static int color(double d) {
	// TODO Auto-generated method stub
	return spectrum((int)(d*203),1);
}
public static int spectrum(int n, double full)
{
	n=n%(6*256);
	if (n<256)
		return new Color((int) (255*full),(int) (n*full),0).getRGB();
	n-=256;
	if (n<256)
		return new Color((int) ((255-n)*full),(int) (255*full),0).getRGB();
	n-=256;
	if (n<256)
		return new Color(0,(int) (255*full),(int) (n*full)).getRGB();
	n-=256;
	if (n<256)
		return new Color(0,(int) ((255-n)*full),(int) (255*full)).getRGB();
	n-=256;
	if (n<256)
		return new Color((int) (n*full),0,(int) (255*full)).getRGB();
	n-=256;
		return new Color((int) (255*full),0,(int) ((255-n)*full)).getRGB();
}
}
