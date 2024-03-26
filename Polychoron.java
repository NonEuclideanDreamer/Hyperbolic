//***********************************************************************************
// Hyperbolic/Polychoron.java
// author @non-euclidean dreamer
// Collection of Polyhedra/Polychora/Honeycombs in Hyperbolic Space
//***********************************************************************************


import java.awt.image.*;
import java.util.ArrayList;
import java.awt.Color;

public class Polychoron 
{
	static double faintness;
	
	//how are edges/vertices/faces of the Platonic solids connected 
	final static int[][] tetrahedronEdgepoint=new int[][] {{0,1},{0,2},{0,3},{1,2},{2,3},{3,1}};
	final static int[][] hexahedronEdgepoint=new int[][] {{0,3},{0,1},{1,2},{2,3},{7,4},{5,4},{6,5},{7,6},{0,4},{1,5},{2,6},{7,3}};
	final static int[][] octahedronEdgepoint=new int[][]{{0,1},{0,2},{0,3},{0,4},{5,1},{5,2},{5,3},{5,4},{1,2},{2,3},{3,4},{4,1}};
	final static int[][] dodecahedronEdgepoint=new int[][]{{4,0},{0,1},{1,2},{2,3},{3,4},{0,5},{1,6},{2,7},{3,8},{4,9},{5,10},{5,11},{6,11},{6,12},{7,12},
			{7,13},{8,13},{8,14},{9,14},{9,10},{10,15},{11,16},{12,17},{13,18},{14,19},{15,16},{16,17},{17,18},{18,19},{19,15}};
	final static int[][] icosahedronEdgepoint=new int[][]{{0,1},{0,2},{0,3},{0,4},{0,5},{1,2},{2,3},{3,4},{4,5},{5,1},{1,6},{2,6},{2,7},{3,7},{3,8},
			{4,8},{4,9},{5,9},{5,10},{1,10},{10,6},{6,7},{7,8},{8,9},{9,10},{6,11},{7,11},{8,11},{9,11},{10,11}};
	final static int[][] tetrahedronSidepoint=new int[][] {{1,2,3},{0,2,3},{0,1,3},{0,1,2}};
	final static int[][] hexahedronSidepoint=new int[][]{{1,0,3,2},{3,0,4,7},{0,1,5,4},{1,2,6,5},{2,3,7,6},{4,5,6,7}};
	final static int[][] octahedronSidepoint=new int[][]{{0,2,1},{0,3,2},{0,4,3},{0,1,4},{5,1,2},{5,2,3},{5,3,4},{5,4,1}};
	final static int[][] dodecahedronSidepoint=new int[][]{{0,1,2,3,4},{4,9,10,5,0},{0,5,11,6,1},{1,6,12,7,2},{2,7,13,8,3},{3,8,14,9,4},
			{5,10,15,16,11},{6,11,16,17,12},{7,12,17,18,13},{8,13,18,19,14},{9,14,19,15,10},{15,19,18,17,16}};
	final static int[][] icosahedronSidepoint=new int[][]{{0,1,2},{0,2,3},{0,3,4},{0,4,5},{0,5,1},{1,6,2},{2,7,3},{3,8,4},{4,9,5},{5,10,1},
			{1,10,6},{2,6,7},{3,7,8},{4,8,9},{5,9,10},{10,11,6},{6,11,7},{7,11,8},{8,11,9},{9,11,10}};
	final static int[][] icosahedronSideedge=new int[][]{{0,5,1},{1,6,2},{2,7,3},{3,8,4},{4,9,0},{10,11,5},{12,13,6},{14,15,7},{16,17,8},{18,19,9},
			{19,20,10},{11,21,12},{13,22,14},{15,23,16},{17,24,18},{29,25,20},{25,26,21},{26,27,22},{27,28,23},{28,29,24}};//checkorientation
	final static int[][] dodecahedronSideedge=new int[][]{{0,1,2,3,4},{0,9,19,10,5},{1,5,11,12,6},{2,6,13,14,7},{3,7,15,16,8},{4,8,17,18,9},
			{11,10,20,25,21},{13,12,21,26,22},{15,14,22,27,23},{17,16,23,28,24},{19,18,24,29,20},{25,29,28,27,26}};
	final static int[][] hexahedronSideedge=new int[][] {{1,0,3,2},{0,8,4,11},{1,9,5,8},{2,10,6,9},{3,11,7,10},{5,6,7,4}};
	final static int[][] octahedronSideedge=new int[][] {{1,8,0},{2,9,1},{3,10,2},{0,11,3},{4,8,5},{5,9,6},{6,10,7},{7,11,4}};
	final static int[][] dodecahedronEdgenumber=new int[][] {{0,1},{1,1},{2,1},{3,1},{4,1},{2,0},{2,0},{2,0},{2,0},{2,0},{3,1},{4,0},{3,1},{4,0},{3,1},
			{4,0},{3,1},{4,0},{3,1},{4,0},{4,2},{4,2},{4,2},{4,2},{4,2},{3,1},{3,2},{3,3},{3,4},{3,0}};
	final static int[][] dodecahedronVertexnumber=new int[][] {{0,4,0},{1,4,0},{2,4,0},{3,4,0},{4,4,0},{3,0,1},{3,0,1},{3,0,1},{3,0,1},{3,0,1},
		{2,4,1},{2,4,1},{2,4,1},{2,4,1},{2,4,1},{3,0,2},{3,4,2},{3,3,2},{3,2,2},{3,1,2}},
			hexahedronVertexnumber= {{1,0,1},{0,0,1},{3,0,1},{2,0,1},{0,2,3},{1,2,3},{2,2,3},{3,2,3}};
	final static int[] hexaoctaTetraVertexnumber= {0,0,1,1,3,2,2,3,0,2,1,3},
					revHotVn= {2,3,3,2,1,1,0,0,1,3,0,2};
	final static int[][] thovn=new int[][] {{0,1,8},{2,3,10},{5,6,9},{4,7,11}},
					rthovn=new int[][] {{6,7,10},{4,5,8},{0,3,11},{1,2,9}};
	
	static Cellcomplex vf; //vertex figure used to build honeycomb
	
	static boolean weirdcase=false;
	
	public static Cellcomplex tetrahedron(double s)
	{
		Point[] vertex=new Point[4],
				center;
		int[][]edgepoint=new int[6][2],
				sidepoint=new int[4][3];
		double sqrt=Math.sqrt(2),sqrt6=Math.sqrt(6);
		vertex[0]=new Line(Point.zero,new Minkowski(2/sqrt6,0,-sqrt/sqrt6,0),1).location(s);
		vertex[1]=new Line(Point.zero,new Minkowski(-2/sqrt6,0,-sqrt/sqrt6,0),1).location(s);
		vertex[2]=new Line(Point.zero,new Minkowski(0,2/sqrt6,sqrt/sqrt6,0),1).location(s);
		vertex[3]=new Line(Point.zero,new Minkowski(0,-2/sqrt6,sqrt/sqrt6,0),1).location(s);
		int a=0,b=0;
		for(int i=0;i<3;i++)
			for(int j=i+1;j<4;j++)
			{
				edgepoint[a][0]=i;
				edgepoint[a][1]=j;
				a++;
				for(int k=j+1;k<4;k++)
				{
					sidepoint[b][0]=i;
					sidepoint[b][1]=j;
					sidepoint[b][2]=k;
					b++;
				}
			}
		center=Cellcomplex.centers(vertex, sidepoint);
		return new Cellcomplex(vertex,center,edgepoint,sidepoint);
	}
	
