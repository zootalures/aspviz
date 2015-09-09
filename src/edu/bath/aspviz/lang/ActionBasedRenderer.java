package edu.bath.aspviz.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.bath.asplib.model.Literal;
import edu.bath.asplib.model.Stereotype;
import edu.bath.asplib.model.as.AnswerSet;
import edu.bath.aspviz.DrawCall;
import edu.bath.aspviz.DrawContext;
import edu.bath.aspviz.Renderer;

public class ActionBasedRenderer<DCTX extends DrawContext,Surface extends Object> implements
		Renderer<DCTX,Surface> {

	public void addAction(DrawAction<DCTX,Surface> action) {
		actions.add(action);
	}

	// protected SortedSet<Action<DCTX>> actions = new TreeSet<Action<DCTX>>(
	// new Comparator<Action<DCTX>>() {
	// public int compare(Action<DCTX> o1, Action<DCTX> o2) {
	// return ((Integer) o1.getPrecedence()).compareTo(o2
	// .getPrecedence());
	// };
	// });

	protected List<DrawAction<DCTX,Surface>> actions = new ArrayList<DrawAction<DCTX,Surface>>();

	public void render(DCTX c,Surface sfc, AnswerSet answerSet) {
		Map<Stereotype, List<Literal>> litsByStereotype = answerSet
				.getLiteralsByStereotype();

		for (DrawAction<DCTX,Surface> a : actions) {
			System.err.println("Action " + a.getStereotype());
			List<Literal> lits = litsByStereotype.get(a.getStereotype());
			if (null != lits) {
				for (Literal l : lits) {
					System.err.println("acting on  " + l.toString());
					a.act(c, sfc, l);
				}
			}
		}
		
		while(c.getPostActions().size()>0){
			DrawCall<DCTX,Surface> dc = (DrawCall<DCTX,Surface> )c.getPostActions().remove(0);
			dc.draw(c,sfc);
		}
	}
}
