package org.kacprzak.eclipse.django_editor.editors;

import org.eclipse.jface.text.rules.IWordDetector;

public class GenericNonWordDetector implements IWordDetector {

	private static String VALID = "+-*/%=><&|^~!?:";
	
	public boolean isWordStart(char c) {
		return VALID.indexOf(c) >= 0;
	}

	/*
	 * @see IWordDetector#isWordPart
	 */
	public boolean isWordPart(char c) {
		return VALID.indexOf(c) >= 0;
	}

}
