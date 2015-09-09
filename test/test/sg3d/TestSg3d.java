package test.sg3d;

import static org.junit.Assert.assertTrue;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.junit.Before;
import org.junit.Test;

import edu.bath.asplib.model.ProblemRun;
import edu.bath.asplib.model.as.AnswerSet;
import edu.bath.asplib.parser.SmodelsLexer;
import edu.bath.asplib.parser.SmodelsParser;
import edu.bath.aspviz.sg3d.GLNode;
import edu.bath.aspviz.sg3d.Sg3dSceneGraphBuilder;

public class TestSg3d {

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testBuildSGFromas() throws Exception {
		SmodelsParser parser;
		SmodelsLexer lexer;
		lexer = new SmodelsLexer(new ANTLRFileStream(
				"testdata/testans1.smodels"));
		parser = new SmodelsParser(new CommonTokenStream(lexer));
		parser.top();
		ProblemRun pr = parser.getProblemRun();
		assertTrue(pr.getAnswerSets().size() == 1);
		AnswerSet as = pr.getAnswerSets().get(0);
		System.err.println("rendering  " + as);
		Sg3dSceneGraphBuilder buld = new Sg3dSceneGraphBuilder();
		GLNode topNode = buld.buildSceneGraph(as);
	}
}
