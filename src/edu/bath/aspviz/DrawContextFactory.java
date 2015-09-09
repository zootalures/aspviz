package edu.bath.aspviz;


public interface DrawContextFactory<DC extends DrawContext,DCIN extends Object> {
	DC createDC(DCIN obj);
}
