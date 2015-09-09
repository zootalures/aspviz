package edu.bath.aspviz;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.apache.log4j.Logger;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import edu.bath.asplib.model.ProblemRun;
import edu.bath.asplib.model.SimpleProgramPrinter;
import edu.bath.asplib.model.Stereotype;
import edu.bath.asplib.model.as.AnswerSet;
import edu.bath.asplib.parser.SmodelsLexer;
import edu.bath.asplib.parser.SmodelsParser;
import edu.bath.asplib.tool.CombinedSolverConfiguration;
import edu.bath.asplib.tool.ExternalProcessSolver;
import edu.bath.asplib.tool.Solver;
import edu.bath.asplib.tool.SolverConfiguration;
import edu.bath.aspviz.actions.AddFilesAction;
import edu.bath.aspviz.actions.ClearModelAction;
import edu.bath.aspviz.actions.ExitAction;
import edu.bath.aspviz.actions.ExportFrameAction;
import edu.bath.aspviz.actions.RenderAnswerSetsAction;
import edu.bath.aspviz.lang.java2d.TwoDRenderer;
import edu.bath.aspviz.lang.java2d.TwoDVizContext;
import edu.bath.aspviz.model.ProblemModel;
import edu.bath.aspviz.model.Rendering;
import edu.bath.aspviz.model.VizModel;

public class MainWindow extends ApplicationWindow {
	public static final String LIBRARY_DATA = "/lib/aspviz2d.lib";

	Logger log = Logger.getLogger(getClass());

	AddFilesAction addFilesAction;
	ExitAction exitAction;
	ClearModelAction clearModelAction;
	RenderAnswerSetsAction renderAnswerSetsAction;
	ExportFrameAction exportFrameAction;
	VizModel vizModel;
	ProblemModel model = new ProblemModel();
	SolverConfiguration solverConfig = new CombinedSolverConfiguration();

	Canvas drawCanvas;
	Spinner renderScroll;
	Spinner frameScroll;
	Label asLabel;
	List fileList;

	public VizModel getVizModel() {
		return vizModel;
	}

	public void setVizModel(VizModel vizModel) {
		this.vizModel = vizModel;
		renderScroll.setEnabled(false);
		if (vizModel != null) {
			if (vizModel.getRenderings().size() > 0) {
				renderScroll.setMinimum(0);
				renderScroll.setSelection(0);
				renderScroll.setMaximum(vizModel.getRenderings().size() - 1);
				renderScroll.setEnabled(true);
			}
		} else {

		}

		drawCanvas.redraw();
	}

	public void setupActions() {

	}

	public MainWindow() {
		super((Shell) null);
		addFilesAction = new AddFilesAction(this);
		exitAction = new ExitAction(this);
		clearModelAction = new ClearModelAction(this);
		clearModelAction.setEnabled(false);
		exportFrameAction = new ExportFrameAction(this);
		exportFrameAction.setEnabled(false);
		renderAnswerSetsAction = new RenderAnswerSetsAction(this);
		renderAnswerSetsAction.setEnabled(false);
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();

	}

	public ProblemModel getModel() {
		return model;
	}

	public void setModel(ProblemModel model) {
		this.model = model;
		setVizModel(null);
		if (model == null) {
			renderAnswerSetsAction.setEnabled(false);
			clearModelAction.setEnabled(false);
			exportFrameAction.setEnabled(false);
		} else {
			renderAnswerSetsAction.setEnabled(true);
			clearModelAction.setEnabled(true);
			exportFrameAction.setEnabled(true);
			getStatusLineManager().setMessage(
					"Loaded " + model.getInputFilenames().size() + " files");

		}
	}

	public AnswerSet getCurrentFrame() {
		int currentRendering = renderScroll.getSelection();
		if (currentRendering >= 0
				&& currentRendering < vizModel.getRenderings().size()) {
			Rendering rend = vizModel.getRenderings().get(currentRendering);

			int currentFrame = frameScroll.getSelection();
			if (currentFrame >= 0 && currentFrame < rend.getAnswerSets().size()) {
				return rend.getAnswerSets().get(currentFrame);
			}
		}
		return null;
	}

