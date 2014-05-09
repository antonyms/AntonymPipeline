package com.ibm.bluej.util.common.visualization;

import java.awt.*;
import java.util.*;

import javax.swing.text.*;
import javax.swing.text.Highlighter.Highlight;

import com.ibm.bluej.util.common.*;


public class Highlighting {
	private static HashMap<Color,DefaultHighlighter.DefaultHighlightPainter> painters = new HashMap<Color,DefaultHighlighter.DefaultHighlightPainter>();
	
	private static HashMap<Color,UnderlineHighlighter.UnderlineHighlightPainter> underlines = new HashMap<Color,UnderlineHighlighter.UnderlineHighlightPainter>();
	
	public static void removeAllHighlights(JTextComponent text) {
		text.getHighlighter().removeAllHighlights();
	}

	/**
	 * removes all highlights of a specific color (assuming they were applied with this highlighting class)
	 * @param text
	 * @param c
	 */
	public static void removeAllHighlights(JTextComponent text, Color c) {
		DefaultHighlighter.DefaultHighlightPainter painter = painters.get(c);
		if (c == null)
			return;
		Highlighter high = text.getHighlighter();
		for (Highlight h : high.getHighlights()) {
			if (h.getPainter() == painter)
				high.removeHighlight(h);
		}
	}
	
	public static void highlightAll(JTextComponent text, Map<Span, Color> highlights) {
		for (Map.Entry<Span, Color> e : highlights.entrySet()) {
			Highlighting.highlight(text, e.getKey(), e.getValue());
		}
	}
	
	public static Object highlight(JTextComponent text, Span span, Color c) {
		Object highlight = null;
		DefaultHighlighter.DefaultHighlightPainter painter = null;
		synchronized (painters) {
			painter = painters.get(c);
			if (painter == null) {
				painter = new DefaultHighlighter.DefaultHighlightPainter(c);
				painters.put(c, painter);
			}
		}
		try {
			highlight = text.getHighlighter().addHighlight(span.start, span.end, painter);
		} catch (BadLocationException e) {
			System.err.println(e);
		}
		return highlight;
	}
	
	public static Object underline(JTextComponent text, Span span, Color c) {
		Object highlight = null;
		UnderlineHighlighter.UnderlineHighlightPainter painter = null;
		synchronized (underlines) {
			painter = underlines.get(c);
			if (painter == null) {
				painter = new UnderlineHighlighter.UnderlineHighlightPainter(c);
				underlines.put(c, painter);
			}
		}	
		try {
			highlight = text.getHighlighter().addHighlight(span.start, span.end, painter);
		} catch (BadLocationException e) {
			System.err.println(e);
		}
		return highlight;
	}
}
