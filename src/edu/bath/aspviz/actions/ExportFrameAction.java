package edu.bath.aspviz.actions;

import java.io.File;
import java.io.FileOutputStream;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.FileDialog;

import edu.bath.asplib.model.as.AnswerSet;
import edu.bath.aspviz.MainWindow;
import edu.bath.aspviz.SVGRenderer;

public class ExportFrameAction extends Action {

	MainWindow mainwindow;

	public ExportFrameAction(MainWindow mainwindow) {
		this.mainwindow = mainwindow;
		setText("Ex&port@Ctrl+x");
	}

	@Override
	public void run() {
		FileDialog dialog = new FileDialog(mainwindow.getShell());

		dialog.setText("Select a filename to write to");
		dialog.setFilterExtensions(new String[] { "*.svg" });
		String result = dialog.open();
		String filename = dialog.getFileName();
		try {
			if (result != null) {

				File f = new File(filename);
				if ((f.exists()  && !f.canWrite())) {
					Status status = new Status(IStatus.ERROR, "ASPVIZ", 0,
							"The file " + filename + " is not writable", null);
					ErrorDialog.openError(mainwindow.getShell(),
							"Cannot open file", "error", status);
					return;
				}
				AnswerSet frame;
				if (null != (frame = mainwindow.getCurrentFrame())) {
					SVGRenderer renderer = new SVGRenderer();
					FileOutputStream fo =  new FileOutputStream(f);
					renderer.render(frame,fo,mainwindow.getVizModel().getSearchPaths());
					fo.flush();
					fo.close();
				}
			}
		} catch (Exception e) {
			Status status = new Status(IStatus.ERROR, "ASPVIZ", 0,
					"Error writing to " + filename + "\n " + e, null);
			ErrorDialog.openError(mainwindow.getShell(), "Cannot open file",
					"error", status);
			return;
		}
	}
}
