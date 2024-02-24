//******************************************
// Hyperbolic/Honeycomb.java
// author: Non-Euclidean Dreamer
// Class for potentially infinite Honeycombs 
//******************************************

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Honeycomb 
{
	static double denseness=10;
	static Cellcomplex vf;//vertexfigure
	static double[][]vfd;//vertexfigure distances
	static int filling=3;
	static boolean empty=false;
	ArrayList<Point> vertex=new ArrayList<Point>(),
	 center=new ArrayList<Point>();
	ArrayList< int[]> edgepoint=new ArrayList<int[]>(),
		sidepoint=new ArrayList<int[]>(),
		pointedge=new ArrayList<int[]>(),
		pointside=new ArrayList<int[]>(),
		sideedge=new ArrayList<int[]>(),
		edgeside=new ArrayList<int[]>();
		
//************************************************************	
// Add vertex with empty(-1) array for adjoining edges & faces
//************************************************************
		public int addVertex(Point point,int nedge,int nside) 
		{
			int out=vertex.size();
			
			point.print();
			
			vertex.add(point);
			pointedge.add(minarray(nedge));
			pointside.add(minarray(nside));
			
			return out;
		}


//*****************************************************************************
// Add Edge connecting vertices nr i and j, listing it in the pointedge-arrays
//*****************************************************************************
		public void addEdge(int i, int j) 
		{
			int n=edgepoint.size();
		//	System.out.println("new edge nr"+n+":"+i+", "+j+" ,length="+vertex.get(i).distance(vertex.get(j)));
			System.out.println("new edge nr"+n+":"+i+", "+j+" ,length="+vertex.get(i).distance(vertex.get(j)));

			edgepoint.add(new int[] {i,j});
			/*if(pointedge.get(i)[0]==-1)	pointedge.get(i)[0]=n;
			else
			{
				int a=edgepoint.get(pointedge.get(i)[0])[1];
				if(a==i)a=edgepoint.get(pointedge.get(i)[0])[0];
			}*/
			int k=0;
			while(pointedge.get(i)[k]>-1) {k++;}
			pointedge.get(i)[k]=n;
			
			k=0;
			while(pointedge.get(j)[k]>-1) {k++;}
			pointedge.get(j)[k]=n;
		}
		public void addEdgealt(int i, int j)
		{	int n=edgepoint.size();
		//	System.out.println("new edge nr"+n+":"+i+", "+j+" ,length="+vertex.get(i).distance(vertex.get(j)));
		int nedge=pointedge.get(0).length;
			edgepoint.add(new int[] {i,j});

			boolean done =false;
			int k=0;
			while(!done)
			{
				while(pointedge.get(i)[k]>-1) {k++;}//System.out.println("edge nr"+k+" of vertex "+i+" is "+pointedge.get(i)[k]);
				int l=0;

				boolean fits=true;
				Point p1=vertex.get(j);
				while(l<k&&fits)
				{
					if(pointedge.get(i)[l]>-1)
					{
					//	System.out.println("distance end of edge "+l+" of vertex "+i+"(which is "+getEndCor(i,l)+") = "+Math.abs(p1.distance(getEnd(i, l))));

						if(Math.abs(p1.distance(getEnd(i, l))-vfd[k][l])>0.1)fits=false;
						else l++;
					}
					else l++;
				}
				if(fits)
				{
					l++;
					while(l<nedge&&fits)
					{
						if(pointedge.get(i)[l]>-1)
						{
							if(Math.abs(p1.distance(getEnd(i, l))-vfd[l][k])>0.1)fits=false;
							else l++;
						}
						else l++;
					}
				}
				if(fits)
				{pointedge.get(i)[k]=n;
				System.out.println("pointedge("+i+","+k+")="+n);
				done=true;}
				
				else k++;
			}
			done =false;
			 k=0;
			while(!done)
			{
				while(pointedge.get(j)[k]>-1) {k++;}
				int l=0;
				boolean fits=true;
				Point p1=vertex.get(i);
				while(l<k&&fits)
				{
					if(pointedge.get(j)[l]>-1)
					{
						//System.out.println("distance end of edge "+l+" of vertex "+j+" = "+Math.abs(p1.distance(getEnd(j, l))));
						if(Math.abs(p1.distance(getEnd(j, l))-vfd[k][l])>0.1)fits=false;
						else l++;
					}
					else l++;
				}
				if(fits)
				{
					l++;
					while(l<nedge&&fits)
					{
						if(pointedge.get(j)[l]>-1)
						{
							if(Math.abs(p1.distance(getEnd(j, l))-vfd[l][k])>0.1)fits=false;
							else l++;
						}
						else l++;
					}
				}
				if(fits)
				{pointedge.get(j)[k]=n;
				System.out.println("pointedge("+j+","+k+")="+n);
				done=true;}
				else k++;
			}
		}
		
		//***********************************
		//Other end of edge nr k from point i
		//************************************
		 Point getEnd(int i, int k) 
		{
			int a=getEndCor(i,k);
			return vertex.get(a);
		}


//*********************************************
// number of other end of edge nr k of vertex i
//*********************************************
		int getEndCor(int i, int k) 
		{
			if(pointedge.get(i)[k]==-1) return -1;
			int index=pointedge.get(i)[k],
					out=edgepoint.get(index)[0];
			if(out==i)
				out=edgepoint.get(index)[1];

			return out;
		}



//*********************************************
// Gives an array of length n filled with -1
//*********************************************		
		private int[] minarray(int n) 
		{
			int[]out=new int[n];
			for(int i=0;i<n;i++)out[i]=-1;
			return out;
		}
		
		//**********************
		//Draw Vertices to image
		//**********************
		public void drawVertices(Observer eye,BufferedImage image,double[][] zBuffer,Color background)
		{
			for(int i=0;i<vertex.size();i++)
			{
				Point v=vertex.get(i);
				v.setColor(v.standardcolor().getRGB());
				v.println();
				eye.drawPoint(image, zBuffer, v);//, radius, perforation
			}
		}


		//*********************************************
		//Add face with center c and vertices nr in sid
		//*********************************************
		public int addFace(Point c, int[] sid) 
		{
			int out=center.size();
			
			center.add(c);
			sidepoint.add(sid);
			
			for(int i=0;i<sid.length;i++)
			{
				int l=0;
				while(!edgeinface(pointedge.get(sid[i])[vf.edgepoint[l][0]],out)||!edgeinface(pointedge.get(sid[i])[vf.edgepoint[l][1]],out))l++;
				pointside.get(sid[i])[l]=out;
			}
			
			return out;
		}

		//******************************************
		// checks whether edge nr i lies in face nr j
		//******************************************
		private boolean edgeinface(int i, int j) 
		{
			if(i==-1)return false;
			
			boolean v1=false,v0=false;
			for(int k=0;k<sidepoint.get(j).length;k++)
			{
				if(sidepoint.get(j)[k]==edgepoint.get(i)[0])v0=true;
				if(sidepoint.get(j)[k]==edgepoint.get(i)[1])v1=true;
			}
			return (v0&&v1);
		}

		//**********************
		//Draw Edges to image
		//**********************

			public void drawEdges(Observer eye,BufferedImage image,double[][] zBuffer,Color background)
	{
		Line line;
		for(int i=0;i<edgepoint.size();i++)
		{
			line=vertex.get(edgepoint.get(i)[0]).geodesic(vertex.get(edgepoint.get(i)[1]));
			line.setColor((line.location(0.5).standardcolor()));
			eye.drawLine(image, zBuffer, line, 1);
		}
	}
			
			
			//********************
			// Draw Faces to image
			//********************
			public void drawFaces(Observer eye,BufferedImage image,double[][] zBuffer,Color background)
			{
				Line[] tocenter;
				Line line;
				for(int i=0;i<center.size();i++)
				{
					System.out.print(i+".");
					Color color=center.get(i).standardcolor();
					tocenter=new Line[sidepoint.get(i).length];
					for(int j=0;j<sidepoint.get(i).length;j++)
					{
						tocenter[j]=vertex.get(sidepoint.get(i)[j]).geodesic(center.get(i));
					}
					
					for(int j=0;j<sidepoint.get(i).length;j++)
					{
						int k=j-1; if(k<0){k=sidepoint.get(i).length-1;}
						
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





			Point getVertex(int i) 
			{
				return vertex.get(i);
			}



//draws last "amount" of faces
			public void drawFaces(Observer eye, BufferedImage image, double[][] zBuffer, Color background, int amount) 
			{
				Line[] tocenter;
				Line line;
				for(int i=center.size()-amount;i<center.size();i++)
				{
					System.out.print(i+".");
					Color color=center.get(i).standardcolor();
					tocenter=new Line[sidepoint.get(i).length];
					for(int j=0;j<sidepoint.get(i).length;j++)
					{
						tocenter[j]=vertex.get(sidepoint.get(i)[j]).geodesic(center.get(i));
					}
					
					for(int j=0;j<sidepoint.get(i).length;j++)
					{
						int k=j-1; if(k<0){k=sidepoint.get(i).length-1;}
						
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
}
