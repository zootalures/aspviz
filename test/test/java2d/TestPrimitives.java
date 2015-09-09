package test.java2d;

import java.io.File;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import edu.bath.asplib.model.as.AnswerSet;
import edu.bath.aspviz.ASPViz;

public class TestPrimitives {
	AnswerSet vizSet;

	@Before
	public void setup() {
		vizSet = new AnswerSet();
	}

	@Test
	public void testPrimitiveSheet() throws Exception {
		String primitveSheetFile = "testdata/testsheet.lp";
		ASPViz.runVisualisation(new String[] {},
				new String[] { primitveSheetFile }, "svg", new File("."),
				new LinkedList<File>(), 1, false, false);
	}
}
