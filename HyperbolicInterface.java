package Hyperbolic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import javax.swing.*;



public class HyperbolicInterface extends JFrame
	{
	static int	impermin=720,
			denseness=6,filling=5,dotsize=20, //drawingspecs
				width=1080,height=1080,//2560,height=1440,//
				black=Color.black.getRGB(),
			start=0;//where does the enumeration of the files start
		static boolean empty=false;//Is the middle of polygons not drawn?	
		static double faintness=6,//4.75,//
				range=0.5;//6.25;//2; 
		static Color color=Video.randomColorMatt(70); //new Color(166,162,98);//
		static double[][] zBuffer=new double[1080][1080];
		static Honeycomb honey;
		public static Observer eye;
		JPanel panel;
		public HyperbolicInterface()
		{
			setTitle("Hyperbolic Music");
			setSize(1080, 1080);//1920,1080);//
			setBackground(Video.randomColorMatt(70));
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			panel=new HyperbolicPanel();
			add(panel);
			   setFocusable(true);

		        setVisible(true);
		}
		
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

			HyperbolicInterface frame=new HyperbolicInterface();
			
			//\Bend observer to my will*/
			Line	line;
			Minkowski right=new Minkowski(0,0,1,0);
			Point.still=false;
			
			honey=Polychoron.archimedean("pyramid", new int[] {6,6,6,6,3,4,4,4},new int[] {0,1,1,2,2},  range);
							//Polychoron.tilingnce(4,4,3,6);//(7,3,5,faintness);//
							//Polychoron.modify("rect",6,3,3,faintness);//

			Observer.range=faintness;//*3.2114;//8;//
			Observer.background=color;

			for(int i=0;i<2*impermin;i++)//for(int i=2334;i<2243+1000;i++)
			{ 
				//set up observer: psi changes the slope if its path
					double phi=i*Math.PI/impermin+0*Math.PI, psi=Math.PI/3;//Math.PI/impermin/10*i;//
					line=new Line(Point.zero,new Minkowski(Math.cos(phi),Math.sin(phi)*Math.cos(psi),Math.sin(phi)*Math.sin(psi),0),sqrt);
					//right=new Minkowski(-Math.sin(phi),Math.cos(phi)*Math.cos(psi),Math.cos(phi)*Math.sin(psi),0).transport(-1, line);//right=direction of movement
					right=new Minkowski(0,-Math.sin(psi),Math.cos(psi),0).transport(-1, line);
					line=line.transport(-1);
					eye=new Observer(line,right,width,height,Math.PI/3);
			//	Observer.range*=1.0005;
			//setup image
				
				zBuffer=eye.InitiateBuffer();

				System.out.print(i+". ");
				
				frame.repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//honey.drawEdges(eye, image, zBuffer, color);
			//	honey.drawFaces(eye, image, zBuffer, color);

			
		}
		} 
		class HyperbolicPanel extends JPanel
		{
			 @Override
	 	    protected void paintComponent(Graphics g) 
			 {
	 	        super.paintComponent(g);
	 	        setBackground(color);
	 	        honey.draw(g,HyperbolicInterface.eye,HyperbolicInterface.zBuffer);
	 	    //    scale.drawSpiral(2, 1, g);
			 }
			 
			 
		}
		 
	

}