	//First uniform honeycomb. the generalized method is tilingnce
	public static Honeycomb tilingn33(int n,int v)//v=number of finished vertices
	{
		double sqrt2=Math.sqrt(2), sqrt3=Math.sqrt(3);
		double s=2*acosh(sqrt3/sqrt2*Math.cos(Math.PI/n));
		Honeycomb honey=new Honeycomb();
		honey.addVertex(Point.zero,4,6);
		
		Cellcomplex tetrahedron=tetrahedron(s);
		
		for(int i=0;i<4;i++)
		{
			honey.addVertex(tetrahedron.vertex[i],4,6);
			honey.addEdge(0,i+1);
		}
		
		int k=0;
		while(k<honey.vertex.size())
		{	if(Point.zero.distance(honey.vertex.get(k))<v)
		{System.out.println("k="+k+", distance="+Point.zero.distance(honey.vertex.get(k)));
			int l=0;
			int[] pointedge=honey.pointedge.get(k);
			for(int i=0;i<3;i++)
				for(int j=i+1;j<4;j++)
				{
					if(honey.pointside.get(k)[l]<0)
					{
						int[]sidepoint=new int[n];
						//System.out.println("edge nr"+pointedge[i]+"("+honey.edgepoint.get(pointedge[i])[0]+","+honey.edgepoint.get(pointedge[i])[1]+")");
						int a=honey.edgepoint.get(pointedge[i])[1];
						if(a==k) {a=honey.edgepoint.get(pointedge[i])[0];}

					//	System.out.println("edge nr"+pointedge[j]+"("+honey.edgepoint.get(pointedge[j])[0]+","+honey.edgepoint.get(pointedge[j])[1]);
						int b=honey.edgepoint.get(pointedge[j])[1];
						if(b==k) {b=honey.edgepoint.get(pointedge[j])[0];}
						Point m=honey.vertex.get(a).middle(honey.vertex.get(b));
						Line line=honey.vertex.get(k).geodesic(m);
						line.setSpeed(1);
						Point center=line.location(acosh(1/sqrt2/Math.tan(Math.PI/n)));

					//	System.out.println("center to vertexshouldbe="+acosh(1	/sqrt2/Math.tan(Math.PI/n)));
						sidepoint[0]=k;
						sidepoint[1]=a;
						sidepoint[n-1]=b;
						int[] in= {2,n-2},mid= {k,k},nju= {a,b},old= {b,a};
						int it=0;
						while(in[0]<=in[1]+1) 
						{
						Minkowski w=honey.vertex.get(mid[it]).direction(honey.vertex.get(old[it])).mirrortransport(1, honey.vertex.get(mid[it]).geodesic(honey.vertex.get(nju[it])));
						//System.out.println("wnorm="+w.hdot(w)+", old="+old[it]+", mid="+mid[it]+", nju="+nju[it]);
						//honey.vertex.get(old[it]).print();
						//honey.vertex.get(mid[it]).print();
						//honey.vertex.get(nju[it]).print();
						Point p=new Line( honey.vertex.get(nju[it]),w,s).location(1);
						//System.out.println("center to vertex="+p.distance(center));
					//	System.out.println("s "+s);
						int count=0, done=-1;int edge=honey.pointedge.get(nju[it])[count];
						while(count<4&&edge>-1&&done<0)  //Case: edge already exists (Does this even happen??
						{
							if(p.distance(honey.vertex.get(honey.edgepoint.get(edge )[0]))<s/2)done=0;
							else if(p.distance(honey.vertex.get(honey.edgepoint.get(edge )[1]))<s/2)done=1;
							else
							{
								count++;
								if(count<4)
								edge=honey.pointedge.get(nju[it])[count];
							}
						}
						if(done>-1)		{
							int h=honey.edgepoint.get(edge)[done];
							
							sidepoint[in[it]]=h;
							in[it]+=Math.pow(-1, it);
							old[it]=mid[it];
							mid[it]=nju[it];
							nju[it]=h;
							it=1-it;
						}
						else {
						while(count<honey.vertex.size()&&done<0)
						{
							if(honey.vertex.get(count).distance(p)<s/2)done=1;
							else count++;
						}
						if(done>0)
						{
							sidepoint[in[it]]=count;
							honey.addEdge(count, nju[it]);
							in[it]+=Math.pow(-1, it);
							old[it]=mid[it];
							mid[it]=nju[it];
							nju[it]=count;
							it=1-it;
						}
						
						else {
							int h=honey.addVertex(p, 4, 6);
							honey.addEdge(h, nju[it]);
							sidepoint[in[it]]=h;
							in[it]+=Math.pow(-1, it);
							old[it]=mid[it];
							mid[it]=nju[it];
							nju[it]=h;
							it=1-it;
						}
						}
				
						}
						honey.addFace(center,sidepoint);
					}
					
					l++;
				}
		}k++;
			
		}
		return honey;
	}
	
	 static double acosh(double x) 
	{
		if(x<1)
		return 0;
		
		else return Math.log(x+Math.sqrt(x*x-1));
	}
	 
	 //regular ngon with circumference s
	public static Cellcomplex ngon(int n, double s) 
	{
		Point[] vertex=new Point[n],
				center=new Point[1];
		int[][]edgepoint=new int[n][2],
				sidepoint=new int[1][n];
		double sqrt=Math.sqrt(2),sqrt6=Math.sqrt(6);
		center[0]=Point.zero;
		for(int i=0;i<n;i++)
		{
			vertex[i]=new Line( Point.zero,new Minkowski(Math.cos(2*Math.PI/n*i),0,Math.sin(2*Math.PI/n*i),0),1).location(s);
			edgepoint[i][0]=i;
			edgepoint[i][1]=(i+1)%n;
			sidepoint[0][i]=i;
		}

		return new Cellcomplex(vertex,center,edgepoint,sidepoint);
	}
	
