package edu.bath.aspviz;

import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.List;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import edu.bath.asplib.model.as.AnswerSet;
import edu.bath.aspviz.lang.java2d.TwoDRenderer;
import edu.bath.aspviz.lang.java2d.TwoDVizContext;

public class SVGRenderer {

	public void render(AnswerSet answerset, OutputStream target,
			List<File> searchPaths) {
		render(Collections.singletonList(answerset), target, searchPaths);
	}

	public void render(List<AnswerSet> answersets, OutputStream target,
			List<File> searchPaths) {
		try {
			// Get a DOMImplementation.
			DOMImplementation domImpl = GenericDOMImplementation
					.getDOMImplementation();

			// Create an instance of org.w3c.dom.Document.
			String svgNS = "http://www.w3.org/2000/svg";
			Document document = domImpl.createDocument(svgNS, "svg", null);

			// Create an instance of the SVG Generator.
			SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
			AffineTransform dflTransform = svgGenerator.getTransform();
			for (AnswerSet as : answersets) {
				svgGenerator.setTransform(dflTransform);
				TwoDVizContext ctx = new TwoDVizContext();
				ctx.setFileSearchPaths(searchPaths);
				TwoDRenderer rend = new TwoDRenderer();
				rend.render(ctx, svgGenerator, as);
		
			}
			// Finally, stream out SVG to the standard output using
			// UTF-8 encoding.
			boolean useCSS = true; // we want to use CSS style attributes
			svgGenerator.stream(new OutputStreamWriter(target), useCSS);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
