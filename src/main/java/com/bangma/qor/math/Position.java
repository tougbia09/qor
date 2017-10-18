package com.bangma.qor.math;

public class Position {
	public final int x;
	public final int y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int[] getVector() {
		return new int[] {x, y};
	}
}