	//builds any hyperbolic uniform honeycomb (except those with ideal vertices)
	public static Honeycomb tilingnce(int n,int c,int e,double faintness)
	{
		double sqrt2=Math.sqrt(2), sqrt3=Math.sqrt(3);
		double s=2*acosh(sqrt3/sqrt2*Math.cos(Math.PI/n)),
				r=acosh(1/sqrt2/Math.tan(Math.PI/n));
		Honeycomb honey=new Honeycomb();
		int nedge,nside;
		vf=Polychoron.tetrahedron(s);
		Polychoron.faintness=faintness;
		Observer.range=faintness;
		if(e==2)
		{
			s=2*acosh(Math.cos(Math.PI/n)/Math.sin(Math.PI/c));
			r=acosh(1/Math.tan(Math.PI/c)/Math.tan(Math.PI/n));
			vf=Polychoron.ngon(c,s);
		}
		else if(e==4)
		{
			//if(c==3)
	
			s=2*acosh(Math.cos(Math.PI/n)/Math.sin(Math.PI/4));
			r=acosh(1/Math.tan(Math.PI/4)/Math.tan(Math.PI/n));
			vf=Polychoron.octahedron(s);
		}
		else if(e==5)
		{
			double phi=(Math.sqrt(5)+1)/2;
			double angle=Math.atan(1/phi);
			s=2*acosh(Math.cos(Math.PI/n)/Math.sin(angle));
			r=acosh(1/Math.tan(angle)/Math.tan(Math.PI/n));
			vf=Polychoron.icosahedron(s);
		}
		else if(c==4)//e=3
		{
			double angle=Math.asin(1/sqrt3);
			s=2*acosh(Math.cos(Math.PI/n)/Math.sin(angle));
			r=acosh(1/Math.tan(angle)/Math.tan(Math.PI/n));
			vf=Polychoron.hexahedron(s);
		}
		else if(c==5)//e=3
		{
			double angle=Math.atan((3-Math.sqrt(5))/2);
			s=2*acosh(Math.cos(Math.PI/n)/Math.sin(angle));
			r=acosh(1/Math.tan(angle)/Math.tan(Math.PI/n));
			vf=Polychoron.dodecahedron(s);
		}
		nedge=vf.vertex.length;
		nside=vf.edgepoint.length;
		Honeycomb.vf=vf;
		Honeycomb.vfd=new double[vf.vertex.length][vf.vertex.length];
		for(int i=0;i<vf.vertex.length;i++)for(int j=0;j<i+1;j++) {Honeycomb.vfd[i][j]=vf.vertex[i].distance(vf.vertex[j]);System.out.println("vfd("+i+", "+j+")="+Honeycomb.vfd[i][j]);}
		honey.addVertex(Point.zero,nedge,nside);

			
			for(int i=0;i<nedge;i++)
			{
				honey.addVertex(vf.vertex[i],nedge,nside);
			//	doit.add(true);
				honey.addEdge(0,i+1);
			}
		
			System.out.println("center to vertex should be="+r+", edgelength should be"+s );
	
		int k=0;
		int[][]per= {{0,0,0},{1,3,4},{2,5,7},{3,4,1},{4,1,3},{5,7,2},{6,6,6}};;
		while(k<honey.vertex.size())
		{	
			if(honey.getVertex(k).isIn())//(doit.get(k))
		{System.out.println("k="+k+", distance="+Point.zero.distance(honey.vertex.get(k)));
			int i,j;
			int[] pointedge=honey.pointedge.get(k);
			boolean draw=false;
			
			//Fill in missing edges
			boolean notdone;
			int mu=0;
			if(k>0)
				do{
				notdone=false;	
				int mom=honey.getEndCor(k,mu);
				
			if(mom>-1) {	
			Point mother=honey.getEnd(k, mu);
			for(int o=0;o<nedge;o++)if(honey.getEndCor(mom,o)>-1)
			{
				
				Minkowski w=mother.direction(honey.getEnd(mom,o)).mirrortransport(1, mother.geodesic(honey.vertex.get(k)));
		//		System.out.println("wnorm="+w.hdot(w)+", old="+old[it]+", mid="+mid[it]+", nju="+nju[it]);
		//		honey.vertex.get(old[it]).print();
			//	honey.vertex.get(mid[it]).print();
			//	honey.vertex.get(nju[it]).print();
				Point p=new Line( honey.vertex.get(k),w,s).location(1);
				boolean done=false;
				int u=0;
				while(!done&&u<nedge)
				{
					if(pointedge[u]>-1) {
					if(p.distance(honey.getEnd(k,u))<s/2)done=true;//edge already exists
					else u++;}
					else u++;
				}
				if(!done)
				{
					u=0;
					while(!done&&u<honey.vertex.size())
					{
						if(p.distance(honey.getVertex(u))<s/2)done=true;
						else u++;
					}
					if(done)honey.addEdgealt(k, u);//vertex exists
					else
					{
						honey.addVertex(p,nedge,nside);
						//doit.add(p.isIn());
						honey.addEdgealt(k, u);
					}
				}
				
			}
			else notdone=true;
			}
			else notdone=true;
			
			mu++;	}while(notdone&&mu<nedge);
			

			int ver0=honey.vertex.size();
			//for(int m=0;m<nedge;m++)
			//	System.out.println("edge nr"+m+"of vertex nr"+k+": "+honey.pointedge.get(k)[m]);
			for(int l=0;l<vf.edgepoint.length;l++)
				{
					i=vf.edgepoint[l][0];
					j=vf.edgepoint[l][1];
				//	System.out.println("i="+i+", j="+j);
					if(honey.pointside.get(k)[l]==-1&&pointedge[i]>-1&&pointedge[j]>-1)
					{
						int[]sidepoint=new int[n];
					//	System.out.println("edge nr"+pointedge[i]+"("+honey.edgepoint.get(pointedge[i])[0]+","+honey.edgepoint.get(pointedge[i])[1]+")");
						int a=honey.edgepoint.get(pointedge[i])[1];
						if(a==k) {a=honey.edgepoint.get(pointedge[i])[0];}

					//	System.out.println("edge nr"+pointedge[j]+"("+honey.edgepoint.get(pointedge[j])[0]+","+honey.edgepoint.get(pointedge[j])[1]+")");
						int b=honey.edgepoint.get(pointedge[j])[1];
						if(b==k) {b=honey.edgepoint.get(pointedge[j])[0];}
						Point m=honey.vertex.get(a).middle(honey.vertex.get(b));
						Line line=honey.vertex.get(k).geodesic(m);
						line.setSpeed(1);
						Point center=line.location(r);

					
						sidepoint[0]=k;
						sidepoint[1]=a;
						sidepoint[n-1]=b;
						int[] in= {2,n-2},mid= {k,k},nju= {a,b},old= {b,a}, lastedge= {pointedge[i],pointedge[j]};
						int it=0;
						
						while(in[0]<=in[1]+1) 
						{
						Minkowski w=honey.vertex.get(mid[it]).direction(honey.vertex.get(old[it])).mirrortransport(1, honey.vertex.get(mid[it]).geodesic(honey.vertex.get(nju[it])));
					//	System.out.println("wnorm="+w.hdot(w)+", old="+old[it]+", mid="+mid[it]+", nju="+nju[it]);
				//		honey.vertex.get(old[it]).print();
					//	honey.vertex.get(mid[it]).print();
					//	honey.vertex.get(nju[it]).print();
						Point p=new Line( honey.vertex.get(nju[it]),w,s).location(1);
				//	System.out.println("center to vertex="+p.distance(center));
					//	System.out.println("s "+s);
						int count=0, done=-1;int edge=honey.pointedge.get(nju[it])[count];
						while(count<nedge&&done<0)  //Case: edge already exists (Does this even happen??
						{
							if(edge>-1) {
						//	System.out.println("checking edge nr. "+edge+"("+honey.edgepoint.get(edge )[0]+", "+honey.edgepoint.get(edge )[1]+")");
							if(p.distance(honey.vertex.get(honey.edgepoint.get(edge )[0]))<s/2)done=0;
							else if(p.distance(honey.vertex.get(honey.edgepoint.get(edge )[1]))<s/2)done=1;
							else
							{
								count++;
								
							}}
							else count++;
							if(count<nedge)
								edge=honey.pointedge.get(nju[it])[count];
						}
						if(done>-1)		{
							int h=honey.edgepoint.get(edge)[done];
						//	System.out.println("vertex exists as well as edge");
							sidepoint[in[it]]=h;
							in[it]+=Math.pow(-1, it);
							old[it]=mid[it];
							mid[it]=nju[it];
							nju[it]=h;
							it=1-it;
						}
						else {
							count=0;
						while(count<honey.vertex.size()&&done<0)
						{
							if(honey.vertex.get(count).distance(p)<s/2)done=1;
							else count++;
						}
						if(done>0)
						{
							sidepoint[in[it]]=count;
							//System.out.println("vertex exists but edge doesn't");
							honey.addEdgealt(count, nju[it]);
							
							in[it]+=Math.pow(-1, it);
							old[it]=mid[it];
							mid[it]=nju[it];
							nju[it]=count;
							it=1-it;
						}
						
						else {
						//	System.out.println("neither vertex nor edge exist");
							int h=honey.addVertex(p, nedge,nside);
							if (p.isIn())draw=true;
							
							honey.addEdgealt(h, nju[it]);
							sidepoint[in[it]]=h;
							in[it]+=Math.pow(-1, it);
							old[it]=mid[it];
							mid[it]=nju[it];
							nju[it]=h;
							it=1-it;
						}
						}
				
						}
						 honey.addFace(center,sidepoint);
						 
				//int drawIt=2000;	if(honey.sidepoint.size()%drawIt==0) {	honey.drawFaces(Image.eye, Image.image, Image.zBuffer, Image.color,drawIt);		Image.eye.finish(Image.image, "hr535");}
					}
					
				}
			
			
			int num=honey.vertex.size()-ver0;
		}k++;
		}
		return honey;
	}
	
	private static Cellcomplex octahedron(double s) 
	{
		Point[] vertex=new Point[6],center;
		int[][]edgepoint=octahedronEdgepoint,
				sidepoint=octahedronSidepoint;
	
		vertex[0]=new Line( Point.zero,new Minkowski(0,0,1,0),1).location(s);
		vertex[1]=new Line( Point.zero,new Minkowski(1,0,0,0),1).location(s);
		vertex[2]=new Line( Point.zero,new Minkowski(0,1,0,0),1).location(s);
		vertex[3]=new Line( Point.zero,new Minkowski(-1,0,0,0),1).location(s);
		vertex[4]=new Line( Point.zero,new Minkowski(0,-1,0,0),1).location(s);
		vertex[5]=new Line( Point.zero,new Minkowski(0,0,-1,0),1).location(s);

	
		center=Cellcomplex.centers(vertex, sidepoint);
		return new Cellcomplex(vertex,center,edgepoint,sidepoint);
	}
	
