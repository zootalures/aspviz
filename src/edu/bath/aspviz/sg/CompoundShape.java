package edu.bath.aspviz.sg;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class CompoundShape extends BoundedShape {
	List<BoundedShape> children = new ArrayList<BoundedShape>();

	@Override
	Rectangle2D getBounds() {
		if (children.size() == 0) {
			return null;
		}
		Rectangle2D box = null;
		for (BoundedShape bs : children) {
			if (box == null) {
				box = bs.getBounds();
			} else {
				box.add(bs.getBounds());
			}
		}
		return box;
	}

}
