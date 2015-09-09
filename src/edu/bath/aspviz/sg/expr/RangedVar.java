package edu.bath.aspviz.sg.expr;

public class RangedVar<T extends Number> extends Variable<T> {
	T min; 
	public RangedVar(T min, T max) {
		super();
		this.min = min;
		this.max = max;
	}
	public T getMin() {
		return min;
	}
	public void setMin(T min) {
		this.min = min;
	}
	public T getMax() {
		return max;
	}
	public void setMax(T max) {
		this.max = max;
	}
	T max;
}