	private static Cellcomplex icosahedron(double s) 
	{
		Point[] vertex=new Point[12],center;
		int[][]edgepoint=icosahedronEdgepoint,
				sidepoint=icosahedronSidepoint;
	double sqrt=1/Math.sqrt(5), phi=(Math.sqrt(5)+1)/2;
		vertex[0]=new Line( Point.zero,new Minkowski(0,0,1,0),1).location(s);
		vertex[1]=new Line( Point.zero,new Minkowski(0,2*sqrt,sqrt,0),1).location(s);
		vertex[2]=new Line( Point.zero,new Minkowski(Math.sqrt(phi*sqrt),sqrt/phi,sqrt,0),1).location(s);
		vertex[3]=new Line( Point.zero,new Minkowski(Math.sqrt(sqrt/phi),-sqrt*phi,sqrt,0),1).location(s);
		vertex[4]=new Line( Point.zero,new Minkowski(-Math.sqrt(sqrt/phi),-sqrt*phi,sqrt,0),1).location(s);
		vertex[5]=new Line( Point.zero,new Minkowski(-Math.sqrt(phi*sqrt),sqrt/phi,sqrt,0),1).location(s);
		vertex[6]=new Line( Point.zero,new Minkowski(Math.sqrt(sqrt/phi),sqrt*phi,-sqrt,0),1).location(s);
		vertex[7]=new Line( Point.zero,new Minkowski(Math.sqrt(phi*sqrt),-sqrt/phi,-sqrt,0),1).location(s);
		vertex[8]=new Line( Point.zero,new Minkowski(0,-2*sqrt,-sqrt,0),1).location(s);
		vertex[9]=new Line( Point.zero,new Minkowski(-Math.sqrt(phi*sqrt),-sqrt/phi,-sqrt,0),1).location(s);
		vertex[10]=new Line( Point.zero,new Minkowski(-Math.sqrt(sqrt/phi),sqrt*phi,-sqrt,0),1).location(s);
		vertex[11]=new Line( Point.zero,new Minkowski(0,0,-1,0),1).location(s);
		center=Cellcomplex.centers(vertex, sidepoint);
		return new Cellcomplex(vertex,center,edgepoint,sidepoint);
	}
	private static Cellcomplex dodecahedron(double s) 
	{
		Point[] vertex=new Point[20],center;
		int[][]edgepoint=dodecahedronEdgepoint,
				sidepoint=dodecahedronSidepoint;
	double sqrt=Math.sqrt(3), phi=(Math.sqrt(5)+1)/2,psi3=Math.asin(Math.sqrt((5+2*Math.sqrt(5))/15)), psi4=0.188710531;
	for(int i=0;i<5;i++)
	{
		vertex[i]=new Line( Point.zero,Minkowski.polar(1.0, Math.PI/5*(2*i+1),psi3,0.0),1).location(s);
		vertex[i+5]=new Line( Point.zero,Minkowski.polar(1.0, Math.PI/5*(2*i+1),psi4,0.0),1).location(s);
		vertex[i+10]=new Line( Point.zero,Minkowski.polar(1.0, Math.PI/5*(2*i),-psi4,0.0),1).location(s);
		vertex[i+15]=new Line( Point.zero,Minkowski.polar(1.0, Math.PI/5*(2*i),-psi3,0.0),1).location(s);
	}
		
		
		center=Cellcomplex.centers(vertex, sidepoint);
		return new Cellcomplex(vertex,center,edgepoint,sidepoint);
	}
	private static Cellcomplex hexahedron(double s) 
	{
		Point[] vertex=new Point[8],center;
		int[][]edgepoint=hexahedronEdgepoint,
				sidepoint=hexahedronSidepoint;
		double sqrt=1/Math.sqrt(3);
		vertex[0]=new Line( Point.zero,new Minkowski(sqrt,sqrt,sqrt,0),1).location(s);
		vertex[1]=new Line( Point.zero,new Minkowski(-sqrt,sqrt,sqrt,0),1).location(s);
		vertex[2]=new Line( Point.zero,new Minkowski(-sqrt,-sqrt,sqrt,0),1).location(s);
		vertex[3]=new Line( Point.zero,new Minkowski(sqrt,-sqrt,sqrt,0),1).location(s);
		vertex[4]=new Line( Point.zero,new Minkowski(sqrt,sqrt,-sqrt,0),1).location(s);
		vertex[5]=new Line( Point.zero,new Minkowski(-sqrt,sqrt,-sqrt,0),1).location(s);
		vertex[6]=new Line( Point.zero,new Minkowski(-sqrt,-sqrt,-sqrt,0),1).location(s);
		vertex[7]=new Line( Point.zero,new Minkowski(sqrt,-sqrt,-sqrt,0),1).location(s);
	
		center=Cellcomplex.centers(vertex, sidepoint);
		return new Cellcomplex(vertex,center,edgepoint,sidepoint);
	}

	//vertex figure for an ngon, an mgon and a kgon meeting
	public static Cellcomplex trigon(int n, int m , int k)
	{
		Point[] vertex=new Point[3],center= {Point.zero};
		int[][] edgepoint= {{0,1},{1,2},{2,0}},
				sidepoint= {{0,1,2}};
		
		double a=Math.cos(Math.PI/n),
				b=Math.cos(Math.PI/m),
				c=Math.cos(Math.PI/k),		
				x=2*a*b*c/Math.sqrt(2*a*a*b*b+2*a*a*c*c+2*b*b*c*c-a*a*a*a-b*b*b*b-c*c*c*c),
				s=acosh(x)*2;
		vertex[0]=new Line(Point.zero,Minkowski.polar(1,0,0,0),1).location(s);

		vertex[1]=new Line(Point.zero,Minkowski.polar(1,2*Math.asin(a/x),0,0),1).location(s);

		vertex[2]=new Line(Point.zero,Minkowski.polar(1,-2*Math.asin(c/x),0,0),1).location(s);
		return new Cellcomplex(vertex,center,edgepoint,sidepoint);
	}
	//vertex figure for an ngon, an mgon and a kgon meeting
		public static Cellcomplex ngon(int[]n)
		{
			Point[] vertex=new Point[n.length],center= {Point.zero};
			int[][] edgepoint= new int[n.length][2],
					sidepoint= new int[1][n.length];
			
			double[] a=new double[n.length];
			for(int i=0;i<n.length;i++)
			{
				
				edgepoint[i][0]=i;
				edgepoint[i][1]=(i+1)%n.length;
				sidepoint[0][i]=i;
				a[i]=Math.cos(Math.PI/n[i]);
			}
			
			double x=1,oldx=0;
			
			//Newton's algorithm
			while(Math.abs(x-oldx)>1E-12)
			{
				oldx=x;
			//	System.out.println("x="+x);
				double f=-Math.PI,fprime=0;
				for(int i=0;i<n.length;i++)
				{
					f+=Math.asin(a[i]/x);
					fprime-=a[i]/x/Math.sqrt(x*x-a[i]*a[i]);
				}
				x-=f/fprime;
			}
			

				double	s=acosh(x)*2,//cosh(s/2)=x
						angle=0;
				for(int i=0;i<n.length;i++)
				{
					vertex[i]=new Line(Point.zero,Minkowski.polar(1,angle,0,0),1).location(s);
					angle+=2*Math.asin(a[i]/x);
				}

			return new Cellcomplex(vertex,center,edgepoint,sidepoint);
		}
		
