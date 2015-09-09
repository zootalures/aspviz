package edu.bath.aspviz.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.ApplicationWindow;

public class ExitAction extends Action {
	ApplicationWindow window;

	public ExitAction(ApplicationWindow w) {
		setText("E&xit@Ctrl+w");
		window = w;
	}

	public void run() {
		window.close();
	}

}
