/*
 * Created on Oct 11, 2004
 */
package org.kacprzak.eclipse.django_editor.editors.format;

/**
 * @author Phil Zoio
 */
public class TextFormattingStrategy extends DefaultFormattingStrategy
{

	public TextFormattingStrategy()
	{
		super();
	}

	public String format(String content, boolean isLineStart, String indentation, int[] positions)
	{
		if (indentation.length() == 0)
			return content;
		return lineSeparator + content.trim() + lineSeparator + indentation;
	}

}