		public static Cellcomplex pyramid(int[]facetype)
		{
			int n=facetype.length/2;
			Point[] vertex=new Point[n+1],
					center;
			int[][]edgepoint=new int[n*2][2],
					sidepoint=new int[n][3];
			for(int i=0;i<n;i++)
			{
				int j=(i+1)%n;
				edgepoint[i][0]=0;
				edgepoint[i][1]=i+1;
				edgepoint[i+n][0]=i+1;
				edgepoint[i+n][1]=j+1;
			}
			double[]a=new double[2*n];
			for(int i=0;i<2*n;i++)a[i]=Math.cos(Math.PI/facetype[i]);
			double z=1.1,oldz=0,f,fprime,step=0.05;
			double[]u=new double[2*n],altu=new double[2*n],v=new double[n],
					uprime=new double[2*n],altuprime=new double[2*n],vprime=new double[n];
			
			while(Math.abs(z-oldz)>1E-14)
			{
				//System.out.println("z="+z);
				oldz=z;
				if(z<1) {z=1+step;step/=2;}
				f=-2*Math.PI;
				fprime=0;
				for(int i=0;i<2*n;i++)
				{
					
					u[i]=a[i]/z;
					altu[i]=alt(u[i]);
					uprime[i]=-u[i]/z;
					altuprime[i]=-u[i]*uprime[i]/altu[i];
				//	System.out.println("u="+u[i]);
				//	System.out.println("altu="+altu[i]);
				//	System.out.println("u'="+uprime[i]);
				//	System.out.println("altu'="+altuprime[i]);
				}
				for(int i=0;i<n;i++)
				{
					int j=(i+1)%n;
					double num=(altu[i+n]*altu[i+n]-u[i+n]*u[i+n]-(altu[i]*altu[i]-u[i]*u[i])*(altu[j]*altu[j]-u[j]*u[j])),
							denom=(4*altu[i]*u[i]*altu[j]*u[j]),
							numprime=2*altu[i+n]*altuprime[i+n]-2*u[i+n]*uprime[i+n]-2*(altu[i]*altuprime[i]-u[i]*uprime[i])*(altu[j]*altu[j]-u[j]*u[j])-2*(altu[i]*altu[i]-u[i]*u[i])*(altu[j]*altuprime[j]-u[j]*uprime[j]),
							denomprime=4*(altuprime[i]*u[i]*altu[j]*u[j]+altu[i]*uprime[i]*altu[j]*u[j]+altu[i]*u[i]*altuprime[j]*u[j]+altu[i]*u[i]*altu[j]*uprime[j]);
					v[i]=num/denom;//(-altu[i]*altu[j]+altu[i+3])/u[i]/u[j];
					vprime[i]=(denom*numprime-num*denomprime)/(denom*denom);//(u[i]*u[j]*(-altuprime[i]*altu[j]-altuprime[j]*altu[i]+altuprime[i+3])-(u[i]*uprime[j]+uprime[i]*u[j])*(-altu[i]*altu[j]+altu[i+3]));
				if(Math.abs(v[i])>1)v[i]=Math.signum(v[i]);
					f+=Math.acos(v[i]);
					fprime-=vprime[i]/alt(v[i]);System.out.println("v="+v[i]);
				//	System.out.println("vprime="+vprime[i]);
				}
				//System.out.println("f="+f);
				//System.out.println("fprime="+fprime);

				
				z-=f/fprime;
				//System.out.println("z="+z);
			}
			double s=acosh(z)*2,angle=0;
			vertex[0]=new Line(Point.zero,Minkowski.polar(1, 0, Math.PI/2, 0),1).location(s);
			for(int i=0;i<n;i++)
			{
				vertex[i+1]=new Line(Point.zero,Minkowski.polar(1,angle,Math.PI/2-2*Math.asin(u[i]),0),1).location(s);
				angle+=Math.acos(v[i]);
			}
			
			center=Cellcomplex.centers(vertex, sidepoint);
			
			return new Cellcomplex(vertex,center,edgepoint,sidepoint);
		}
		
		public static Cellcomplex tetrahedron(int[]facetype)
		{
			Point[] vertex=new Point[4],
					center;
			int[][]edgepoint=tetrahedronEdgepoint,
					sidepoint=tetrahedronSidepoint;
			double[]a=new double[6];
			for(int i=0;i<6;i++)a[i]=Math.cos(Math.PI/facetype[i]);
			double z=1.1,oldz=0,f,fprime,step=0.05;
			double[]u=new double[6],altu=new double[6],v=new double[3],
					uprime=new double[6],altuprime=new double[6],vprime=new double[3];
			
			while(Math.abs(z-oldz)>1E-12)
			{
				//System.out.println("z="+z);
				oldz=z;
				if(z<1) {z=1+step;step/=2;}
				f=-2*Math.PI;
				fprime=0;
				for(int i=0;i<6;i++)
				{
					
					u[i]=a[i]/z;
					altu[i]=alt(u[i]);
					uprime[i]=-u[i]/z;
					altuprime[i]=-u[i]*uprime[i]/altu[i];
				//	System.out.println("u="+u[i]);
					//System.out.println("altu="+altu[i]);
				//	Sstem.out.println("u'="+uprime[i]);
				//	System.out.println("altu'="+altuprime[i]);
				}
				for(int i=0;i<3;i++)
				{
					int j=(i+1)%3;
					double num=(altu[i+3]*altu[i+3]-u[i+3]*u[i+3]-(altu[i]*altu[i]-u[i]*u[i])*(altu[j]*altu[j]-u[j]*u[j])),
							denom=(4*altu[i]*u[i]*altu[j]*u[j]),
							numprime=2*altu[i+3]*altuprime[i+3]-2*u[i+3]*uprime[i+3]-2*(altu[i]*altuprime[i]-u[i]*uprime[i])*(altu[j]*altu[j]-u[j]*u[j])-2*(altu[i]*altu[i]-u[i]*u[i])*(altu[j]*altuprime[j]-u[j]*uprime[j]),
							denomprime=4*(altuprime[i]*u[i]*altu[j]*u[j]+altu[i]*uprime[i]*altu[j]*u[j]+altu[i]*u[i]*altuprime[j]*u[j]+altu[i]*u[i]*altu[j]*uprime[j]);
					v[i]=num/denom;//(-altu[i]*altu[j]+altu[i+3])/u[i]/u[j];
					vprime[i]=(denom*numprime-num*denomprime)/(denom*denom);//(u[i]*u[j]*(-altuprime[i]*altu[j]-altuprime[j]*altu[i]+altuprime[i+3])-(u[i]*uprime[j]+uprime[i]*u[j])*(-altu[i]*altu[j]+altu[i+3]));
				if(Math.abs(v[i])>1)v[i]=Math.signum(v[i]);
					f+=Math.acos(v[i]);
					fprime-=vprime[i]/alt(v[i]);//System.out.println("v="+v[i]);
					//System.out.println("vprime="+vprime[i]);
				}
				//System.out.println("f="+f);
				//System.out.println("fprime="+fprime);

				
				z-=f/fprime;
				//System.out.println("z="+z);
			}
			double s=acosh(z)*2,angle=0;
			vertex[0]=new Line(Point.zero,Minkowski.polar(1, 0, Math.PI/2, 0),1).location(s);
			for(int i=0;i<3;i++)
			{
				vertex[i+1]=new Line(Point.zero,Minkowski.polar(1,angle,Math.PI/2-2*Math.asin(u[i]),0),1).location(s);
				angle+=Math.acos(v[i]);
			}
			
			center=Cellcomplex.centers(vertex, sidepoint);
			
			return new Cellcomplex(vertex,center,edgepoint,sidepoint);
		}
		
	public static Honeycomb archimedean(String type,int[]facetype,double faintness)
	{
		Cellcomplex vf;
		int[] edgetype;
		if(type.equals("trigon"))
			{
				vf=trigon(facetype[0],facetype[1],facetype[2]);
				edgetype=new int[vf.vertex.length];
				if(facetype[0]!=facetype[2])
				{
					edgetype[1]=1;
					if(facetype[1]==facetype[2])edgetype[2]=1;
					else if(facetype[1]!=facetype[0])edgetype[2]=2;
				}
				else if(facetype[0]!=facetype[1])edgetype[2]=1;		
			}
		else// if(type.equals("ngon"))
		{
			vf=ngon(facetype);
			int n=vf.vertex.length;
			edgetype=new int[vf.vertex.length];
			boolean done;
			int k=1;
			for(int i=1;i<vf.vertex.length;i++)
			{
				done=false;
				int a=facetype[i],b=facetype[(i+n-1)%n],c=b,d,j=i-1;
				while(!done&&j>-1)
				{
					d=c;
					c=facetype[(j+n-1)%n];
					if((a==c&&b==d)||(a==d&&b==c))done=true;
					else j--;
				}
				if(done)
				edgetype[i]=edgetype[j];
				else {edgetype[i]=k;k++;}
					
					
			}
		}
	/*	if (false)//else if(type.equals("tet")
		{
			vf=tetrahedron(facetype);
			int n=vf.vertex.length;
			edgetype=new int[n];
			boolean done;
	
			int a=facetype[0], b=facetype[1],c=facetype[2],d;
		
		}*/
	
		//	Cellcomplex.print(edgetype);
			return archimedean(vf,facetype,edgetype,faintness);
	}
	
