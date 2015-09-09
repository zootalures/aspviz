package edu.bath.aspviz.sg3d;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import edu.bath.asplib.model.BasePredicate;
import edu.bath.asplib.model.FTerm;
import edu.bath.asplib.model.Literal;
import edu.bath.asplib.model.NumberTerm;
import edu.bath.asplib.model.Stereotype;
import edu.bath.asplib.model.StringTerm;
import edu.bath.asplib.model.Term;
import edu.bath.asplib.model.as.AnswerSet;
import edu.bath.aspviz.sg.ContextAction;

public class Sg3dSceneGraphBuilder {

	Logger log = Logger.getLogger(Sg3dSceneGraphBuilder.class);

	public GLNode buildSceneGraph(AnswerSet as) {
		GLNode rootNode = new GLNode();
		Map<String, GLNode> nodeMap = new HashMap<String, GLNode>();

		/**
		 * add nodes themselves
		 */
		for (Literal lit : as.searchLiterals("node", 1)) {
			GLNode node = new GLNode();
			node.setId(lit.getParam(0).toString());
			nodeMap.put(node.getId(), node);
		}

		/**
		 * Add parent-child links
		 */
		for (Literal lit : as.searchLiterals("node_parent", 2)) {
			GLNode child = nodeMap.get(lit.getParam(0).toString());
			if (child == null) {
				log.warn("literal" + lit.toString()
						+ " refers to non-existant child ");
				continue;
			}
			GLNode parent = nodeMap.get(lit.getParam(1).toString());
			if (parent == null) {
				log.warn("literal" + lit.toString()
						+ " refers to non-existant parent ");
				continue;
			}
			parent.addChild(child);
			log.debug("added link: " + child.getId() + " below "
					+ parent.getId());
		}

		/*
		 * Add transforms
		 */

		for (Literal lit : as.searchLiterals("node_transform", 2)) {
			GLNode node = nodeMap.get(lit.getParam(0).toString());
			if (node == null) {
				log.warn("literal" + lit.toString()
						+ " refers to non-existant node ");
				continue;
			}
			if (node.getTransforms().size() > 0) {
				log
						.warn("node "
								+ node.getId()
								+ " already has transforms, expect non-deterministic results if trans aren't commutative");
			}
			List<Term> transforms = new ArrayList<Term>();
			Term transParam = lit.getParam(1);
			if (transParam instanceof FTerm
					&& ((FTerm) transParam).getName().equals("l")) {
				transforms.addAll(((FTerm) transParam).getParams());
			} else {
				transforms.add(transParam);
			}
			for (Term transTerm : transforms) {
				node.addTransform(termToTransform(transTerm));
			}
		}

		/**
		 * add node shapes
		 * 
		 */
		for (Literal lit : as.searchLiterals("node_shape", 2)) {
			GLNode node = nodeMap.get(lit.getParam(0).toString());
			if (node == null) {
				log.warn("literal" + lit.toString()
						+ " refers to non-existant node ");
				continue;
			}
			Term shapeTerm = lit.getParam(1);

			node.addShape(termToShape(shapeTerm));
		}

		/**
		 * Add attributes
		 */
		for (Literal lit : as.searchLiterals("node_attrib", 2)) {
			GLNode node = nodeMap.get(lit.getParam(0).toString());
			if (node == null) {
				log.warn("literal" + lit.toString()
						+ " refers to non-existant node ");
				continue;
			}

			Term attr_term = lit.getParam(1);
			node.addAttribute(termToAttrib(attr_term));

		}

		Set<Literal> root = as.searchLiterals("root_node", 3);
		if (root.size() > 0) {
			if (root.size() > 1) {
				log
						.warn("more than one root node specified -expect non-determinism");
			}
			Literal rootLit = root.iterator().next();
			String rootName = rootLit.getParam(0).toString();
			rootNode = nodeMap.get(rootName);
			if (null != rootNode) {
				throw new RuntimeException("Root node " + rootName
						+ " specified but not found");
			} else {
				return rootNode;
			}
		} else {
			if (null != (rootNode = nodeMap.get("origin"))) {
				return rootNode;
			} else {
				throw new RuntimeException(
						"No root node or node called 'origin' specified ");

			}
		}

	}

	double termToDouble(Term t) {
		if (t instanceof NumberTerm) {
			return (double) ((NumberTerm) t).floatValue();
		} else if (t instanceof StringTerm) {
			try {
				return Double.parseDouble(((StringTerm) t).value());
			} catch (NumberFormatException e) {

			}
		}
		throw new RuntimeException("Unable to coerce term " + t.toString()
				+ " into a double ");
	}

	/***
	 * Translates a translation term into a translation action
	 * 
	 * @param node
	 * @param pb
	 */
	ContextAction<GL> termToTransform(Term param) {
		if (param instanceof FTerm) {
			FTerm fbp = (FTerm) param;
			String tname = fbp.getName();
			if (fbp.stMatches(new Stereotype("rotate", 4))) {
				return new Rotate(termToDouble(fbp.getParam(0)),
						termToDouble(fbp.getParam(1)), termToDouble(fbp
								.getParam(2)), termToDouble(fbp.getParam(3)));
			} else if (fbp.stMatches(new Stereotype("translate", 3))) {
				return new Translate(termToDouble(fbp.getParam(0)),
						termToDouble(fbp.getParam(1)), termToDouble(fbp
								.getParam(2)));

			} else if (fbp.stMatches(new Stereotype("scale", 3))) {
				return new Scale(termToDouble(fbp.getParam(0)),
						termToDouble(fbp.getParam(1)), termToDouble(fbp
								.getParam(2)));
			}

		}
		throw new RuntimeException("Unrecognised transform " + param.toString());

	}

	/**
	 * Translates a term into a shape
	 * 
	 * @param shape
	 * @return
	 */
	ContextAction<GL> termToShape(Term shape) {
		String shapeString = shape.toString();

		if (shapeString.equals("cube")) {
			return new GLCube();
		} else if (shapeString.equals("sphere")) {
			return new GLSphere();
		}
		throw new RuntimeException("Unrecognised shape " + shape.toString());

	}

	/**
	 * Translates a term into an attribute
	 * 
	 * @param shape
	 * @return
	 */
	ContextAction<GL> termToAttrib(Term attr) {

		if (attr instanceof FTerm) {
			FTerm fattr = (FTerm) attr;
			if (fattr.stMatches(new Stereotype("color", 1))
					&& fattr.getParam(0) instanceof FTerm
					&& ((FTerm) fattr.getParam(0)).stMatches(new Stereotype(
							"rgb", 3))) {
				FTerm rgb = (FTerm) fattr.getParam(0);
				return new GLColor(termToDouble(rgb.getParam(0))/255.0,
						termToDouble(rgb.getParam(1))/255.0, termToDouble(rgb
								.getParam(2))/255.0);

			}
		}
		throw new RuntimeException("Unrecognised attr " + attr.toString());

	}
}
