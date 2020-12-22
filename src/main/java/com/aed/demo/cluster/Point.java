package com.aed.demo.cluster;

/**
 * The Class Point.
 */
public class Point {
	  
  	/** The x. */
  	public  double x ;
	  
  	/** The y. */
  	public  double y;
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}
	
	/**
	 * Instantiates a new point.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	  
	 
	}