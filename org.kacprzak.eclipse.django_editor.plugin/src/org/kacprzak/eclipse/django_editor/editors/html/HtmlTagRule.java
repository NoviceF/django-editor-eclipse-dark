package org.kacprzak.eclipse.django_editor.editors.html;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;
import org.kacprzak.eclipse.django_editor.editors.ColorProvider;
import org.kacprzak.eclipse.django_editor.editors.GenericWordDetector;
import org.kacprzak.eclipse.django_editor.preferences.IDjangoPrefs;

public class HtmlTagRule implements IRule {

    protected IWordDetector 		wordDetector;

    protected IToken 				defaultToken;
    protected IToken 				attributeToken;
    protected IToken 				stringToken;

	public HtmlTagRule(ColorProvider colorProvider) {
		wordDetector 	= new GenericWordDetector();
        defaultToken 	= colorProvider.getToken(IDjangoPrefs.HTMLTAG_COLOR, IDjangoPrefs.HTMLTAG_STYLE);
        attributeToken 	= colorProvider.getToken(IDjangoPrefs.HTMLTAG_ATTR_COLOR, IDjangoPrefs.HTMLTAG_ATTR_STYLE);
        stringToken 	= colorProvider.getToken(IDjangoPrefs.HTMLSTRING_COLOR, IDjangoPrefs.HTMLSTRING_STYLE);
	}

    private boolean isTagStart(char c) {
    	return c == '<';
    }
    private boolean isTagEnd(char c) {
    	return c == '>';
    }

    private boolean attr = false;
    private char instr = '0';
    
	@Override
	public IToken evaluate(ICharacterScanner scanner) {
        char ch = (char) scanner.read();
        if (isTagStart(ch)) {
        	attr = false;
        	instr = '0';
        	ch = (char) scanner.read();
        	if (ch != '/')
        		scanner.unread();
            do {
                ch = (char) scanner.read();
            } while ( ((int) ch) != ICharacterScanner.EOF && wordDetector.isWordPart(ch) );
            scanner.unread();
            return defaultToken;

        } else {
            if (ch == '/') {
            	char nch = (char) scanner.read();
            	if (isTagEnd(nch)) {
            		instr = '0';
            		attr = false;
            		return defaultToken;
            	}
            } else if (isTagEnd(ch)) {
            	attr = false;
            	instr = '0';
            	return defaultToken;
            }
            
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
            	attr = false;
                return stringToken;

            } else if (!attr && (wordDetector.isWordPart(ch) || ch == '}') ) {
                do {
                    ch = (char) scanner.read();
                } while ( wordDetector.isWordPart(ch));
                scanner.unread();
                attr = true;
                instr = '0';
                return attributeToken;
            }

        }
        scanner.unread();
        return Token.UNDEFINED;
	}
}
