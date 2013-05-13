package org.kacprzak.eclipse.django_editor.editors;

import org.eclipse.jface.text.rules.IWordDetector;

public class GenericWordDetector implements IWordDetector {

	public boolean isWordStart(char c) {
		return Character.isJavaIdentifierStart(c);
	}

	/*
	 * @see IWordDetector#isWordPart
	 */
	public boolean isWordPart(char c) {
		return Character.isJavaIdentifierPart(c);
	}

}