	public static Honeycomb archimedean(Cellcomplex vf, int[]facetype,int[]edgetype,double faintness)
	{
		double s=Point.zero.distance(vf.vertex[0]);
		Honeycomb honey=new Honeycomb();
		int nedge,nside;
		Polychoron.faintness=faintness;
		Observer.range=faintness;
		double[] r=new double[vf.edgepoint.length];
		nedge=vf.vertex.length;
		nside=vf.edgepoint.length;
		Honeycomb.vf=vf;
		Honeycomb.edgetype=edgetype;
		Honeycomb.vfd=new double[vf.vertex.length][vf.vertex.length];
		for(int i=0;i<vf.vertex.length;i++)for(int j=0;j<i+1;j++) {Honeycomb.vfd[i][j]=vf.vertex[i].distance(vf.vertex[j]);System.out.println("vfd("+i+", "+j+")="+Honeycomb.vfd[i][j]);}
		honey.addVertex(Point.zero,nedge,nside);

			
			for(int i=0;i<nedge;i++)
			{
				honey.addVertex(vf.vertex[i],nedge,nside);
				honey.addarchEdge(0,i+1, edgetype[i]);
			}
	for(int i=0;i<nside;i++)
	{
		r[i]=acosh(1/Math.tan(Point.zero.angle(vf.vertex[vf.edgepoint[i][0]], vf.vertex[vf.edgepoint[i][1]])/2)/Math.tan(Math.PI/facetype[i]));
	}
		int k=0;
		while(k<honey.vertex.size())
		{	
			if(honey.getVertex(k).isIn())//(doit.get(k))
		{System.out.println("k="+k+", distance="+Point.zero.distance(honey.vertex.get(k)));
			int i,j;
			int[] pointedge=honey.pointedge.get(k);
			
			//Fill in missing edges
			boolean notdone;
			int mu=0;
			if(k>0)
				do{
				notdone=false;	
				int mom=honey.getEndCor(k,mu);
				
			if(mom>-1) {	
			Point mother=honey.getEnd(k, mu);
			for(int o=0;o<nedge;o++)if(honey.getEndCor(mom,o)>-1)
			{			
				Minkowski w=mother.direction(honey.getEnd(mom,o)).mirrortransport(1, mother.geodesic(honey.vertex.get(k)));
				int e=honey.getAltEdgeNr(mom,o);
				Point p=new Line( honey.vertex.get(k),w,s).location(1);
				boolean done=false;
				int u=0;
				while(!done&&u<nedge)
				{
					if(pointedge[u]>-1) {
					if(p.distance(honey.getEnd(k,u))<s/2)done=true;//edge already exists
					else u++;}
					else u++;
				}
				if(!done)
				{
					u=0;
					while(!done&&u<honey.vertex.size())
					{
						if(p.distance(honey.getVertex(u))<s/2)done=true;
						else u++;
					}
					if(done)honey.addarchEdge(k, u,edgetype[o]);//vertex exists
					else
					{
						honey.addVertex(p,nedge,nside);
						//doit.add(p.isIn());
						honey.addarchEdge(k, u,edgetype[o]);
					}
				}
				
			}
			else notdone=true;
			}
			else notdone=true;
			
			mu++;	}while(notdone&&mu<nedge);
			

			int ver0=honey.vertex.size();
			//for(int m=0;m<nedge;m++)
				//System.out.println("edge nr"+m+"of vertex nr"+k+": "+honey.pointedge.get(k)[m]);
			for(int l=0;l<vf.edgepoint.length;l++)
				{
					i=vf.edgepoint[l][0];
					j=vf.edgepoint[l][1];
					int n=facetype[l];
					//System.out.println("i="+i+", j="+j);
					if(honey.pointside.get(k)[l]==-1&&pointedge[i]>-1&&pointedge[j]>-1)
					{
						int[]sidepoint=new int[n];
					//	System.out.println("edge nr"+pointedge[i]+"("+honey.edgepoint.get(pointedge[i])[0]+","+honey.edgepoint.get(pointedge[i])[1]+")");
						int a=honey.edgepoint.get(pointedge[i])[1];
						if(a==k) {a=honey.edgepoint.get(pointedge[i])[0];}

					//	System.out.println("edge nr"+pointedge[j]+"("+honey.edgepoint.get(pointedge[j])[0]+","+honey.edgepoint.get(pointedge[j])[1]+")");
						int b=honey.edgepoint.get(pointedge[j])[1];
						if(b==k) {b=honey.edgepoint.get(pointedge[j])[0];}
						Point m=honey.vertex.get(a).middle(honey.vertex.get(b));
						Line line=honey.vertex.get(k).geodesic(m);
						line.setSpeed(1);
						Point center=line.location(r[l]);

					
						sidepoint[0]=k;
						sidepoint[1]=a;
						sidepoint[n-1]=b;
						int[] in= {2,n-2},mid= {k,k},nju= {a,b},old= {b,a},edgetypes= {edgetype[i],edgetype[j]};
						int it=0;
						
						while(in[0]<=in[1]+1) 
						{
						Minkowski w=honey.vertex.get(mid[it]).direction(honey.vertex.get(old[it])).mirrortransport(1, honey.vertex.get(mid[it]).geodesic(honey.vertex.get(nju[it])));
					//	System.out.println("wnorm="+w.hdot(w)+", old="+old[it]+", mid="+mid[it]+", nju="+nju[it]);
				//		honey.vertex.get(old[it]).print();
					//	honey.vertex.get(mid[it]).print();
					//	honey.vertex.get(nju[it]).print();
						Point p=new Line( honey.vertex.get(nju[it]),w,s).location(1);
						p.print();
				//	System.out.println("center to vertex="+p.distance(center));
					//	System.out.println("s "+s);
						int count=0, done=-1;int edge=honey.pointedge.get(nju[it])[count];
						while(count<nedge&&done<0)  //Case: edge already exists (Does this even happen??
						{
							if(edge>-1) {
						//	System.out.println("checking edge nr. "+edge+"("+honey.edgepoint.get(edge )[0]+", "+honey.edgepoint.get(edge )[1]+")");
							if(p.distance(honey.vertex.get(honey.edgepoint.get(edge )[0]))<s/2)done=0;
							else if(p.distance(honey.vertex.get(honey.edgepoint.get(edge )[1]))<s/2)done=1;
							else
							{
								count++;
								
							}}
							else count++;
							if(count<nedge)
								edge=honey.pointedge.get(nju[it])[count];
						}
						if(done>-1)		{
							int h=honey.edgepoint.get(edge)[done];
							//System.out.println("vertex exists as well as edge");
							sidepoint[in[it]]=h;
							in[it]+=Math.pow(-1, it);
							old[it]=mid[it];
							mid[it]=nju[it];
							nju[it]=h;
							it=1-it;
						}
						else {
							count=0;
						while(count<honey.vertex.size()&&done<0)
						{
							if(honey.vertex.get(count).distance(p)<s/2)done=1;
							else count++;
						}
						if(done>0)
						{
							sidepoint[in[it]]=count;
							//System.out.println("vertex exists but edge doesn't");
							boolean specialcase=(weirdcase&&n==5);
							if(specialcase)
							{
								honey.addlateEdge(count, nju[it],edgetypes[(in[it]+it+1)%2]);}
							else
							honey.addarchEdge(count, nju[it],edgetypes[(in[it]+it+1)%2]);
							
							in[it]+=Math.pow(-1, it);
							old[it]=mid[it];
							mid[it]=nju[it];
							nju[it]=count;
							it=1-it;
						}
						
						else {
						//	System.out.println("neither vertex nor edge exist");
							int h=honey.addVertex(p, nedge,nside);
							honey.addarchEdge(count, nju[it],edgetypes[(in[it]+it+1)%2]);
							honey.addarchEdge(h, nju[it],edgetypes[(it+1+in[it])%2]);
							sidepoint[in[it]]=h;
							in[it]+=Math.pow(-1, it);
							old[it]=mid[it];
							mid[it]=nju[it];
							nju[it]=h;
							it=1-it;
						}
						}
				
						}
						 honey.addFace(center,sidepoint);
						 
				//int drawIt=5000;	if(honey.sidepoint.size()%drawIt==0) {	honey.drawFaces(Image.eye, Image.image, Image.zBuffer, Image.color,drawIt);		Image.eye.finish(Image.image, "hrrect534");}
					}
					
				}
			
			
			int num=honey.vertex.size()-ver0;
		}k++;
		}
		return honey;
	}

