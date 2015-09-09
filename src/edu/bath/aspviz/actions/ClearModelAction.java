package edu.bath.aspviz.actions;

import org.eclipse.jface.action.Action;

import edu.bath.aspviz.MainWindow;

public class ClearModelAction extends Action {
	MainWindow window;

	public ClearModelAction(MainWindow w) {
		setText("Clear model");
		window = w;
	}

	public void run() {
		window.setModel(null);

	}

}
