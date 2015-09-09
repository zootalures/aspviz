package edu.bath.aspviz.lang.java2d;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.bath.aspviz.Brush;

public class Polygon {
	static class PolyLine {
		public PolyLine(Point2D.Double p1, Point2D.Double p2) {
			this.p1 = p1;
			this.p2 = p2;
		}

		Point2D.Double p1;
		Point2D.Double p2;
	}

	Color fillColor;
	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public Brush getLineBrush() {
		return lineBrush;
	}

	public void setLineBrush(Brush lineBrush) {
		this.lineBrush = lineBrush;
	}

	Brush lineBrush;
	List<PolyLine> polyLines = new LinkedList<PolyLine>();

	public void addPolyLine(Point2D.Double p1, Point2D.Double p2) {
		polyLines.add(new PolyLine(p1, p2));

	}

	public Point2D.Double[] toPolyPath(){
		List<PolyLine> polyLines = new LinkedList<PolyLine>();
		polyLines.addAll(this.polyLines);
		List<Point2D.Double> points = new LinkedList<Point2D.Double>();
		
		if(polyLines.size()==0){
			return new Point2D.Double[]{};
		}
		Point2D.Double startPoint ;	
		Point2D.Double endPoint ;
		PolyLine pl = polyLines.remove(0);
		startPoint = pl.p1;
		endPoint = pl.p2;
		points.add(0,startPoint);
		points.add(endPoint);
		
		while(polyLines.size()>0){
			
			boolean changed = false;
			Iterator<PolyLine> iter= polyLines.iterator();
			while(iter.hasNext()){
				PolyLine newpl  = iter.next();
				if(newpl.p1.equals(startPoint)){
					points.add(0,newpl.p2);
					startPoint = newpl.p2;
					iter.remove();
					changed = true;
				}else if (newpl.p1.equals(endPoint)){
					points.add(newpl.p2);
					endPoint = newpl.p2;
					iter.remove();
					changed = true;
				}else if (newpl.p2.equals(startPoint)){					
					points.add(0,newpl.p1);
					startPoint = newpl.p1;
					iter.remove();
					changed = true;
				}else if (newpl.p2.equals(endPoint)){
					points.add(newpl.p1);
					endPoint = newpl.p1;
					iter.remove();
					changed = true;
				}
			}
			if(!changed){
				pl = polyLines.remove(0);
				startPoint = pl.p1;
				endPoint = pl.p2;
			}
			
		}
		
		return points.toArray( new Point2D.Double[]{});
	}
}
