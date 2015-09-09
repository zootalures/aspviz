package edu.bath.aspviz;

import java.awt.Font;
import java.awt.Color;

public class FontInfo {
	Font fontData;
	public Font getFont() {
		return fontData;
	}
	public void setFont(Font fontData) {
		this.fontData = fontData;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public Color getBgColor() {
		return bgColor;
	}
	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}
	Color color;
	Color bgColor;
	
}
