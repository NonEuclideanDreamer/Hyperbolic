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
	static double denseness=10; //How densely are the faces drawn
	static Cellcomplex vf;//vertexfigure
	static double[][]vfd;//vertexfigure distances
	static int[] edgetype;//are there "equivalent edges" in the vertexfigure?
	static int filling=3; //How much of the faces is drawn
	static boolean empty=false; // is the middle of the faces drawn
	ArrayList<Point> vertex=new ArrayList<Point>(), //vertices of the honeycomb
	 center=new ArrayList<Point>();					//centers of the faces of the honeycomb
	ArrayList< int[]> edgepoint=new ArrayList<int[]>(),//which vertices belong to which edges
		sidepoint=new ArrayList<int[]>(),			//which vertices belong to which faces
		pointedge=new ArrayList<int[]>(),			//which edges meet at which vertex
		pointside=new ArrayList<int[]>(),			//which faces meet at which vertex
		sideedge=new ArrayList<int[]>(),			//which edges surround which face
		edgeside=new ArrayList<int[]>();			// which faces meet at which edge
		
//************************************************************	
// Add vertex with empty(-1) array for adjoining edges & faces
//************************************************************
		public int addVertex(Point point,int nedge,int nside) 
		{
			int out=vertex.size();
			
		//	point.print();
			
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
			//	System.out.println("pointedge("+i+","+k+")="+n);
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
				//System.out.println("pointedge("+j+","+k+")="+n);
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
				//v.println();
				eye.drawPoint(image, zBuffer, v);//, radius, perforation
			}
		}


		//*********************************************
		//Add face with center c and vertices nr in sid
		//*********************************************
		public int addFace(Point c, int[] sid) 
		{
			int out=center.size();
			int nside=vf.edgepoint.length;
			center.add(c);
			sidepoint.add(sid);
			for(int i=0;i<sid.length;i++)
			{
				int l=0;
				while(!edgeinface(pointedge.get(sid[i])[vf.edgepoint[l][0]],out)||!edgeinface(pointedge.get(sid[i])[vf.edgepoint[l][1]],out)) {l++;if(l==nside) {System.out.println("failed finding pointside loc for "+i); center.removeLast(); sidepoint.removeLast(); return out-1;}}
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




			//return said vertex
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
			
			// at which slot of the pointedge array is edge nr "edge" of vertex nr "vertex" for its other end
			public int getAltEdgeNr(int vertex,int edge)
			{
				int other=getEndCor(vertex,edge);
				
				for(int i=0;i<vf.vertex.length;i++)
				{
					if(getEndCor(other,i)==vertex)return i;
				}

			return -1;
			}

			// "archedge" as in "archenemy"? I don't remember
			public void addarchEdge(int i, int j, int et) 
			{
				int n=edgepoint.size();
					System.out.println("new edge nr"+n+":"+i+", "+j+" ,length="+vertex.get(i).distance(vertex.get(j)));
				int nedge=pointedge.get(0).length;
					edgepoint.add(new int[] {i,j});

					boolean done =false;
					int k=0;
					while(!done)
					{
						if(k==nedge)return;
						while(pointedge.get(i)[k]>-1 ||edgetype[k]!=et) {k++;if(k==nedge)return;}//System.out.println("edge nr"+k+" of vertex "+i+" is "+pointedge.get(i)[k]);
						int l=0;

						boolean fits=true;
						Point p1=vertex.get(j);
						while(l<k&&fits)
						{
							if(pointedge.get(i)[l]>-1)
							{
							//	System.out.println("distance end of edge "+l+" of vertex "+i+"(which is "+getEndCor(i,l)+") = "+Math.abs(p1.distance(getEnd(i, l))));

								if(Math.abs(p1.distance(getEnd(i, l))-vfd[k][l])>0.02)fits=false;
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
									if(Math.abs(p1.distance(getEnd(i, l))-vfd[l][k])>0.02)fits=false;
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
						if(k==nedge)return;
						while(pointedge.get(j)[k]>-1||edgetype[k]!=et) {k++;if(k==nedge)return;}
						int l=0;
						boolean fits=true;
						Point p1=vertex.get(i);
						while(l<k&&fits)
						{
							if(pointedge.get(j)[l]>-1)
							{
								System.out.println("distance end of edge "+l+" of vertex "+j+" = "+Math.abs(p1.distance(getEnd(j, l))));
								if(Math.abs(p1.distance(getEnd(j, l))-vfd[k][l])>0.02 )fits=false;
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
									if(Math.abs(p1.distance(getEnd(j, l))-vfd[l][k])>0.02)fits=false;
									else l++;
								}
								else l++;
							}
						}
						if(fits)
						{
							pointedge.get(j)[k]=n;
						System.out.println("pointedge("+j+","+k+")="+n);
						done=true;}
						else k++;
					}
				}
				
			//for snubcases were the edgetype isn't consistent
			public void addarchEdge(int i, int j, int et,int et2) 
			{
				int n=edgepoint.size();
					System.out.println("new edge nr"+n+":"+i+", "+j+" ,length="+vertex.get(i).distance(vertex.get(j))+", et="+et+", "+et2);
				int nedge=pointedge.get(0).length;
					edgepoint.add(new int[] {i,j});

					boolean done =false;
					int k=0;
					while(!done)
					{
						if(k==nedge)return;
						while(pointedge.get(i)[k]>-1 ||edgetype[k]!=et) {System.out.println("edge nr"+k+" of vertex "+i+" is "+pointedge.get(i)[k]);k++;if(k==nedge)return;}//
						int l=0;

						boolean fits=true;
						Point p1=vertex.get(j);
						while(l<k&&fits)
						{
							if(pointedge.get(i)[l]>-1)
							{
								System.out.println("distance end of edge "+l+" of vertex "+i+"(which is "+getEndCor(i,l)+") = "+Math.abs(p1.distance(getEnd(i, l)))+", dist to edge "+k+" should be "+vfd[k][l]);
								
								if(Math.abs(p1.distance(getEnd(i, l))-vfd[k][l])>0.02)fits=false;
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
								//	System.out.println("distance end of edge "+l+" of vertex "+i+"(which is "+getEndCor(i,l)+") = "+Math.abs(p1.distance(getEnd(i, l))));

									if(Math.abs(p1.distance(getEnd(i, l))-vfd[l][k])>0.02)fits=false;
									else l++;
								}
								else l++;
							}
						}
						if(fits)
						{pointedge.get(i)[k]=n;
					//	System.out.println("pointedge("+i+","+k+")="+n);
						done=true;}
						
						else k++;
					}
					done =false;
				
					 k=0;
					while(!done)
					{
						if(k==nedge)return;
						while(pointedge.get(j)[k]>-1||edgetype[k]!=et2) {k++;if(k==nedge)return;}
						int l=0;
						boolean fits=true;
						Point p1=vertex.get(i);
						while(l<k&&fits)
						{
							if(pointedge.get(j)[l]>-1)
							{
								//System.out.println("distance end of edge "+l+" of vertex "+j+" = "+Math.abs(p1.distance(getEnd(j, l))));
								if(Math.abs(p1.distance(getEnd(j, l))-vfd[k][l])>0.02 )fits=false;
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
									if(Math.abs(p1.distance(getEnd(j, l))-vfd[l][k])>0.02)fits=false;
									else l++;
								}
								else l++;
							}
						}
						if(fits)
						{
							pointedge.get(j)[k]=n;
					//	System.out.println("pointedge("+j+","+k+")="+n);
						done=true;}
						else k++;
					}
				}
			public void addlateEdge(int i, int j, int et) 
			{
				int n=edgepoint.size();
				//	System.out.println("new edge nr"+n+":"+i+", "+j+" ,length="+vertex.get(i).distance(vertex.get(j)));
				int nedge=pointedge.get(0).length;
					edgepoint.add(new int[] {i,j});

					boolean done =false;
					int k=5;
					while(!done)
					{
						if(k==nedge)return;
						while(pointedge.get(i)[k]>-1 ||edgetype[k]!=et) {k++;if(k==nedge)return;}//System.out.println("edge nr"+k+" of vertex "+i+" is "+pointedge.get(i)[k]);
						int l=0;

						boolean fits=true;
						Point p1=vertex.get(j);
						while(l<k&&fits)
						{
							if(pointedge.get(i)[l]>-1)
							{
							//	System.out.println("distance end of edge "+l+" of vertex "+i+"(which is "+getEndCor(i,l)+") = "+Math.abs(p1.distance(getEnd(i, l))));

								if(Math.abs(p1.distance(getEnd(i, l))-vfd[k][l])>0.02)fits=false;
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
									if(Math.abs(p1.distance(getEnd(i, l))-vfd[l][k])>0.02)fits=false;
									else l++;
								}
								else l++;
							}
						}
						if(fits)
						{pointedge.get(i)[k]=n;
						//System.out.println("pointedge("+i+","+k+")="+n);
						done=true;}
						
						else k++;
					}
					done =false;
				
					 k=5;
					while(!done)
					{
						if(k==nedge)return;
						while(pointedge.get(j)[k]>-1||edgetype[k]!=et) {k++;if(k==nedge)return;}
						int l=0;
						boolean fits=true;
						Point p1=vertex.get(i);
						while(l<k&&fits)
						{
							if(pointedge.get(j)[l]>-1)
							{
							//	System.out.println("distance end of edge "+l+" of vertex "+j+" = "+Math.abs(p1.distance(getEnd(j, l))));
								if(Math.abs(p1.distance(getEnd(j, l))-vfd[k][l])>0.02 )fits=false;
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
									if(Math.abs(p1.distance(getEnd(j, l))-vfd[l][k])>0.02)fits=false;
									else l++;
								}
								else l++;
							}
						}
						if(fits)
						{
							pointedge.get(j)[k]=n;
					//	System.out.println("pointedge("+j+","+k+")="+n);
						done=true;}
						else k++;
					}
				}

			//**************************************************************************
			// Color corresponds to the number of faces the line of sight passes through
			//****************************************************************************
			public void fractaldraw(Observer eye, BufferedImage image, int[][] zBuffer) {
				for(int i=0;i<center.size();i++)
				{
					int[][]vertex=new int[sidepoint.get(i).length][3];
					int[]c=center.get(i).toScreen(eye);
					for(int j=0;j<sidepoint.get(i).length;j++)
					{
						vertex[j]=this.vertex.get(sidepoint.get(i)[j]).toScreen(eye);
					}
					fillPolygon(zBuffer,eye,i);
				}
				Fractal.draw(image,zBuffer);
			}

			//***************************************************************
			// Drawing a filled polygon via winding number, too slow for CPU
			//***************************************************************
			private void fillTriangle(int[] c, int[][] v, double[][] zBuffer) 
			{
				boolean[][]f=new boolean[zBuffer.length][zBuffer[0].length];
				boolean in=true;
				for(int i=0;i<v.length;i++)if(v[i][2]==0)in=false;
				if(!in)return;
				for(int x=0;x<zBuffer.length;x++)
					for(int y=0;y<zBuffer[0].length;y++)
					{
						double alpha=0;
						double[]ang=new double[v.length];
						for(int i=0;i<v.length;i++)
						{
							ang[i]=Math.atan2(v[i][1]-y, v[i][0]-x);
							if(v[i][2]==0)ang[i]=(ang[i]+2*Math.PI)%(2*Math.PI)-Math.PI;
						}
						for(int i=0;i<v.length;i++)
						{
							int j=(i+1)%v.length;
							double a=ang[j]-ang[i];
							if(a>Math.PI)a-=2*Math.PI;
							else if(a<-Math.PI)a+=2*Math.PI;
							alpha+=a;
						}
						if(Math.abs(alpha)>1)zBuffer[x][y]+=1;
					}
				
			}
			
			//****************************************************
			// for fractaldraw. adds 1 to each pixel of the face
			//****************************************************
			public void fillPolygon(int[][]Buffer, Observer eye,int face)//to be reconsidered for concave tiles
			{
				int width=Buffer.length,height=Buffer[0].length;
			
				boolean[][]canvas=new boolean[width][height];
				int[] one=center.get(face).toScreen(eye),two,three;
				int[]sp=sidepoint.get(face);
				int[][]v=new int[sp.length][3];
				boolean allin=true;
	
				for (int i=0;i<sp.length;i++)
				{
					v[i]=vertex.get(sp[i]).toScreen(eye);
					if(v[i][2]==0)allin=false;
				}
				if(allin)
				for (int i=0;i<sp.length;i++)
				{
					//System.out.println("in");
					two=v[i];
					three=v[(i+1)%sp.length];
				
					drawTriangle(one,two,three,canvas,Buffer);
				}
			//	else System.out.println("out");
//
			}

			//**********************************************************************************************************************
			// for fractaldraw, fills a single triangle, making sure we don't double down on pixels between two triangle of the face
			//**********************************************************************************************************************
			private void drawTriangle(int[] v1, int[] v2, int[] v3, boolean[][] canvas, int[][]Buffer) {
				int[]one=v1,two=v2,three=v3;
				if(v1[1]>v2[1])
				{
					if(v1[1]<v3[1])
					{
						two=v1;one=v2;
					}
					else if(v3[1]<v2[1])
					{
						one=v3;three=v1;
					}
					else
					{
						one=v2;two=v3;three=v1;
					}
				}
				else if(v1[1]>v3[1])
				{
					one=v3;two=v1;three=v2;
				}
				else if(v2[1]>v3[1])
				{
					two=v3;three=v2;
				}
				int sign=(int)Math.signum((two[0]-one[0])*(three[1]-one[1])-(three[0]-one[0])*(two[1]-one[1]));//System.out.print("drawTriangle");
				//System.out.println(one[0]+","+two[0]+","+three[0]);
				//if(two[0]<three[0])sign=-1;
				if(sign==0) {}//System.out.println("sign==0");
				else
				{
				for(int y=Math.max(0, one[1]);y<Math.min(canvas.length, two[1]);y++)
				{
					int x1=one[0]+(three[0]-one[0])*(y-one[1])/(three[1]-one[1]),x2=one[0]+(two[0]-one[0])*(y-one[1])/(two[1]-one[1]);
					//sign=(int) Math.signum(x2-x1);//if(two[1]==three[1])//System.out.println(x1+"<"+x2);
					for(int x=x1;sign*x<sign*(x2)+1;x+=sign)
					{
						try{
							if(! canvas[x][y]) {Buffer[x][y]++;
								canvas[x][y]=true; 
							}
						}
						catch(ArrayIndexOutOfBoundsException e){}
					}
				}
				
				for(int y=Math.max(0, two[1]);y<Math.min(canvas.length, three[1]);y++)
				{
					int x1=one[0]+(three[0]-one[0])*(y-one[1])/(three[1]-one[1]), x2=two[0]+(three[0]-two[0])*(y-two[1])/(three[1]-two[1]);//sign=(int) Math.signum(x2-x1);
					for(int x=x1;sign*x<sign*(x2)+1;x+=sign)
					{
						try{	if(! canvas[x][ y]) {Buffer[x][y]++;
						canvas[x][ y]=true; 
					}}catch(ArrayIndexOutOfBoundsException e){}
					}}
				}//System.out.println();
			}
			
}
