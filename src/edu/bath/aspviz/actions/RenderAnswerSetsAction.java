package edu.bath.aspviz.actions;

import org.eclipse.jface.action.Action;

import edu.bath.aspviz.MainWindow;
import edu.bath.aspviz.model.VizModel;

public class RenderAnswerSetsAction extends Action {

	MainWindow mainWindow;

	public RenderAnswerSetsAction(MainWindow window) {
		this.mainWindow = window;
		setText("&Render@Ctrl+r");
	}

	public void run() {
		if(mainWindow.getVizModel()!=null){
			VizModel model = mainWindow.getVizModel();
		}
		mainWindow.setStatus("starting rendering");
		mainWindow.renderCurrentModel();
		mainWindow.setStatus("");
	}

}
