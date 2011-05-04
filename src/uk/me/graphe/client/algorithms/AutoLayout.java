package uk.me.graphe.client.algorithms;

import java.util.HashMap;

import uk.me.graphe.client.Console;
import uk.me.graphe.client.VertexDrawable;
import uk.me.graphe.shared.graphmanagers.GraphManager2d;

public class AutoLayout
{
	private final GraphManager2d graphManager;
	
	private static final int x = 0, y = 1;	
	
	// TODO: set these constants.
	private static final float dampening = (float) 0.97;
	private static final float springConst = 0;
	private static final int springLengthConst = 0;
	private static final int repelConst = 100;

	private HashMap<VertexDrawable, float[]> vertexVelocity;
	private HashMap<VertexDrawable, int[]> vertexPosition;
		
	public AutoLayout(GraphManager2d gManager)
	{
		graphManager = gManager;
	}
	
	public void initialize()
	{
		vertexVelocity = new HashMap<VertexDrawable, float[]>(graphManager.getVertexDrawables().size());
		vertexPosition = new HashMap<VertexDrawable, int[]>(graphManager.getVertexDrawables().size());
		
		for (VertexDrawable v : graphManager.getVertexDrawables())
		{
			vertexVelocity.put(v, new float[] { 0, 0 });
			vertexPosition.put(v, new int[] { v.getCenterX(), v.getCenterY() });
		}		
	}
	
	public void run()
	{
		float[] force;
		final float[] netForce = new float[2];
		
		for (int i = 0; i < 100; i++)
		{
			for (VertexDrawable v1 : graphManager.getVertexDrawables())
			{
				netForce[x] = 0;
				netForce[y] = 0;
	
				for (VertexDrawable v2 : graphManager.getVertexDrawables())
				{
					if (!v1.equals(v2))
					{
						force = forceRepel (v1, v2); // Repel force of v2 acting on v1.
						netForce[x] += force[x];
						netForce[y] += force[y];
					}
				}
	
				vertexVelocity.get(v1)[x] += netForce[x];
				vertexVelocity.get(v1)[y] += netForce[y];
				
				vertexVelocity.get(v1)[x] *= dampening;
				vertexVelocity.get(v1)[y] *= dampening;
			}
			
			for (VertexDrawable v1 : graphManager.getVertexDrawables())
			{
	
				int posx = (int) (v1.getCenterX() + (vertexVelocity.get(v1)[x]));
				int posy = (int) (v1.getCenterY() + (vertexVelocity.get(v1)[y]));
				
				graphManager.moveVertexTo(graphManager.getVertexFromDrawable(v1), posx, posy);;
			}			
		}
	}
	
	private float[] forceRepel (VertexDrawable on, VertexDrawable by)
	{
		final float[] result = new float[2];
		int dist_x, dist_y, dir_x, dir_y;
		
		dist_x = on.getCenterX() - by.getCenterX();
		dist_y = on.getCenterY() - by.getCenterY();
		
		if (dist_x > 0)
		{
			dir_x = 1;
		}
		else
		{
			dir_x = -1;
		}
		
		if (dist_y > 0)
		{
			dir_y = 1;
		}
		else
		{
			dir_y = -1;
		}

		result[x] = (float) ((repelConst / Math.pow(dist_x, 2)) * dir_x);
		result[y] = (float) ((repelConst / Math.pow(dist_y, 2)) * dir_y);
		
		return result;
	}
	
	private float[] forceSpring (VertexDrawable v1, VertexDrawable v2)
	{
		final float[] result = new float[2];
		final double length;
		int dist_x, dist_y, dir_x, dir_y;
		
		dist_x = v1.getCenterX() - v2.getCenterX();
		dist_y = v1.getCenterY() - v2.getCenterY();
		
		length = Math.sqrt((dist_x ^ 2) + (dist_y ^ 2));
		
		if (dist_x > 0)
		{
			dir_x = 1;
		}
		else
		{
			dir_x = -1;
			dist_x *= -1;
		}
		
		if (dist_y > 0)
		{
			dir_y = 1;
		}
		else
		{
			dir_y = -1;
			dist_y *= -1;
		}
		
		result[x] = -springConst * dist_x * dir_x;
		result[y] = -springConst * dist_y * dir_y;
		
		return result;
	}
}