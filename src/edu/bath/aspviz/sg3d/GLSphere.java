package edu.bath.aspviz.sg3d;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

public class GLSphere extends GLShape{
	
	@Override
	public void act(GL gl) {
		GLU glu = new GLU();
		GLUquadric quad = glu.gluNewQuadric();
		glu.gluSphere(quad, 0.5, 30, 30);
		
	}

}
