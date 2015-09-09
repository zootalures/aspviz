package edu.bath.aspviz.model;

import java.util.List;

import edu.bath.asplib.model.as.AnswerSet;

public class Rendering {
	AnswerSet baseAnswerSet;
	public AnswerSet getBaseAnswerSet() {
		return baseAnswerSet;
	}

	public void setBaseAnswerSet(AnswerSet baseAnswerSet) {
		this.baseAnswerSet = baseAnswerSet;
	}

	List<AnswerSet> answerSets;

	public List<AnswerSet> getAnswerSets() {
		return answerSets;
	}

	public void setAnswerSets(List<AnswerSet> answerSets) {
		this.answerSets = answerSets;
	}

}
