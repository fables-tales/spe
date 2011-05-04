package uk.me.graphe.client.algorithms;

import java.util.HashMap;

import uk.me.graphe.client.VertexDrawable;
import uk.me.graphe.shared.graphmanagers.GraphManager2d;

public class AutoLayout
{
	private final GraphManager2d graphManager;
	private final int x = 0, y = 1;
	
	public AutoLayout(GraphManager2d gManager)
	{
		graphManager = gManager;
	}
	
	public void run()
	{
		float[] force = new float[2], fIs, fH;
		
		HashMap<VertexDrawable, float[]> velocity = new HashMap<VertexDrawable, float[]>();
		
		for (VertexDrawable v1 : graphManager.getVertexDrawables())
		{
			velocity.put(v1, new float[2]);
		}
		
		for (int i = 0; i < 100; i++)
		{
		for (VertexDrawable v1 : graphManager.getVertexDrawables())
		{
			force[x] = 0;
			force[y] = 0;

			for (VertexDrawable v2 : graphManager.getVertexDrawables())
			{
				if (!v1.equals(v2))
				{
					fIs = forceGravity (v1, v2);
					force[x] += fIs[x];
					force[y] += fIs[y];
										
					if (graphManager.isEdgeBetween(graphManager.getVertexFromDrawable(v1),graphManager.getVertexFromDrawable(v2)))
					{
						fH = forceHooke (v1, v2);
						force[x] += fH[x];
						force[y] += fH[y];
					}
				}
			}

			velocity.get(v1)[x] += force[x];
			velocity.get(v1)[y] += force[y];
		}
		
		for (VertexDrawable v1 : graphManager.getVertexDrawables())
		{

			int posx = (int) (v1.getCenterX() + velocity.get(v1)[x]);
			int posy = (int) (v1.getCenterY() + velocity.get(v1)[y]);
			
			graphManager.moveVertexTo(graphManager.getVertexFromDrawable(v1), posx, posy);;
		}
		
		}
		graphManager.invalidate();
	}
	
	private float[] forceGravity (VertexDrawable on, VertexDrawable by)
	{
		float[] result = new float[2];
		int dist_x, dist_y, dir_x, dir_y;
		
		dist_x = by.getCenterX() - on.getCenterX();
		dist_y = by.getCenterY() - on.getCenterY();
		
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

		result[x] = .00000001f * (dist_x ^ 2) * dir_x;
		result[y] = .00000001f * (dist_y ^ 2) * dir_y;
		
		return result;
	}
	
	public static final float ReplusionConst = 2;
	
	private float[] forceInverseSquare (VertexDrawable on, VertexDrawable by)
	{
		float[] result = new float[2];
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
		
		if ((dist_x == 0) & (dist_y == 0))
		{
			result[x] = 0;
			result[y] = 0;
		}
		else
		{
			result[x] = (float) ((ReplusionConst / (dist_x ^ 2)) * dir_x);
			result[y] = (float) ((ReplusionConst / (dist_y ^ 2)) * dir_y);
		}		
		
		return result;
	}
	
	public static final float HookeConst = .000005f;
	
	private float[] forceHooke (VertexDrawable on, VertexDrawable by)
	{
		float[] result = new float[2];
		int dist_x, dist_y, dir_x, dir_y;
		
		dist_x = by.getCenterX() - on.getCenterX();
		dist_y = by.getCenterY() - on.getCenterY();
		
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
		
		result[x] = HookeConst * dist_x * dir_x;
		result[y] = HookeConst * dist_y * dir_y;
		
		return result;
	}
}