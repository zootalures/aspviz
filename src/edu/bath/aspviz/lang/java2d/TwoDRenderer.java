package edu.bath.aspviz.lang.java2d;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;

import org.apache.log4j.Logger;

import edu.bath.asplib.model.FTerm;
import edu.bath.asplib.model.Literal;
import edu.bath.asplib.model.NumberTerm;
import edu.bath.asplib.model.Stereotype;
import edu.bath.asplib.model.StringTerm;
import edu.bath.asplib.model.Term;
import edu.bath.asplib.model.as.AnswerSet;
import edu.bath.aspviz.Brush;
import edu.bath.aspviz.DrawCall;
import edu.bath.aspviz.FontInfo;
import edu.bath.aspviz.lang.DrawAction;
import edu.bath.aspviz.lang.ActionBasedRenderer;

public class TwoDRenderer extends
		ActionBasedRenderer<TwoDVizContext, Graphics2D> {

	Logger log = Logger.getLogger(getClass());

	Color termToColor(TwoDVizContext ctx, Term term) {
		FTerm cterm = (FTerm) term;
		Color cdata;
		if (cterm.getName().equals("rgb") && cterm.getArity() == 3) {
			Color c = new Color(((NumberTerm) ((FTerm) term).getParam(0))
					.intValue(), ((NumberTerm) ((FTerm) term).getParam(1))
					.intValue(), ((NumberTerm) ((FTerm) term).getParam(2))
					.intValue());
			return c;
		} else if (null != (cdata = ctx.getColor(cterm.toString()))) {
			return cdata;
		} else {
			throw new RuntimeException("Color " + cterm.toString()
					+ " is not recognised");
		}
	}

	Point2D.Double termToPoint(Term term) {
		FTerm cterm = (FTerm) term;
		Point2D.Double p = new Point2D.Double(((NumberTerm) cterm.getParam(0))
				.intValue(), ((NumberTerm) cterm.getParam(1)).intValue());
		return p;
	}

	DrawAction<TwoDVizContext, Graphics2D> color = new DrawAction<TwoDVizContext, Graphics2D>(
			new Stereotype("color", 2), DrawAction.FIRST) {
		public void act(TwoDVizContext ctx, Graphics2D sfc, Literal literal) {
			Color c = termToColor(ctx, literal.getParam(1));
			ctx.setColor(literal.getParam(0).toString(), c);
		};
	};
	{
		addAction(color);
	}

	DrawAction<TwoDVizContext, Graphics2D> sprite = new DrawAction<TwoDVizContext, Graphics2D>(
			new Stereotype("sprite", 2), DrawAction.FIRST) {
		public void act(TwoDVizContext ctx, Graphics2D sfc, Literal literal) {
			log.debug("creating sprite from literal " + literal.toString());
			
			String id = literal.getParam(0).toString();

			try {
				String filename = ((StringTerm) (literal.getParam(1))).value();
				File gotFile = ctx.findFileByName(filename);

				BufferedImage img = javax.imageio.ImageIO.read(gotFile);

				ctx.setSprite(id, img);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	};
	{
		addAction(sprite);
	}

	DrawAction<TwoDVizContext, Graphics2D> backgroundColor = new DrawAction<TwoDVizContext, Graphics2D>(
			new Stereotype("background_color", 1), DrawAction.SECOND) {
		public void act(TwoDVizContext ctx, Graphics2D sfc, Literal literal) {
			Color c = termToColor(ctx, literal.getParam(0));
			sfc.setBackground(c);
			Rectangle rect = sfc.getClipBounds();
			sfc.fillRect(0, 0, 2000, 2000);

		};
	};
	{
		addAction(backgroundColor);
	}

	DrawAction<TwoDVizContext, Graphics2D> scaleCanvas = new DrawAction<TwoDVizContext, Graphics2D>(
			new Stereotype("scale_canvas", 2), DrawAction.FIRST) {
		public void act(TwoDVizContext ctx, Graphics2D sfc, Literal literal) {
			float scaleX = ((NumberTerm) literal.getParam(0)).floatValue();
			float scaleY = ((NumberTerm) literal.getParam(1)).floatValue();

			sfc.scale(scaleX, scaleY);
		};
	};
	{
		addAction(scaleCanvas);
	}
	DrawAction<TwoDVizContext, Graphics2D> translateCanvas = new DrawAction<TwoDVizContext, Graphics2D>(
			new Stereotype("translate_canvas", 2), DrawAction.FIRST) {
		public void act(TwoDVizContext ctx, Graphics2D sfc, Literal literal) {
			float transX = ((NumberTerm) literal.getParam(0)).floatValue();
			float transY = ((NumberTerm) literal.getParam(1)).floatValue();

			sfc.translate(transX, transY);
		};
	};
	{
		addAction(translateCanvas);
	}
	DrawAction<TwoDVizContext, Graphics2D> rotateCanvas = new DrawAction<TwoDVizContext, Graphics2D>(
			new Stereotype("rotate_canvas", 1), DrawAction.FIRST) {
		public void act(TwoDVizContext ctx, Graphics2D sfc, Literal literal) {
			float rot = ((NumberTerm) literal.getParam(0)).floatValue();

			sfc.rotate(rot);
		};
	};
	{
		addAction(rotateCanvas);
	}

	DrawAction<TwoDVizContext, Graphics2D> createBrush = new DrawAction<TwoDVizContext, Graphics2D>(
			new Stereotype("brush", 1), DrawAction.FIRST) {
		public void act(TwoDVizContext ctx, Graphics2D sfc, Literal literal) {
			Brush b = new Brush();
			;
			ctx.setBrush(literal.getParam(0).toString(), b);
		};
	};
	{
		addAction(createBrush);
	}

	DrawAction<TwoDVizContext, Graphics2D> createFont = new DrawAction<TwoDVizContext, Graphics2D>(
			new Stereotype("font", 1), DrawAction.FIRST) {
		public void act(TwoDVizContext ctx, Graphics2D sfc, Literal literal) {
			FontInfo finfo = new FontInfo();

			Font b = new Font("sans-serif", 0, 9);
			finfo.setFont(b);
			ctx.setFont(literal.getParam(0).toString(), finfo);
		};
	};
	{
		addAction(createFont);
	}

	DrawAction<TwoDVizContext, Graphics2D> fontWeight = new DrawAction<TwoDVizContext, Graphics2D>(
			new Stereotype("font_style", 2), DrawAction.SECOND) {
		public void act(TwoDVizContext ctx, Graphics2D sfc, Literal literal) {
			String fontname = literal.getParam(0).toString();
			FontInfo fi = ctx.getFont(fontname);
			Font b = fi.getFont();
			String style = literal.getParam(1).toString();
			int fstyle = b.getStyle();

			if (style.equals("bold")) {
				fstyle |= Font.BOLD;
			} else if (style.equals("italic")) {
				fstyle |= Font.ITALIC;
			} else if (style.equals("bolditalic")) {
				fstyle |= Font.BOLD | Font.ITALIC;
			}

			Font newfont = new Font(b.getFamily(), fstyle, b.getSize());
			fi.setFont(newfont);
		};
	};
	{
		addAction(fontWeight);
	}

	DrawAction<TwoDVizContext, Graphics2D> fontSize = new DrawAction<TwoDVizContext, Graphics2D>(
			new Stereotype("font_size", 2), DrawAction.SECOND) {
		public void act(TwoDVizContext ctx, Graphics2D sfc, Literal literal) {
			String fontname  = literal.getParam(0).toString();
			FontInfo font = ctx.getFont(fontname);
			Font f = font.getFont();
			int fontsize = 	((NumberTerm) literal.getParam(1)).intValue();
			Font nf = new Font(f.getFamily(), f.getStyle(),fontsize);
			log.debug("setting size of " + fontname  + " to " +fontsize );
		};
	};
	{
		addAction(fontSize);
	}

	DrawAction<TwoDVizContext, Graphics2D> fontColor = new DrawAction<TwoDVizContext, Graphics2D>(
			new Stereotype("font_color", 2), DrawAction.SECOND) {
		public void act(TwoDVizContext ctx, Graphics2D sfc, Literal literal) {
			FontInfo fi = ctx.getFont(literal.getParam(0).toString());
			Color c = ctx.getColor(literal.getParam(1));
			fi.setColor(c);
		};
	};
	{
		addAction(fontColor);
	}

	DrawAction<TwoDVizContext, Graphics2D> fontBGColor = new DrawAction<TwoDVizContext, Graphics2D>(
			new Stereotype("font_bgcolor", 2), DrawAction.SECOND) {
		public void act(TwoDVizContext ctx, Graphics2D sfc, Literal literal) {
			FontInfo fi = ctx.getFont(literal.getParam(0).toString());
			Color c = ctx.getColor(literal.getParam(1));
			fi.setBgColor(c);

		};
	};
	{
		addAction(fontBGColor);
	}

	DrawAction<TwoDVizContext, Graphics2D> brushWidth = new DrawAction<TwoDVizContext, Graphics2D>(
			new Stereotype("brush_width", 2), DrawAction.SECOND) {

		public void act(TwoDVizContext ctx, Graphics2D sfc, Literal literal) {
			Brush b = ctx.getBrush(literal.getParam(0).toString());
			Term w = literal.getParam(1);

			b.setWidth(((NumberTerm) w).intValue());
		};
	};
	{
		addAction(brushWidth);
	}

	DrawAction<TwoDVizContext, Graphics2D> brushColor = new DrawAction<TwoDVizContext, Graphics2D>(
			new Stereotype("brush_color", 2), DrawAction.SECOND) {
		public void act(TwoDVizContext ctx, Graphics2D sfc, Literal literal) {
			Brush b = ctx.getBrush(literal.getParam(0).toString());

			Color c = termToColor(ctx, literal.getParam(1));
			b.setColor(c);
		};
	};
	{
		addAction(brushColor);
	}

	public void setupBrush(Graphics2D gc, Brush b) {

		gc.setStroke(new BasicStroke(b.getWidth()));
		if (b.getColor() != null) {
			gc.setColor(b.getColor());
		}
	}

	DrawAction<TwoDVizContext, Graphics2D> drawRect = new DrawAction<TwoDVizContext, Graphics2D>(
			new Stereotype("draw_rect", 4), DrawAction.THIRD) {
		public void act(TwoDVizContext ctx, Graphics2D sfc, Literal literal) {
			Brush b = ctx.getBrush(literal.getParam(0).toString());
			setupBrush(sfc, b);
			Term p1 = literal.getParam(1);

			sfc.drawRect(((NumberTerm) ((FTerm) p1).getParam(0)).intValue(),
					((NumberTerm) ((FTerm) p1).getParam(1)).intValue(),
					((NumberTerm) literal.getParam(2)).intValue(),
					((NumberTerm) literal.getParam(3)).intValue());
		};
	};

	{
		addAction(drawRect);
	}

	DrawAction<TwoDVizContext, Graphics2D> fill_rect = new DrawAction<TwoDVizContext, Graphics2D>(
			new Stereotype("fill_rect", 5), DrawAction.THIRD) {
		public void act(TwoDVizContext ctx, Graphics2D sfc, Literal literal) {
			Brush b = ctx.getBrush(literal.getParam(0).toString());


			Color fillColor = ctx.getColor(literal.getParam(1));
			Term p1 = literal.getParam(2);
			sfc.setColor(fillColor);
			sfc.fillRect(((NumberTerm) ((FTerm) p1).getParam(0)).intValue(),
					((NumberTerm) ((FTerm) p1).getParam(1)).intValue(),
					((NumberTerm) literal.getParam(3)).intValue(),
					((NumberTerm) literal.getParam(4)).intValue());
			
			setupBrush(sfc, b);
			sfc.drawRect(((NumberTerm) ((FTerm) p1).getParam(0)).intValue(),
					((NumberTerm) ((FTerm) p1).getParam(1)).intValue(),
					((NumberTerm) literal.getParam(3)).intValue(),
					((NumberTerm) literal.getParam(4)).intValue());

		};
	};

	{
		addAction(fill_rect);
	}

	DrawAction<TwoDVizContext, Graphics2D> drawLine = new DrawAction<TwoDVizContext, Graphics2D>(
			new Stereotype("draw_line", 3), DrawAction.THIRD) {
		public void act(TwoDVizContext ctx, Graphics2D sfc, Literal literal) {
			Brush b = ctx.getBrush(literal.getParam(0).toString());
			setupBrush(sfc, b);
			Point2D.Double p1 = termToPoint(literal.getParam(1));
			Point2D.Double p2 = termToPoint(literal.getParam(2));

			sfc.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
		};
	};
	{
		addAction(drawLine);
	}

	DrawAction<TwoDVizContext, Graphics2D> drawSprite = new DrawAction<TwoDVizContext, Graphics2D>(
			new Stereotype("draw_sprite", 4), DrawAction.THIRD) {
		public void act(TwoDVizContext ctx, Graphics2D sfc, Literal literal) {
			BufferedImage sprite = ctx
					.getSprite(literal.getParam(0).toString());
			Point2D.Double pos = termToPoint(literal.getParam(1));
			String hlayout = literal.getParam(2).toString();
			String vlayout = literal.getParam(3).toString();
			int xpos = (int) pos.x;
			int ypos = (int) pos.y;

			int height = sprite.getHeight();
			int width = sprite.getWidth();
			if (hlayout.equals("c")) {
				xpos -= width / 2.0;
			}
			if (vlayout.equals("c")) {
				ypos -= height / 2;
			}
			sfc.drawImage(sprite, xpos, ypos, null);
		}
	};
	{
		addAction(drawSprite);
	}
	DrawAction<TwoDVizContext, Graphics2D> drawText = new DrawAction<TwoDVizContext, Graphics2D>(
			new Stereotype("draw_text", 5), DrawAction.THIRD) {
		public void act(TwoDVizContext ctx, Graphics2D sfc, Literal literal) {
			String fontname = literal.getParam(0).toString();
			FontInfo fi = ctx.getFont(fontname);
			Font fd = fi.getFont();

			if (fi.getColor() != null) {
				sfc.setColor(fi.getColor());
			} else {
				sfc.setColor(Color.black);

			}

			if (fi.getBgColor() != null) {
				sfc.setBackground(fi.getBgColor());

			} else {
				sfc.setBackground(Color.white);
			}

			String hlayout = literal.getParam(1).toString();
			String vlayout = literal.getParam(2).toString();

			Point2D.Double p = termToPoint(literal.getParam(3));
			double xpos =  p.x;
			double ypos =  p.y;

			String text = literal.getParam(4).toString();
			FontMetrics fm = sfc.getFontMetrics(fd);
			int textw = fm.stringWidth(text);
			int texth = fm.getHeight();
			if (hlayout.equals("c")) {
				xpos -= 1.0*(textw / 2.0);
			}
			if (vlayout.equals("c")) {
				ypos += 0.465*(texth / 2.0);
			}
			// log.debug("Rendering text in font  bold:" + fd.isBold());
			sfc.setFont(fd);
			
			sfc.drawString(text, (int)xpos, (int)ypos);

		};
	};

	{
		addAction(drawText);
	}

	DrawAction<TwoDVizContext, Graphics2D> polygon = new DrawAction<TwoDVizContext, Graphics2D>(
			new Stereotype("polygon", 1), DrawAction.SECOND) {
		public void act(TwoDVizContext ctx, Graphics2D sfc, Literal literal) {
			final edu.bath.aspviz.lang.java2d.Polygon poly = new edu.bath.aspviz.lang.java2d.Polygon();
			ctx.setPolygon(literal.getParam(0).toString(), poly);
			ctx.addPostCall(new DrawCall<TwoDVizContext, Graphics2D>() {
				@Override
				public void draw(TwoDVizContext ctx, Graphics2D sfc) {
					Point2D.Double[] path = poly.toPolyPath();
					int xpts[] = new int[path.length];
					int ypts[] = new int[path.length];

					for (int i = 0; i < path.length; i++) {
						xpts[i] = (int) path[i].x;
						ypts[i] = (int) path[i].y;
					}
					if (poly.getLineBrush() != null) {
						setupBrush(sfc, poly.getLineBrush());
					}

					if (poly.getFillColor() != null) {
						sfc.setBackground(poly.getFillColor());
						sfc.fillPolygon(xpts, ypts, xpts.length);
					} else {
						sfc.drawPolygon(xpts, ypts, xpts.length);
					}

				}
			});
		};
	};
	{
		addAction(polygon);

	}

	DrawAction<TwoDVizContext, Graphics2D> poly_line = new DrawAction<TwoDVizContext, Graphics2D>(
			new Stereotype("poly_line", 3), DrawAction.THIRD) {
		public void act(TwoDVizContext ctx, Graphics2D sfc, Literal literal) {
			edu.bath.aspviz.lang.java2d.Polygon p = ctx.getPolygon(literal
					.getParam(0).toString());
			p.addPolyLine(termToPoint(literal.getParam(1)), termToPoint(literal
					.getParam(2)));
		};
	};
	{
		addAction(poly_line);
	}

	DrawAction<TwoDVizContext, Graphics2D> drawEllipse= new DrawAction<TwoDVizContext, Graphics2D>(
			new Stereotype("draw_ellipse", 4), DrawAction.THIRD) {
		public void act(TwoDVizContext ctx, Graphics2D sfc, Literal literal) {
			//brush,  point, w, h
			Brush b = ctx.getBrush(literal.getParam(0).toString());
			setupBrush(sfc, b);
			Point2D.Double p = termToPoint(literal.getParam(1));
			int w = ((NumberTerm)literal.getParam(2)).intValue();
			int h = ((NumberTerm)literal.getParam(3)).intValue();
			double sx = p.x - w/2.0;
			double sy = p.y - h/2.0;
			sfc.drawOval((int)sx, (int)sy, w,h);
			
		};
	};
	{
		addAction(drawEllipse);
	}

	DrawAction<TwoDVizContext, Graphics2D> fillEllipse= new DrawAction<TwoDVizContext, Graphics2D>(
			new Stereotype("fill_ellipse", 5), DrawAction.THIRD +100) {
		public void act(TwoDVizContext ctx, Graphics2D sfc, Literal literal) {
			//brush,  point, w, h
			Brush b = ctx.getBrush(literal.getParam(0).toString());
			Color fillColor = ctx.getColor(literal
					.getParam(1));

			Point2D.Double p = termToPoint(literal.getParam(2));
			int w = ((NumberTerm)literal.getParam(3)).intValue();
			int h = ((NumberTerm)literal.getParam(4)).intValue();
			double sx = p.x - w/2.0;
			double sy = p.y - h/2.0;
			sfc.setColor(fillColor);
			sfc.fillOval((int)sx, (int)sy, w,h);

			setupBrush(sfc, b);

			sfc.drawOval((int)sx, (int)sy, w,h);
		};
	};
	{
		addAction(fillEllipse);
	}

	@Override
	public void render(TwoDVizContext c, Graphics2D g2d, AnswerSet answerSet) {
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		super.render(c, g2d, answerSet);
	}
}
