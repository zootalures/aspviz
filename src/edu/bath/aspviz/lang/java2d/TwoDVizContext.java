package edu.bath.aspviz.lang.java2d;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.bath.asplib.model.BasePredicate;
import edu.bath.asplib.model.NumberTerm;
import edu.bath.asplib.model.Term;
import edu.bath.aspviz.Brush;
import edu.bath.aspviz.DrawContext;
import edu.bath.aspviz.FontInfo;
import edu.bath.aspviz.ObjectNotFoundException;

public class TwoDVizContext extends DrawContext {
	Map<String, Color> colors = new HashMap<String, Color>();
	Map<String, BufferedImage> sprites = new HashMap<String, BufferedImage>();
	Map<String, Polygon> polygons = new HashMap<String, Polygon>();
	public Brush defaultBrush = new Brush();
	public Font defaultFont = new Font("sans", 0, 12);
	Map<String, Brush> brushes = new HashMap<String, Brush>();
	Map<String, FontInfo> fonts = new HashMap<String, FontInfo>();
	int scale = 1;
	Logger log = Logger.getLogger(getClass());
	List<File> fileSearchPaths = new ArrayList<File>();

	public List<File> getFileSearchPaths() {
		return fileSearchPaths;
	}

	public void setFileSearchPaths(List<File> fileSearchPaths) {
		this.fileSearchPaths = fileSearchPaths;
	}

	public File findFileByName(String name) throws FileNotFoundException {
		for (File path : fileSearchPaths) {
			log.debug("Looking in " + path  + " for " + name);
			
			if (path!=null && path.isDirectory()) {
				
				File nf = new File(path.getAbsolutePath() + File.separator +  name);
				if (nf.exists()) {
					log.debug("Found " + nf  + " for " + name);

					return nf;
				}
			}
		}
		throw new FileNotFoundException("File " + name + " not found");

	}

	public Polygon getPolygon(String polygon) {
		Polygon pg = polygons.get(polygon);
		if (pg == null)
			throw new ObjectNotFoundException("Polygon " + polygon
					+ " not found");
		return pg;
	}

	public void setPolygon(String name, Polygon polly) {
		polygons.put(name, polly);
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public TwoDVizContext() {
		defaultBrush.setColor(new Color(0, 0, 0));
		defaultBrush.setWidth(1);
	}

	public Brush getBrush(String brush) {
		Brush b = brushes.get(brush);
		if (b == null) {
			throw new RuntimeException("Brush not found :" + brush);
		}
		return b;
	}

	public void setBrush(String id, Brush b) {
		brushes.put(id, b);
	}

	public BufferedImage getSprite(String sprite) {
		BufferedImage b = sprites.get(sprite);
		if (b == null) {
			throw new RuntimeException("Sprite not found: " + sprite);
		}
		return b;
	}

	public void setSprite(String id, BufferedImage s) {
		sprites.put(id, s);
	}

	public Font getFontData(String font) {
		FontInfo fi = fonts.get(font);
		;
		Font f = fi.getFont();
		if (f == null) {
			throw new RuntimeException("Brush not found :" + font);
		}
		return f;
	}

	public FontInfo getFont(String font) {
		FontInfo fi = fonts.get(font);
		;
		if (fi == null) {
			throw new RuntimeException("Brush not found :" + font);
		}
		return fi;
	}

	public void setFont(String id, FontInfo f) {
		fonts.put(id, f);
	}

	public static final String DEFAULT_BRUSH = "default";
	{
		brushes.put(DEFAULT_BRUSH, defaultBrush);
	}

	public Color getColor(String color) {
		return colors.get(color);
	}

	public Color getColor(Term color) {
		if (color instanceof BasePredicate
				&& ((BasePredicate) color).getArity() == 3
				&& ((BasePredicate) color).getName().equals("rgb")) {
			BasePredicate bp = (BasePredicate) color;
			return new Color(((NumberTerm) bp.getParam(0)).intValue(),
					((NumberTerm) bp.getParam(1)).intValue(), ((NumberTerm) bp
							.getParam(2)).intValue());

		}
		return colors.get(color.toString());

	}

	public void setColor(String color, Color colorval) {

		colors.put(color, colorval);
	}
}