	public static Honeycomb archimedean(String type,int[] facetype, int[] edgetype, double faintness) 
	{
		Cellcomplex vf;
		if(type.equals("trigon"))
			{
				vf=trigon(facetype[0],facetype[1],facetype[2]);
			}
		else if(type.equals("ngon"))
		{
			vf=ngon(facetype);
		}
			
		else if(type.equals("tet"))
			vf=tetrahedron(facetype);
		
		else vf=pyramid(facetype);
			return archimedean(vf,facetype,edgetype,faintness);
	}
	
	public static Honeycomb archimedean(String type, int[] facetype, int[] edgetype,int[]switchedge,int[][]chiralface, double faintness) 
	{
		Cellcomplex vf;
		if(type.equals("trigon"))
			{
				vf=trigon(facetype[0],facetype[1],facetype[2]);
			}
		else if(type.equals("ngon"))
		{
			vf=ngon(facetype);
		}
			
		else if(type.equals("tet"))
			vf=tetrahedron(facetype);
		
		else vf=pyramid(facetype);
			Cellcomplex.print(edgetype);
			return archimedean(vf,facetype,edgetype,switchedge,chiralface,faintness);
	}
	
	private static Honeycomb archimedean(Cellcomplex vf, int[] facetype, int[] edgetype, int[] switchedge,int[][]chiralface,
			double faintness) 
	{
		double s=Point.zero.distance(vf.vertex[0]);
		Honeycomb honey=new Honeycomb();
		int nedge,nside;
		Polychoron.faintness=faintness;
		Observer.range=faintness;
		double[] r=new double[vf.edgepoint.length];
		nedge=vf.vertex.length;
		nside=vf.edgepoint.length;
		Honeycomb.vf=vf;
		Honeycomb.edgetype=edgetype;
		Honeycomb.vfd=new double[vf.vertex.length][vf.vertex.length];
		for(int i=0;i<vf.vertex.length;i++)for(int j=0;j<i+1;j++) {Honeycomb.vfd[i][j]=vf.vertex[i].distance(vf.vertex[j]);System.out.println("vfd("+i+", "+j+")="+Honeycomb.vfd[i][j]);}
		honey.addVertex(Point.zero,nedge,nside);

			
			for(int i=0;i<nedge;i++)
			{
				honey.addVertex(vf.vertex[i],nedge,nside);
				honey.addarchEdge(0,i+1, edgetype[i], edgetype[switchedge[i]]);
			}
	for(int i=0;i<nside;i++)
	{
		r[i]=acosh(1/Math.tan(Point.zero.angle(vf.vertex[vf.edgepoint[i][0]], vf.vertex[vf.edgepoint[i][1]])/2)/Math.tan(Math.PI/facetype[i]));
	}
		int k=0;
		while(k<honey.vertex.size())
		{	
			if(honey.getVertex(k).isIn())//(doit.get(k))
		{//System.out.println("k="+k+", distance="+Point.zero.distance(honey.vertex.get(k)));
			int i,j;
			int[] pointedge=honey.pointedge.get(k);
			
			//Fill in missing edges
			boolean notdone;
			int mu=0;
			Minkowski mirdir=Minkowski.e1;
			if(k>0)
				do{
				notdone=false;	
				int mom=honey.getEndCor(k,mu);
				
			if(mom>-1) {	
			Point mother=honey.vertex.get( mom);	
			int toomanyparameternames=honey.getAltEdgeNr(k,mu);
			boolean doit=switchedge[mu]==mu;//&&honey.getEndCor(mom, 3)>-1&&honey.getEndCor(mom, 1)>-1;
			boolean mirroranyway=false;
			int[]momedge=honey.pointedge.get(mom);
			if(!doit)doit=switchedge[mu]!=mu&&momedge[switchedge[toomanyparameternames]]>-1;
			//System.out.println("doit="+doit);
			if(switchedge[mu]!=mu&&doit)
					{
			
					Point end=honey.getEnd(mom, switchedge[toomanyparameternames]);
					mirdir=mother.direction( honey.vertex.get(k));//.add(mother.direction(end).times(-1));

			//		System.out.print("prenormalized mirdir=");mirdir.println();
					mirdir=mirdir.add(mother.direction(end).times(-1));
					mirdir=mirdir.hnormalize();
					//System.out.println("mirdir from "+k+" to "+honey.getEndCor(mom, switchedge[toomanyparameternames])+", where mother="+mom);
					}
			else if(mirroranyway)
			{
				Point end=honey.getEnd(mom, 3),start=honey.getEnd(mom, 1);
				mirdir=mother.direction( start).add(mother.direction(end).times(-1));
				mirdir=mirdir.hnormalize();
			}
			System.out.print("mirdir=");mirdir.println();
			Line momline= mother.geodesic(honey.vertex.get(k));
			System.out.println("try mu="+mu+", mom="+mom);
			if(doit)
			{for(int o=0;o<nedge;o++)if(honey.getEndCor(mom,o)>-1)
			{		
				Minkowski w=Minkowski.e1;
			
				if(switchedge[mu]==mu&&!mirroranyway) 
				 w=mother.direction(honey.getEnd(mom,o)).mirrortransport(1, momline);
				else 
				{
				
						 w=mother.direction(honey.getEnd(mom,o)).mirror(mirdir).mirrortransport(1,momline);//tobecontinued...
				}
				
				System.out.print("mom="+mom+", edge nr "+o+", try direction " );
				w.println();
				Point p=new Line( honey.vertex.get(k),w,s).location(1);
				boolean done=false;
				int u=0;
				while(!done&&u<nedge)
				{
					if(pointedge[u]>-1) {
					if(p.distance(honey.getEnd(k,u))<s/2) {done=true;System.out.println("edge already exists, nr"+u);}
					else u++;}
					else u++;
				}
				if(!done)
				{
					u=0;
					while(!done&&u<honey.vertex.size())
					{
						if(p.distance(honey.getVertex(u))<s/2)done=true;
						else u++;
					}
					if(done)
						{
						if(switchedge[o]==o)
						honey.addarchEdge(k, u,o);//vertex exists
						else
							honey.addarchEdge(k, u,edgetype[o],edgetype[switchedge[o]]);
						}
					else
					{
						honey.addVertex(p,nedge,nside);
						if(switchedge[o]==o)
							honey.addarchEdge(k, u,edgetype[o]);//vertex exists
							else
								honey.addarchEdge(k, u,edgetype[o],edgetype[switchedge[o]]);
					}
				}
				
			}}
			else notdone=true;
			}
			else notdone=true;
			
			mu++;	}while(notdone&&mu<nedge);
			

			int ver0=honey.vertex.size();
			//for(int m=0;m<nedge;m++)
			//	System.out.println("edge nr"+m+"of vertex nr"+k+": "+honey.pointedge.get(k)[m]);
			for(int l=0;l<vf.edgepoint.length;l++)
				{
					i=vf.edgepoint[l][0];
					j=vf.edgepoint[l][1];
					int n=facetype[l];
					System.out.println("i="+i+", j="+j);
					if(honey.pointside.get(k)[l]==-1&&pointedge[i]>-1&&pointedge[j]>-1)
					{
						int[]sidepoint=new int[n];
					//	System.out.println("edge nr"+pointedge[i]+"("+honey.edgepoint.get(pointedge[i])[0]+","+honey.edgepoint.get(pointedge[i])[1]+")");
						int a=honey.edgepoint.get(pointedge[i])[1];
						if(a==k) {a=honey.edgepoint.get(pointedge[i])[0];}

					//	System.out.println("edge nr"+pointedge[j]+"("+honey.edgepoint.get(pointedge[j])[0]+","+honey.edgepoint.get(pointedge[j])[1]+")");
						int b=honey.edgepoint.get(pointedge[j])[1];
						if(b==k) {b=honey.edgepoint.get(pointedge[j])[0];}
						Point m=honey.vertex.get(a).middle(honey.vertex.get(b));
						Line line=honey.vertex.get(k).geodesic(m);
						line.setSpeed(1);
						Point center=line.location(r[l]);

						sidepoint[0]=k;
						sidepoint[1]=a;
						sidepoint[n-1]=b;
						int[] in= {2,n-2},mid= {k,k},nju= {a,b},old= {b,a};
						int it=0;
						
						while(in[0]<=in[1]+1) 
						{
						Minkowski w=honey.vertex.get(mid[it]).direction(honey.vertex.get(old[it])).mirrortransport(1, honey.vertex.get(mid[it]).geodesic(honey.vertex.get(nju[it])));
					//	System.out.println("wnorm="+w.hdot(w)+", old="+old[it]+", mid="+mid[it]+", nju="+nju[it]);
				//		honey.vertex.get(old[it]).print();
					//	honey.vertex.get(mid[it]).print();
					//	honey.vertex.get(nju[it]).print();
						Point p=new Line( honey.vertex.get(nju[it]),w,s).location(1);
						p.print();
				//	System.out.println("center to vertex="+p.distance(center));
					//	System.out.println("s "+s);
						int count=0, done=-1;int edge=honey.pointedge.get(nju[it])[count];
						while(count<nedge&&done<0)  //Case: edge already exists (Does this even happen??
						{
							if(edge>-1) {
							//System.out.println("checking edge nr. "+edge+"("+honey.edgepoint.get(edge )[0]+", "+honey.edgepoint.get(edge )[1]+")");
							if(p.distance(honey.vertex.get(honey.edgepoint.get(edge )[0]))<s/2)done=0;
							else if(p.distance(honey.vertex.get(honey.edgepoint.get(edge )[1]))<s/2)done=1;
							else
							{
								count++;
								
							}}
							else count++;
							if(count<nedge)
								edge=honey.pointedge.get(nju[it])[count];
						}
						if(done>-1)		{
							int h=honey.edgepoint.get(edge)[done];
							System.out.println("vertex exists as well as edge");
							sidepoint[in[it]]=h;
							in[it]+=Math.pow(-1, it);
							old[it]=mid[it];
							mid[it]=nju[it];
							nju[it]=h;
							it=1-it;
						}
						else {
							count=0;
						while(count<honey.vertex.size()&&done<0)
						{
							if(honey.vertex.get(count).distance(p)<s/2)done=1;
							else count++;
						}
						if(done>0)
						{
							sidepoint[in[it]]=count;
							System.out.println("vertex exists but edge doesn't");
							
							{
								int index=in[it]-1+it;
								int[]et= {chiralface[l][index],switchedge[chiralface[l][index]]};
								//System.out.println("et=("+et[0]+","+et[1]+")");
								honey.addarchEdge(nju[it],count, edgetype[et[it]],edgetype[et[1-it]] );
							}
							
							in[it]+=Math.pow(-1, it);
							old[it]=mid[it];
							mid[it]=nju[it];
							nju[it]=count;
							it=1-it;
						}
						
						else {
							System.out.println("neither vertex nor edge exist");
							int h=honey.addVertex(p, nedge,nside);
							
								{
									int index=in[it]-1+it;
									int[]et= {chiralface[l][index],switchedge[chiralface[l][index]]};
								//	System.out.println("et=("+et[0]+","+et[1]+")");
									honey.addarchEdge(nju[it],h, edgetype[et[it]],edgetype[et[1-it]]  );
								}
							sidepoint[in[it]]=h;
							in[it]+=Math.pow(-1, it);
							old[it]=mid[it];
							mid[it]=nju[it];
							nju[it]=h;
							it=1-it;
						}
						}
				
						}
						 honey.addFace(center,sidepoint);
						 
				//int drawIt=2000;	if(honey.sidepoint.size()%drawIt==0) {	honey.drawFaces(Image.eye, Image.image, Image.zBuffer, Image.color,drawIt);		Image.eye.finish(Image.image, "hr535");}
					}
					
				}
			
			
			int num=honey.vertex.size()-ver0;
		}k++;
		}
		return honey;
	}

