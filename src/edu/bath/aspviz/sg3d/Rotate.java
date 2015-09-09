package edu.bath.aspviz.sg3d;

import javax.media.opengl.GL;

import edu.bath.aspviz.sg.ContextAction;

public class Rotate implements ContextAction<GL> {

	public Rotate(double angle, double x, double y, double z) {
		super();
		this.angle = angle;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	double angle, x, y, z;

	@Override
	public void act(GL gl) {

		gl.glRotated(angle, x, y, z);
	}

}
