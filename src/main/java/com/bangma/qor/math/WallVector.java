package com.bangma.qor.math;

/**
 * Mathematical description of walls.
 * @author tim
 *
 */
public class WallVector {
	public final int a;
	public final int b;
	public final char o;
	
	/**
	 * create a new Vector3i
	 * @param a node id (edge)
	 * @param b node id (center)
	 * @param c node id (edge)
	 */
	public WallVector(int a, int b, char o) {
		this.a = a;
		this.b = b;
		this.o = o;
	}
	
	/**
	 * return the points in order, as an array.
	 * @return int[] of points (a, b)
	 */
	public int[] getVector() {
		return new int[] {a, b};
	}
	
	/**
	 * Check if vector w is the same as this
	 * @param w vector to check against
	 * @return true if same, false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		WallVector w = (WallVector) o;
		return this.a == w.a && this.b == w.b;
	}
	
	/**
	 * Operation to subtract v from a and b
	 * @param v value to subtract
	 * @return new WallVector with a-v, b-v
	 */
	public WallVector sub(int v) {
		return new WallVector(a-v,b-v,o);
	}
	

	/**
	 * Operation to add v to a and b
	 * @param v value to add
	 * @return new WallVector with a+v, b+v
	 */
	public WallVector add(int v) {
		return new WallVector(a+v,b+v,o);
	}
	
	/**
	 * Create a WallVector object from a position on a grid
	 * @param p the (x,y) Position object
	 * @param orientation the orientation of the new WallVector
	 * @param g the graph to create with
	 * @return a new WallVector appropriate for Graph g.
	 */
	public static WallVector createWallVector(Position p, char orientation, Graph g) {
		int a = g.convertTupleToId(p);
		int b;
		if (orientation == 'h') {
			if (p.x == g.getWidth() - 1) b = a - 1;
			else b = a + 1;
		} else {
			if (p.y == g.getWidth() - 1) b = a - g.getWidth();
			else b = a + g.getWidth();
		}
		return new WallVector(a, b, orientation);
	}
}
