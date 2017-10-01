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
}
