package edu.bath.aspviz.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



public class VizModel {
	List<File> searchPaths = new ArrayList<File>();
	
	public List<File> getSearchPaths() {
		return searchPaths;
	}

	public void setSearchPaths(List<File> searchPaths) {
		this.searchPaths = searchPaths;
	}

	ArrayList<Rendering> renderings= new ArrayList<Rendering>();

	public ArrayList<Rendering> getRenderings() {
		return renderings;
	}

	public void setRenderings(ArrayList<Rendering> renderings) {
		this.renderings = renderings;
	}
	
}
