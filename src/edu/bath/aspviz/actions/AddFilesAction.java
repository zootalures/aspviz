package edu.bath.aspviz.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import edu.bath.aspviz.MainWindow;
import edu.bath.aspviz.model.ProblemModel;

public class AddFilesAction extends Action {
	MainWindow window;

	public AddFilesAction(MainWindow w) {
		setText("Add files");

		window = w;
		setId("add");
	}

	public void run() {
		FileDialog fd = new FileDialog(window.getShell(), SWT.MULTI);

		String result = fd.open();
		if (result!=null) {
			ProblemModel currentModel = window.getModel();
			if (currentModel == null) {
				currentModel = new ProblemModel();
			}
			for (String fn : fd.getFileNames()) {
				currentModel.getInputFilenames().add(
						fd.getFilterPath() + java.io.File.separator + fn);

			}

			if (currentModel.getInputFilenames().size() > 0) {
				window.setModel(currentModel);
			}
		}

	}

}
