package com.aed.demo.cluster;

import java.util.Arrays;

/**
 * The Class Boundary.
 */
public class Boundary {
	
	/**
	 * Instantiates a new boundary.
	 */
	public Boundary()
	{
	}
    
    /** The points. */
    private  Point[] points ; // Points making up the boundary
    
    /** The point counter. */
    private int point_counter=0;
    
    /**
     * Contains.
     *
     * @param test the test
     * @return true, if successful
     */
    public boolean contains(Point test) {
      int i;
      int j;
      boolean result = false;
      for (i = 0, j = points.length - 1; i < points.length; j = i++) {
        if ((points[i].y > test.y) != (points[j].y > test.y) &&
            (test.x < (points[j].x - points[i].x) * (test.y - points[i].y) / (points[j].y-points[i].y) + points[i].x)) {
          result = !result;
         }
      }
      return result;
    }
    
    /**
     * Inits the point size.
     *
     * @param size the size
     * @return true, if successful
     */
    public boolean initPointSize(int size)
    {
    	points = new Point[size];
    	return true;
    }
    
    /**
     * Load point.
     *
     * @param point the point
     * @return true, if successful
     */
    public boolean loadPoint(Point point)
    {
    	points[point_counter] = point;
    	point_counter++;
    	//System.out.println("Point Loaded: "+point);
    	return true;
    }
    
    /**
     * Gets the points.
     *
     * @return the points
     */
    public Point[] getPoints()
    {
    	return points;
    }

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Boundary [points=" + Arrays.toString(points) + ", point_counter=" + point_counter + "]";
	}
    
    
    
    
}