package edu.bath.aspviz;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.apache.log4j.Logger;

import edu.bath.asplib.model.Literal;
import edu.bath.asplib.model.ProblemRun;
import edu.bath.asplib.model.SimpleProgramPrinter;
import edu.bath.asplib.model.Stereotype;
import edu.bath.asplib.model.as.AnswerSet;
import edu.bath.asplib.model.lang.Program;
import edu.bath.asplib.parser.SmodelsLexer;
import edu.bath.asplib.parser.SmodelsParser;
import edu.bath.asplib.tool.CombinedSolverConfiguration;
import edu.bath.asplib.tool.ExternalProcessSolver;
import edu.bath.asplib.tool.Solver;
import edu.bath.asplib.tool.Solver.ProgramSource;
import edu.bath.aspviz.model.Rendering;
import edu.bath.aspviz.model.VizModel;
import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

public class ASPViz {
	static Logger log = Logger.getLogger(ASPViz.class);
	static StringBuffer sbuf = new StringBuffer();
	static LongOpt[] longopts = new LongOpt[] {
			new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h'),
			new LongOpt("format", LongOpt.OPTIONAL_ARGUMENT, null, 'f'),
			new LongOpt("size", LongOpt.OPTIONAL_ARGUMENT, null, 's'),
			new LongOpt("viz", LongOpt.REQUIRED_ARGUMENT, null, 'v'),
			new LongOpt("out", LongOpt.REQUIRED_ARGUMENT, null, 'o'),
			new LongOpt("num", LongOpt.REQUIRED_ARGUMENT, null, 'n'),
			new LongOpt("combined", LongOpt.NO_ARGUMENT, null, 'c'),
			new LongOpt("allcombined", LongOpt.NO_ARGUMENT, null, 'C'),
			new LongOpt("justrender", LongOpt.NO_ARGUMENT, null, 'j'),
			new LongOpt("paths", LongOpt.REQUIRED_ARGUMENT, null, 'P') };

	static final String LIBRARY_DATA = "aspviz2d.lib";

	enum CombinedType {
		NONE, RENDER, BEFORE
	};

	public static String getLibDir() {
		String libDir = System.getProperty("aspviz.lib");
		if (libDir != null) {
			return libDir;
		}

		libDir = System.getProperty("aspviz.dir");
		if (libDir != null) {
			return libDir + File.separator + "lib" + File.separator;
		}
		File f = new File("/usr/share/aspviz/lib");
		if (f.isDirectory())
			return f.getAbsolutePath();
		return "lib/";

	}

