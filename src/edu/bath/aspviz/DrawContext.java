package edu.bath.aspviz;

import java.util.LinkedList;
import java.util.List;

public class DrawContext {

	@SuppressWarnings("unchecked")
	List<DrawCall> postActions = new LinkedList<DrawCall>();
	
	public List<DrawCall> getPostActions() {
		return postActions;
	}

	public void setPostActions(List<DrawCall> postActions) {
		this.postActions = postActions;
	}

	public void addPostCall(DrawCall dc){
		postActions.add(dc);
	}

}
