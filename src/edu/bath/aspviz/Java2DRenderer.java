package edu.bath.aspviz;
import java.awt.Graphics2D;

import org.eclipse.swt.graphics.GC;

import edu.bath.aspviz.lang.java2d.TwoDRenderer;
import edu.bath.aspviz.lang.java2d.TwoDVizContext;

public class Java2DRenderer implements Renderer<TwoDVizContext, GC> {
	Graphics2DRenderer renderer = new Graphics2DRenderer();
	TwoDRenderer twodr = new TwoDRenderer();
	
	public void render(TwoDVizContext ctx, GC gc, edu.bath.asplib.model.as.AnswerSet answerSet) {
		renderer.prepareRendering(gc);
		Graphics2D g2d = renderer.getGraphics2D();
		twodr.render(ctx, g2d, answerSet);
		
	    renderer.render(gc);
	};

}