	@Override
	protected Control createContents(Composite parent) {
		parent.getShell().setSize(640, 480);
		parent.getShell().setText("ASP Viz window");

		parent.setLayout(new org.eclipse.swt.layout.RowLayout());

		TabFolder tf = new TabFolder(parent, 0);

		TabItem ti = new TabItem(tf, 0);
		Composite controls = new Composite(tf, SWT.FILL);
		ti.setControl(controls);
		tf.setSelection(0);
		ti.setText("files");
		fileList = new List(controls, SWT.Expand);

		drawCanvas = new Canvas(parent, 0);
		drawCanvas.setLayoutData(new RowData(1024, 768));
		// drawCanvas.setBounds(0,0,640,480);
		final Java2DRenderer j2dr = new Java2DRenderer();

		drawCanvas.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent event) {
				if (vizModel != null && vizModel.getRenderings().size() > 0) {
					final AnswerSet currentFrame = getCurrentFrame();
					if (currentFrame != null) {
						TwoDVizContext tdc = new TwoDVizContext();
						tdc.setFileSearchPaths(vizModel.getSearchPaths());
						j2dr.render(tdc, event.gc, currentFrame);
					}
				} else {
					Color c = new Color(event.gc.getDevice(),
							new RGB(0, 255, 0));
					event.gc.setBackground(c);
					event.gc.setForeground(c);
					event.gc.fillRectangle(0, 0, drawCanvas.getBounds().width,
							drawCanvas.getBounds().height);
				}
			}
		});
		Composite frames = new Composite(controls, 0);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		frames.setLayout(gl);

		Label renderLabel = new Label(frames, SWT.RIGHT);
		renderLabel.setText("rendering");
		renderScroll = new Spinner(frames, 0);
		renderScroll.setEnabled(false);
		renderScroll.setSelection(0);
		renderScroll.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				frameScroll.setSelection(0);
				drawCanvas.redraw();
			}
		});
		Label frameLabel = new Label(frames, SWT.RIGHT);
		frameLabel.setText("frame");
		frameScroll = new Spinner(frames, 0);
		frameScroll.setEnabled(false);
		frameScroll.setSelection(0);
		frameScroll.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				drawCanvas.redraw();
			}
		});
		controls.pack();
		parent.pack();
		return parent;
	}

	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBar = new ToolBarManager(style);
		toolBar.add(addFilesAction);
		toolBar.add(renderAnswerSetsAction);
		return toolBar;
	}

	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuBar = new MenuManager();
		MenuManager fileMenu = new MenuManager("&File");

		Separator separator = new Separator();
		fileMenu.add(clearModelAction);
		fileMenu.add(addFilesAction);
		fileMenu.add(renderAnswerSetsAction);

		fileMenu.add(exportFrameAction);
		fileMenu.add(separator);
		fileMenu.add(exitAction);

		menuBar.add(fileMenu);
		return menuBar;
	}

	/**
	 * The big "everything gets done here" loop.
	 * 
	 * TODO: all this needs refactoring properly, it's a hack atm
	 */
	public void renderCurrentModel() {

		try {
			if (model == null || model.getInputFilenames().size() == 0)
				return;
			Solver solver = new ExternalProcessSolver(solverConfig);
			java.util.List<Solver.ProgramSource> sources = new ArrayList<Solver.ProgramSource>();

			for (String input : model.getInputFilenames()) {
				sources.add(new Solver.FileProgramSource(input));
			}
			ProblemRun pr = solver.solve(sources
					.toArray(new Solver.ProgramSource[0]));

			if (pr.getAnswerSets().size() == 0) {
				getStatusLineManager().setErrorMessage(
						"no answer sets produced");
				return;
			} else {
				getStatusLineManager().setMessage(
						"loaded " + pr.getAnswerSets().size()
								+ " answer sets, rendering...");
			}

			String libloc = System.getProperty("aspviz.libfile", System
					.getProperty("aspviz.dir", new File("").getAbsolutePath()
							+ LIBRARY_DATA));
			log.debug("reading library from " + libloc);


			java.util.List<String> searchPaths = new ArrayList<String>();
			searchPaths.addAll(model.getVizFilenames());
			searchPaths.addAll(model.getInputFilenames());
			java.util.List<File> searchPathFiles = new ArrayList<File>();

			for (String path : searchPaths) {
				File f = new File(path);
				searchPathFiles.add(f.getParentFile());
			}

			int aset = 1;
			VizModel vm = new VizModel();
			vm.setSearchPaths(searchPathFiles);
			int nframes = 0;
			for (AnswerSet as : pr.getAnswerSets()) {
				Rendering rendering = new Rendering();
				rendering.setBaseAnswerSet(as);
				StringBuffer rendcmd = new StringBuffer();
				java.util.List<Solver.ProgramSource> visSources=  new ArrayList<Solver.ProgramSource>();
				
				for (String vizprog : model.getVizFilenames()) {
					visSources.add(new Solver.FileProgramSource(vizprog));
				}
				visSources.add(new Solver.DirectProgramSource(as.asProgram()));
				visSources.add(new Solver.FileProgramSource(libloc));
				ProblemRun visresult = solver.solve(visSources.toArray(new Solver.ProgramSource[0]));
				
				ArrayList<AnswerSet> sorted = new ArrayList<AnswerSet>();
				sorted.addAll(visresult.getAnswerSets());
				AnswerSet.sort(new Stereotype("frame", 1), sorted);
				rendering.setAnswerSets(sorted);
				nframes += sorted.size();
				aset++;

				vm.getRenderings().add(rendering);
			}
			getStatusLineManager().setMessage(
					"rendered " + vm.getRenderings().size()
							+ " problems  with " + nframes + " frames");

			setVizModel(vm);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
