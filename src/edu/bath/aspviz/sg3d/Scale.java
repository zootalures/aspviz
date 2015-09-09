package edu.bath.aspviz.sg3d;

import javax.media.opengl.GL;

import edu.bath.aspviz.sg.ContextAction;

public class Scale implements ContextAction<GL> {
	public Scale(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}


	double x,y,z;
	
	
	@Override
	public void act(GL ctx) {
		ctx.glScaled(x,y,z);
	}
}
