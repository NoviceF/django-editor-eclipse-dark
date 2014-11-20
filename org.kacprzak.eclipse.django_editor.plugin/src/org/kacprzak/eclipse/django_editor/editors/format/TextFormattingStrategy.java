package org.kacprzak.eclipse.django_editor.editors.format;

import org.eclipse.jface.text.formatter.IFormattingStrategy;

/**
 * @author Phil Zoio
 */
public class TextFormattingStrategy implements IFormattingStrategy
{
	protected static final String lineSeparator = System.getProperty("line.separator");

	public TextFormattingStrategy()	{
		super();
	}
	
	public void formatterStarts(String initialIndentation) {}
	public void formatterStops() {}

	public String format(String content, boolean isLineStart, String indentation, int[] positions)
	{
		if (indentation.length() == 0)
			return content;
		return lineSeparator + content.trim() + lineSeparator + indentation;
	}

}