package edu.bath.aspviz.sg;

import org.apache.log4j.Logger;


public abstract class DrawWalker<Context extends Object> {
	public abstract void startContext(Context c,Node<Context> curNode);
	public abstract void endContext(Context c,Node<Context> curNode);
	Logger log = Logger.getLogger(getClass());
	
	public void draw(Context gl, Node<Context> node){
		log.trace("starting draw on "+ node);
		startContext(gl,node);
		for(ContextAction<Context> attr: node.getAttributes()){
			attr.act(gl);
		}

		for(ContextAction<Context> trans: node.getTransforms()){
			trans.act(gl);
		}
		
		for(ContextAction<Context> shape: node.getShapes()){
			shape.act(gl);
		}

		for(Node<Context> child: node.getChildren()){
			draw(gl,child);
		}
		
		endContext(gl,node);
		log.trace("ending draw on "+ node);

	}
}
