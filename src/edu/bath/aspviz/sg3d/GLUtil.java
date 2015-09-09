package edu.bath.aspviz.sg3d;

import javax.media.opengl.GL;

/*
 * Thread local GL hack for handling 
 */
public class GLUtil {

	static ThreadLocal<GL> localGL = new ThreadLocal<GL>();

	public static void setGL(GL gl){
		localGL.set(gl);
	}
	
	public static GL getGL(){
		return localGL.get();
	}
	public static GL GL(){
		GL r = localGL.get();
		if(r == null){
			throw new NullPointerException("Attempt to access GL when it was not set");
		}
		return r;
	}
}