	private static double alt(double v)
	{
		if(Math.abs(v)>1)return 0;
		else return Math.sqrt(1-v*v);
	}
	
	//only works if uniform-->if all edges of original ar identical
	public static Cellcomplex rectify(Cellcomplex original)
	{
		int[]  nb=new int[10], v=new int[10];
		int counter=0;
		Point mid=Point.zero.middle(original.vertex[0]);
		for(int i=0;i<original.edgepoint.length;i++)
		{
			if(original.edgepoint[i][0]==0||original.edgepoint[i][1]==0)
			{
				nb[counter]=i;
				v[counter]=Math.max(original.edgepoint[i][0], original.edgepoint[i][1]);
				counter++;
			}
		}
		
		Point[] vertex=new Point[counter*2],
				center;
		int[][]edgepoint=new int[3*counter][2],
				sidepoint=new int[counter][4];
	
		Line translate=mid.geodesic(Point.zero);
		Minkowski axis=translate.momentum(1);
		for(int i=0;i<counter;i++)
		{
			vertex[i]=translate.transport(mid.geodesic(Point.zero.middle(original.vertex[v[i]])), 1).location(1);
			Minkowski m=Point.zero.direction(vertex[i]);
			double length=vertex[i].distance(Point.zero);
			m=m.mirror(axis);
			
			vertex[counter+i]=new Line(Point.zero,m,length).location(1);
			edgepoint[i][0]=i;
			edgepoint[i][1]=(i+1)%counter;
			edgepoint[i+counter][0]=i+counter;
			edgepoint[i+counter][1]=(i+1)%counter+counter;
			edgepoint[i+2*counter][0]=i;
			edgepoint[i+2*counter][1]=i+counter;
			sidepoint[i][0]=i;
			sidepoint[i][1]=counter+i;
			sidepoint[i][2]=(1+i)%counter;
			sidepoint[i][1]=counter+(1+i)%counter;
		}
		center=Cellcomplex.centers(vertex,sidepoint);
		return new Cellcomplex(vertex,center,edgepoint,sidepoint);
	}
	public static Honeycomb modify(String mod,int n,int c,int e,double faintness)
	{
		double sqrt2=Math.sqrt(2), sqrt3=Math.sqrt(3);
		double s=2*acosh(sqrt3/sqrt2*Math.cos(Math.PI/n)),
				r=acosh(1/sqrt2/Math.tan(Math.PI/n));
		Honeycomb honey=new Honeycomb();
		int nedge,nside;
		vf=Polychoron.tetrahedron(s);
		Polychoron.faintness=faintness;
		Observer.range=faintness;
		if(e==2)
		{
			s=2*acosh(Math.cos(Math.PI/n)/Math.sin(Math.PI/c));
			r=acosh(1/Math.tan(Math.PI/c)/Math.tan(Math.PI/n));
			vf=Polychoron.ngon(c,s);
		}
		else if(e==4)
		{
			//if(c==3)
	
			s=2*acosh(Math.cos(Math.PI/n)/Math.sin(Math.PI/4));
			r=acosh(1/Math.tan(Math.PI/4)/Math.tan(Math.PI/n));
			vf=Polychoron.octahedron(s);
		}
		else if(e==5)
		{
			double phi=(Math.sqrt(5)+1)/2;
			double angle=Math.atan(1/phi);
			s=2*acosh(Math.cos(Math.PI/n)/Math.sin(angle));
			r=acosh(1/Math.tan(angle)/Math.tan(Math.PI/n));
			vf=Polychoron.icosahedron(s);
		}
		else if(c==4)//e=3
		{
			double angle=Math.asin(1/sqrt3);
			s=2*acosh(Math.cos(Math.PI/n)/Math.sin(angle));
			r=acosh(1/Math.tan(angle)/Math.tan(Math.PI/n));
			vf=Polychoron.hexahedron(s);
		}
		else if(c==5)//e=3
		{
			double angle=Math.atan((3-Math.sqrt(5))/2);
			s=2*acosh(Math.cos(Math.PI/n)/Math.sin(angle));
			r=acosh(1/Math.tan(angle)/Math.tan(Math.PI/n));
			vf=Polychoron.dodecahedron(s);
		}
	int[]facetype,edgetype;
		//if(mod.equals("rect"))
		{
			vf=rectify(vf);
			facetype=new int[vf.edgepoint.length];edgetype=new int[vf.vertex.length];
			int k=vf.edgepoint.length/3;
			for(int i=0;i<k;i++)
			{
				facetype[i]=c;
				facetype[i+k]=c;
				facetype[i+2*k]=n;
			}
			
		}
		nedge=vf.vertex.length;
		nside=vf.edgepoint.length;

		return archimedean(vf,facetype,edgetype,faintness);
	}
}
