package edu.bath.aspviz.sg3d;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import com.sun.opengl.util.GLUT;

import edu.bath.aspviz.sg.ContextAction;

public class GLCube extends GLShape {
	public void act(GL gl) {
		GLUT glut = new GLUT();
		
		glut.glutSolidCube(1.0f);
	};
}
