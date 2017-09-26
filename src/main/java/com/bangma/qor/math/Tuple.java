package com.bangma.qor.math;

public class Tuple<T extends Number> {
	T[] values;
	@SuppressWarnings("unchecked")
	public Tuple(T x, T y) {
		values = (T[]) new Number[] {x, y};
	}
	
	public T x() {
		return values[0];
	}
	public T y() {
		return values[1];
	}
	public T[] getVector() {
		return values;
	}
}
