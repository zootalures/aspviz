package edu.bath.aspviz.sg.expr;

import java.util.concurrent.Callable;

public class Sin implements Callable<Double> {
	Callable<Double> value;

	@Override
	public Double call() throws Exception {
		return Math.sin(value.call());
	}
}
