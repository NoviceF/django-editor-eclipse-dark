package org.kacprzak.eclipse.django_editor.editors.format;

import org.eclipse.jface.text.formatter.IFormattingStrategy;


/**
 * 
 * @author Phil Zoio
 */
public class DefaultFormattingStrategy implements IFormattingStrategy
{
	protected static final String lineSeparator = System.getProperty("line.separator");

	public DefaultFormattingStrategy() {
		super();
	}

	public void formatterStarts(String initialIndentation) {}
	public void formatterStops() {}

	public String format(String content, boolean isLineStart, String indentation, int[] positions) {
		return "";
	}


}
