package edu.bath.aspviz;

import org.eclipse.swt.widgets.Display;

public class Main {

	public static void main(String [] args){
		MainWindow dpw = new MainWindow();
		dpw.setBlockOnOpen(true);
		dpw.open();
		Display.getCurrent().dispose();
	}
}
