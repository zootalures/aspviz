package edu.bath.aspviz.sg3d;

import java.awt.Color;

import javax.media.opengl.GL;

import edu.bath.aspviz.sg.ContextAction;

public class GLColor implements ContextAction<GL> {

	double r,g,b;

	
	public GLColor(double r, double g, double b) {
		super();
		this.r = r;
		this.g = g;
		this.b = b;
	}


	public void act(GL ctx) {
		ctx.glColor3d(r,g, b);
		ctx.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_DIFFUSE, new float[]{(float)r,(float)g,(float)b},0);
	
	};
}
