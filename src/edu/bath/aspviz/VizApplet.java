package edu.bath.aspviz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;

import edu.bath.asplib.model.as.AnswerSet;
import edu.bath.asplib.parser.SmodelsLexer;
import edu.bath.asplib.parser.SmodelsParser;
import edu.bath.aspviz.lang.java2d.TwoDRenderer;
import edu.bath.aspviz.lang.java2d.TwoDVizContext;

public class VizApplet extends JApplet {
	JPanel surface;
	JTextArea input;
	JButton parse;
	AnswerSet answerSet;

	@Override
	public void init() {
		try {
			javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					setupGraphics();
				}
			});
		} catch (Exception e) {

		}

	}

	public void setupGraphics() {
		surface = new JPanel() {

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				if (answerSet != null) {
					TwoDRenderer twodr = new TwoDRenderer();
					TwoDVizContext ctx = new TwoDVizContext();
					twodr.render(ctx, g2d, answerSet);
				}
			}
		};
		surface.setMinimumSize(new Dimension(640, 480));

		getContentPane().add(surface, BorderLayout.CENTER);

		input = new JTextArea(50, 80);
		input.setMinimumSize(new Dimension(640, 320));
		getContentPane().add(input, BorderLayout.CENTER);
		input.setText("Copy answer set here");

		parse = new JButton("parse");
		getContentPane().add(parse, BorderLayout.SOUTH);
		parse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String asp = input.getText();
					SmodelsLexer lexer = new SmodelsLexer(new ANTLRInputStream(
							new ByteArrayInputStream(asp.getBytes())));
					SmodelsParser parser = new SmodelsParser(
							new CommonTokenStream(lexer));
					AnswerSet as = parser.answersetfromliterals();
					answerSet = as;

					surface.repaint();
				} catch (Exception exception) {

				}
			}
		});

	}

	public void parse() {

	}
}