	public static void runVisualisation(String[] programs,
			String[] vizprograms, String format, File outputDir,
			List<File> searchPathFiles, int numAnswerSets,
			boolean combineRender, boolean combineBefore) throws Exception {
		log.debug("Running visualization with programs:"
				+ Arrays.toString(programs) + " vizprograms:"
				+ Arrays.toString(vizprograms) + " format:" + format
				+ " output:" + outputDir + " searchPaths:"
				+ searchPathFiles.toString());

		CombinedSolverConfiguration config = new CombinedSolverConfiguration();
		config.setSolverCommand("clingo");
		String solverargs = System.getProperty("solver.args", "");
		config.setSolverArgs(solverargs);
		config.setUseErrorNotOut(true);
		Solver solverobj = new ExternalProcessSolver(config);
		config.setNumAnswerSets(numAnswerSets);

		List<ProgramSource> sources = new ArrayList<ProgramSource>();
		for (String fn : programs) {
			sources.add(new Solver.InputStreamProgramSource(
					new FileInputStream(fn)));
		}

		ProblemRun pr = solverobj.solve(sources
				.toArray(new Solver.ProgramSource[0]));

		if (pr.getAnswerSets().size() == 0) {
			log.debug("No answer sets produced");
			return;
		} else {
			log.debug("loaded " + pr.getAnswerSets().size()
					+ " answer sets, rendering...");
		}

		String libdir = getLibDir();
		String libProgFileName = libdir + LIBRARY_DATA;

		int aset = 0;
		VizModel vm = new VizModel();

		List<AnswerSet> vizAnswerSets;

		if (combineBefore) {
			log.debug("combining conclusions from " + pr.getAnswerSets().size()
					+ " answer sets");
			AnswerSet combinedas = new AnswerSet();
			for (AnswerSet as : pr.getAnswerSets()) {
				for (Literal l : as.getLiterals()) {
					combinedas.addLiteral(l);
				}
			}
			log.debug("combining into answer set with "
					+ combinedas.getLiterals().size() + " literals");
			log.debug("AS " + combinedas.asProgram());

			vizAnswerSets = Collections.singletonList(combinedas);
		} else {
			vizAnswerSets = pr.getAnswerSets();
		}

		for (AnswerSet as : vizAnswerSets) {
			int nframes = 0;
			log.debug("vizualizing program: ");
			Rendering rendering = new Rendering();
			rendering.setBaseAnswerSet(as);

			Program asprog = as.asProgram();
			log.debug("writing answer set prog: " + asprog);
			List<Solver.ProgramSource> vizSources = new ArrayList<Solver.ProgramSource>();

			vizSources.add(new Solver.StringProgramSource("answer_set(" + aset
					+ ").\n"));
			vizSources.add(new Solver.DirectProgramSource(asprog));

			for (String vizprogfn : vizprograms) {
				vizSources.add(new Solver.FileProgramSource(vizprogfn));
			}

			vizSources.add(new Solver.InputStreamProgramSource(
					new FileInputStream(libProgFileName)));
			config.setNumAnswerSets(0);
			ProblemRun result = solverobj.solve(vizSources
					.toArray(new Solver.ProgramSource[0]));

			ArrayList<AnswerSet> sorted = new ArrayList<AnswerSet>();
			sorted.addAll(result.getAnswerSets());
			AnswerSet.sort(new Stereotype("frame", 1), sorted);
			rendering.setAnswerSets(sorted);
			nframes += sorted.size();

			vm.getRenderings().add(rendering);
			log.debug("got " + nframes + " frames for asset" + aset);
			aset++;

		}
		int rendering = 0;
		SVGRenderer renderer = new SVGRenderer();

		if (combineRender) {
			List<AnswerSet> answerSets = new ArrayList<AnswerSet>();
			for (Rendering r : vm.getRenderings()) {
				for (AnswerSet as : r.getAnswerSets()) {
					answerSets.add(as);
				}
			}

			String framefile = outputDir.getCanonicalPath() + File.separator
					+ String.format("combined.%s", format);

			log.debug("writing file " + framefile);
			FileOutputStream fos = new FileOutputStream(framefile);
			log.debug("Rendering " + answerSets.size() + "answer sets ");
			log.debug("to file " + framefile);
			renderer.render(answerSets, fos, searchPathFiles);
			fos.close();
		} else {
			for (Rendering r : vm.getRenderings()) {
				int answerset = 0;
				for (AnswerSet as : r.getAnswerSets()) {

					String framefile = outputDir.getCanonicalPath()
							+ File.separator
							+ String.format("frame-%04d-%04d.%s", rendering,
									answerset, format);

					log.debug("writing file " + framefile);
					FileOutputStream fos = new FileOutputStream(framefile);
					log.debug("Rendering answer set " + as);
					log.debug("to file " + framefile);
					renderer.render(as, fos, searchPathFiles);
					fos.close();
					answerset++;
				}
				rendering++;
			}
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Getopt go = new Getopt("aspviz", args, "Ccf:o:s:hv:n:jP:", longopts);
		String format = "svg";
		String out = "outfile";
		String vizprogram = "visualisation.aspviz";
		int opt = 0;
		boolean combineRender = false;
		boolean combineBefore = false;
		String programs[];
		boolean justRender = false;
		int numAnswerSets = 0;
		List<File> paths = new LinkedList<File>();
		File outputDir = new File(".");

		while ((opt = go.getopt()) != -1) {
			switch (opt) {
			case 0:
			case 'h':
				usage();
				System.exit(1);
			case 1:
			case 'f':
				format = go.getOptarg();
				break;
			case 2:
			case 's':
				break;
			case 3:
			case 'v':
				vizprogram = go.getOptarg();
				break;
			case 4:
			case 'o':
				out = go.getOptarg();
				outputDir = new File(out);
				if (!outputDir.isDirectory())
					error("output directory " + out + " is not a directory");
				break;
			case 'n':
				numAnswerSets = Integer.parseInt(go.getOptarg());
				break;
			case 'c':
				combineRender = true;
				break;
			case 'C':
				combineBefore = true;
				break;
			case 'j':
				justRender = true;
				break;
			case 'P':
				String[] searchPaths = go.getOptarg().split(":");
				for (String searchPath : searchPaths) {
					File fp = new File(searchPath);
					if (!fp.exists() || !fp.isDirectory()) {
						error("path " + searchPath + " is not a directory");
					}
					paths.add(fp);
				}
				break;
			}
		}

		String strformat = format.toString();
		if (strformat.equals("png")) {
			error("PNG isn't supproted at the  moment, sorry");
		} else if (strformat.equals("svg")) {

		} else if (strformat.equals("pdf")) {
			error("PDF isn't supproted at the  moment, sorry");

		} else {
			error("unknown format", format.toString());
		}

		if (justRender) {
			int numArgs = args.length - go.getOptind();
			String infile = "-";
			if (numArgs == 1) {
				infile = Arrays.copyOfRange(args, go.getOptind(), args.length)[0];
			} else if (numArgs > 1) {
				error("for just visualisation specify either zero (stdin) or one file");
			}
			InputStream instream;
			if (infile.equals("-")) {
				instream = System.in;
			} else {
				try {
					instream = new FileInputStream(infile);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

			justRender(strformat, out.toString(), instream, format, outputDir,
					paths);

		} else {
			int numArgs = args.length - go.getOptind();
			if (numArgs <= 0) {
				error("no programs specified");
			}

			programs = Arrays.copyOfRange(args, go.getOptind(), args.length);
			String vizprograms[] = new String[] { vizprogram.toString() };

			List<String> searchPaths = new ArrayList<String>();
			searchPaths.addAll(Arrays.asList(vizprograms));
			searchPaths.addAll(Arrays.asList(programs));

			for (String path : searchPaths) {
				File f = new File(path).getAbsoluteFile();
				paths.add(f.getParentFile());

			}

			try {
				runVisualisation(programs, vizprograms, strformat, outputDir,
						paths, numAnswerSets, combineRender, combineBefore);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static void justRender(String strformat, String string,
			InputStream instream, String format, File outputDir,
			List<File> searchPaths) {
		List<AnswerSet> vizAnswerSets;
		SmodelsParser parser;
		SmodelsLexer lexer;
		try {
			lexer = new SmodelsLexer(new ANTLRInputStream(instream));
			parser = new SmodelsParser(new CommonTokenStream(lexer));
			parser.top();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		ProblemRun pr = parser.getProblemRun();
		log.debug("Parsed " + pr.getAnswerSets().size()
				+ " answer sets from input");

		int framenum = 0;
		for (AnswerSet as : pr.getAnswerSets()) {
			String framefile;
			try {
				framefile = outputDir.getCanonicalPath() + File.separator
						+ String.format("frame-%04d.%s", framenum, format);

				SVGRenderer svgr = new SVGRenderer();
				log.debug("rendering answer set " + framenum + "  to "
						+ framefile);

				svgr.render(as, new FileOutputStream(new File(framefile)),
						searchPaths);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			framenum++;

		}
	}

	public static void usage() {
		System.err
				.println("aspviz -Cc -n numAnswerSets -v vizprog -f [format] -o [outfile] input files...");
		System.err.println("\t --format (-f) output format 'svg' or 'png'");
		System.err.println("\t --output (-o) output file name pattern");
		System.err.println("\t --size (-s) for raster output, canvas size WxH");
		System.err
				.println("\t --justrender (-j) ONLY render, do not parse or solve input is the solver output file (or - for stdin) ");
		System.err
				.println("\t --paths (-P) a  colon seperated list of search paths for additional files (e.g. images) in the visualisation outputs");
		System.err
				.println("\t --combined (-c)  combine answer from problem before visualisation");
		System.err
				.println("\t --allcombined (-c)  combine visualisation answer sets");

	}

	public static void error(String... args) {
		for (String arg : args) {
			System.err.print(arg + " ");
		}
		System.err.println();
		usage();
		System.exit(1);
	}
}
