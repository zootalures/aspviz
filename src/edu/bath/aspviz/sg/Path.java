package edu.bath.aspviz.sg;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Path extends BoundedShape {
	List<Point2D.Double> points = new ArrayList<Point2D.Double>();
	boolean closed = false;

	public List<Point2D.Double> getPoints() {
		return points;
	}

	@Override
	Rectangle2D getBounds() {
		Rectangle2D.Double r2d = new Rectangle2D.Double();
		Point2D.Double botleft = new Point2D.Double(Double.POSITIVE_INFINITY,
				Double.POSITIVE_INFINITY);
		Point2D.Double topright = new Point2D.Double(Double.NEGATIVE_INFINITY,
				Double.NEGATIVE_INFINITY);
		for (Point2D.Double pt : points) {
			if (pt.x < botleft.x) {
				botleft.x = pt.x;
			}
			if (pt.y < botleft.y) {
				botleft.y = pt.y;
			}
			if (pt.x > topright.x) {
				topright.x = pt.x;
			}
			if (pt.y > topright.y) {
				topright.y = pt.y;
			}
		}

		// TODO Auto-generated method stub
		return new Rectangle2D.Double(botleft.x, botleft.y, topright.x
				- botleft.x, topright.y - botleft.y);
	}
}
