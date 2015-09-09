package edu.bath.aspviz;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;

import edu.bath.asplib.model.as.AnswerSet;

public class VizPainter implements PaintListener {
	Logger log = Logger.getLogger(getClass());
	AnswerSet answerSet;
	DrawContextFactory factory;
	
	Renderer renderer ;
	public VizPainter(AnswerSet answerSet,DrawContextFactory f,Renderer renderer) {
		this.answerSet = answerSet;
		this.factory =f;
		this.renderer = renderer;
		
	}

	@Override
	public void paintControl(PaintEvent event) {
		GC gc = event.gc;
		if(gc==null){
			return;
		}
		DrawContext context = factory.createDC(gc);
		log.debug("rendering answer set " + answerSet.toString());
		renderer.render(context,null,answerSet);
	}
}
