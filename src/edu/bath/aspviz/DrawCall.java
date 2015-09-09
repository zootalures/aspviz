package edu.bath.aspviz;

public interface DrawCall<DCTX extends DrawContext,Surface extends Object> {

	public void draw(DCTX ctx,Surface sfc);
}
