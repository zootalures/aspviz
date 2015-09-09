package edu.bath.aspviz.lang;

import edu.bath.asplib.model.Literal;
import edu.bath.asplib.model.Stereotype;
import edu.bath.aspviz.DrawContext;

public abstract class DrawAction<RContext extends DrawContext,Surface extends Object> {
	Stereotype stereotype;
	public static final int FIRST = 0;
	public static final int SECOND= 1000;
	public static final int THIRD= 10000;
	public static final int LAST= 1000000;
	
	public Stereotype getStereotype() {
		return stereotype;
	}

	public void setStereotype(Stereotype stereotype) {
		this.stereotype = stereotype;
	}

	public int getPrecedence() {
		return precedence;
	}

	public void setPrecedence(int precedence) {
		this.precedence = precedence;
	}

	int precedence;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + precedence;
		result = prime * result
				+ ((stereotype == null) ? 0 : stereotype.hashCode());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DrawAction other = (DrawAction) obj;
		if (precedence != other.precedence)
			return false;
		if (stereotype == null) {
			if (other.stereotype != null)
				return false;
		} else if (!stereotype.equals(other.stereotype))
			return false;
		return true;
	}

	public DrawAction(Stereotype stereotype, int precedence) {
		this.stereotype = stereotype;
		this.precedence = precedence;
	}

	public abstract void act(RContext ctx,Surface sfc, Literal literal);
}