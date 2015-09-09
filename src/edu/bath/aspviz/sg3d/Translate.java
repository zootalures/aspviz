package edu.bath.aspviz.sg3d;

import javax.media.opengl.GL;

import edu.bath.aspviz.sg.ContextAction;

public class Translate implements ContextAction<GL> {
	public Translate(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	double x,y,z;
	@Override
	public void act(GL gl) {
		gl.glTranslated(x,y,z);
	}

}
