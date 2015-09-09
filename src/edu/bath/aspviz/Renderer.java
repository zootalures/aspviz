package edu.bath.aspviz;

import edu.bath.asplib.model.as.AnswerSet;

public interface Renderer<CtxType extends DrawContext, Surface extends Object> {
	public void render(CtxType c, Surface s,AnswerSet answerSet);
}
