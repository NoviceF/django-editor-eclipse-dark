package org.kacprzak.eclipse.django_editor.editors.css;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class CssRule implements IPredicateRule {

	private IToken selectorToken;
	private IToken propToken;
	private IToken valueToken;

	/**
	 * Constructor.
	 *
	 * @param propToken a property token
	 * @param valueToken a value token
	 */
	public CssRule(IToken selectorToken, IToken propToken, IToken valueToken){
		this.propToken = propToken;
		this.valueToken = valueToken;
		this.selectorToken = selectorToken;
	}

	private boolean sequenceDetected(ICharacterScanner scanner, char[] sequence, boolean eofAllowed) {
		for (int i= 1; i < sequence.length; i++) {
			int c= scanner.read();
			if (c == ICharacterScanner.EOF && eofAllowed) {
				return true;
			} else if (c != sequence[i]) {
				// Non-matching character detected, rewind the scanner back to the start.
				// Do not unread the first character.
				scanner.unread();
				for (int j= i-1; j > 0; j--)
					scanner.unread();
				return false;
			}
		}

		return true;
	}

	private IToken getToken(ICharacterScanner scanner) {
		int c;
		char[][] delimiters= scanner.getLegalLineDelimiters();
		while ((c= scanner.read()) != ICharacterScanner.EOF) {
			if (c == ':') {
				return propToken;
			} else if (c == ';') {
				return valueToken;
			} else if (c == '{' || c == '}') {
				return selectorToken;
			} else {
				// Check for end of line since it can be used to terminate the pattern.
				for (int i= 0; i < delimiters.length; i++) {
					if (c == delimiters[i][0] && sequenceDetected(scanner, delimiters[i], true)) {
						return null;
					}
				}
			}
		}
		scanner.unread();
		return null;
	}

	public IToken evaluate(ICharacterScanner scanner) {
		return evaluate(scanner, false);
	}
	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		if (resume) {
			IToken token = getToken(scanner);
			if (token!=null) return token;
		} else {
			int c = scanner.read();
			if (c != ' ' && c != '\t' && c!='\r' && c!='\n'){
				IToken token = getToken(scanner);
				if (token!=null) return token;
			}
		}

		scanner.unread();
		return Token.UNDEFINED;
	}


	public IToken getSuccessToken() {
		return null;
	}

}
