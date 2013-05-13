package org.kacprzak.eclipse.django_editor.editors.dj;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class DjangoWhitespaceDetector implements IWhitespaceDetector {

	public boolean isWhitespace(char c) {
		return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
	}
}
