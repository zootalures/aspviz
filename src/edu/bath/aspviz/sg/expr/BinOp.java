package edu.bath.aspviz.sg.expr;

import java.util.concurrent.Callable;

public class BinOp implements Callable<Double> {
	enum Type {
		PLUS, MINUS, TIMES, DIV
	};

	Type type;
	Callable<Double> lhs;
	Callable<Double> rhs;

	@Override
	public Double call() throws Exception {
		Double lhsv = lhs.call();
		Double rhsv = lhs.call();

		switch (type) {
		case PLUS:
			return lhsv + rhsv;
		case MINUS:
			return lhsv - rhsv;

		case TIMES:
			return lhsv * rhsv;
		case DIV:
			return lhsv / rhsv;
		default:
			throw new RuntimeException("Unknown type");
		}
	}
}
