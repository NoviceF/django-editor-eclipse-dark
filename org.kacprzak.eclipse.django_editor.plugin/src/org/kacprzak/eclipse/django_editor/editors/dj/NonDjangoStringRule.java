package org.kacprzak.eclipse.django_editor.editors.dj;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.kacprzak.eclipse.django_editor.editors.ColorProvider;

/**
 * @author Zbigniew Kacprzak
*/
public class NonDjangoStringRule implements IRule {

    protected ColorProvider 		colorProvider;
    protected IToken 				stringToken;

	public NonDjangoStringRule(ColorProvider provider, IToken iStringToken) {
        colorProvider = provider;
        stringToken 	= iStringToken;
	}

    private boolean detectedDjangoTag(char ch, ICharacterScanner scanner) {
    	if (ch != '{' && ch != '%') 
    		return false;
        char nch = (char) scanner.read();
        scanner.unread();
        if ( ch == '{' && (nch == '%' || nch == '{') ) {
        	insideDjTag = true;
        	return true;
        } else if ( (ch == '%' || ch == '}') && nch == '}') {
        	insideDjTag = false;
        	return true;
        }
        return false;
    }

    private char instr = '0';
    private boolean insideDjTag = false;

	@Override
	public IToken evaluate(ICharacterScanner scanner) {
        char ch = (char) scanner.read();
        if (detectedDjangoTag(ch, scanner)) {
        	scanner.unread();
            return Token.UNDEFINED;
        }
        if (! insideDjTag) {
	        if (ch == '"' || ch == '\'' || instr != '0') {
	        	if (instr == ch) {
	        		instr = '0';
	        		return stringToken;
	        	}
	        	if (instr == '0')
	        		instr = ch;

	            do {
	                ch = (char) scanner.read();
	            } while ( ((int) ch) != ICharacterScanner.EOF && ch != '{' && ch != instr );
	            if (ch != instr) {
	            	if (detectedDjangoTag(ch, scanner)) {
	            		scanner.unread();
	            	}
	            } else
	            	instr = '0';
	        	return stringToken;
	        }
        }

        scanner.unread();
        return Token.UNDEFINED;
	}
}
