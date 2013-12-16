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
    protected IToken 				defaultToken;

	public NonDjangoStringRule(ColorProvider provider, IToken iStringToken, IToken iDefaultToken) {
        colorProvider 	= provider;
        stringToken 	= iStringToken;
        defaultToken    = iDefaultToken;
	}

    private char instr = '0';

	@Override
	public IToken evaluate(ICharacterScanner scanner) {
        char ch = (char) scanner.read();
		boolean eod = (int) ch == 65535; // 0xFFFF - on 64bit Linux; need to catch otherwise Eclipse hangs
		boolean eof = ((int) ch) == ICharacterScanner.EOF;
		if (eod || eof || ch == '\r' || ch == '\n') {
			instr = '0';
		    scanner.unread();
		    return Token.UNDEFINED;
		}
		
		if (ch == '"' || ch == '\'' || instr != '0') {
			if (instr == '0')
				instr = ch;
			else if (instr == ch)
				instr = '0';
		    return stringToken;
		}		
        scanner.unread();
        return Token.UNDEFINED;
	}
}
