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
        colorProvider 	= provider;
        stringToken 	= iStringToken;
	}

    private char instr = '0';

	@Override
	public IToken evaluate(ICharacterScanner scanner) {
        char ch = (char) scanner.read();
		boolean eod = (int) ch == 65535; // 0xFFFF - on 64bit Linux; need to catch otherwise Eclipse hangs
		boolean eof = ((int) ch) == ICharacterScanner.EOF;
		if (eod || eof) {
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
