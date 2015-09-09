package edu.bath.aspviz.model;

import java.util.ArrayList;
import java.util.List;

import edu.bath.asplib.tool.SolverConfiguration;

public class ProblemModel {
	List<String> inputFilenames = new ArrayList<String>();
	List<String> vizFilenames = new ArrayList<String>();
		public List<String> getVizFilenames() {
		return vizFilenames;
	}

	public void setVizFilenames(List<String> vizFilenames) {
		this.vizFilenames = vizFilenames;
	}

	public List<String> getInputFilenames() {
		return inputFilenames;
	}

	public void setInputFilenames(List<String> filenames) {
		this.inputFilenames = filenames;
	}
}
