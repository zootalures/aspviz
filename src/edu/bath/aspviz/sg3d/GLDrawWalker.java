package edu.bath.aspviz.sg3d;

import javax.media.opengl.GL;

import edu.bath.aspviz.sg.DrawWalker;
import edu.bath.aspviz.sg.Node;

public class GLDrawWalker extends DrawWalker<GL> {

	public void startContext(GL c, Node<GL> curNode) {
		if (curNode.getAttributes().size() > 0) {
			c.glPushAttrib(GL.GL_ALL_ATTRIB_BITS);
		}
		if (curNode.getTransforms().size() > 0)
			c.glPushMatrix();

	};

	public void endContext(GL c, Node<GL> curNode) {
		if (curNode.getTransforms().size() > 0)
			c.glPopMatrix();
		if (curNode.getAttributes().size() > 0) {
			c.glPopAttrib();
		}
	};
}
