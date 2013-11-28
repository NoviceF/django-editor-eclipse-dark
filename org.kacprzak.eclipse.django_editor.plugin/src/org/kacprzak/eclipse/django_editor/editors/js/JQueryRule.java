package org.kacprzak.eclipse.django_editor.editors.js;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;
import org.kacprzak.eclipse.django_editor.editors.ColorProvider;
import org.kacprzak.eclipse.django_editor.editors.GenericWordDetector;
import org.kacprzak.eclipse.django_editor.preferences.IDjangoPrefs;

public class JQueryRule implements IRule {

    protected IWordDetector 		wordDetector;

    protected IToken 				defaultToken;
    protected IToken 				keywordToken;
    protected IToken 				jQueryDelimiter;
    protected IToken 				stringToken;

    protected Map<String, IToken> 	keywords = new HashMap<String, IToken>();
    
	public JQueryRule(ColorProvider colorProvider) {
		wordDetector 	= new GenericWordDetector();
        defaultToken    = colorProvider.getToken(IDjangoPrefs.DEFAULT_FG_COLOR, IDjangoPrefs.DEFAULT_FG_STYLE);
        keywordToken 	= colorProvider.getToken(IDjangoPrefs.JSKEYWORD_COLOR, IDjangoPrefs.JSKEYWORD_STYLE);
        stringToken 	= colorProvider.getToken(IDjangoPrefs.JSSTRING_COLOR, IDjangoPrefs.JSSTRING_STYLE);
        jQueryDelimiter = colorProvider.getToken(IDjangoPrefs.DJDELIMITER_COLOR, IDjangoPrefs.DJDELIMITER_STYLE);
	}

    private boolean jQueryStart(final char c, ICharacterScanner scanner) {
    	if (c == '$') {
        	char cc = (char) scanner.read();
        	if (cc == '(') 
        		return true;
    		scanner.unread();
    	}
    	return false;
    }
    private boolean jQueryEnd(char c) {
    	return c == ')';
    }

    private boolean jQuery = false;
    private char instr = '0';
    
	@Override
	public IToken evaluate(ICharacterScanner scanner) {
System.out.println("++ JQueryRule.evaluate BEGIN");
		try {
        char ch = (char) scanner.read();
System.out.println("JQueryRule.evaluate(1); ch=" + ch);
        if (jQueryStart(ch, scanner)) {
        	jQuery = true;
            return jQueryDelimiter;

        } else if (jQuery && jQueryEnd(ch)) {
        	jQuery = false;
        	return jQueryDelimiter;
        	
        } else {
            
        	boolean eod = ((int) ch) == 65535; // 0xFFFF - on 64bit Linux; need to catch otherwise Eclipse hangs
        	boolean eof = ((int) ch) == ICharacterScanner.EOF;
        	if (eod || eof) {
System.out.println("JQueryRule.evaluate(5); EOF|EOD ch=" + ch);
                scanner.unread();
                return Token.UNDEFINED;
            }

            if (ch == '"' || ch == '\'' || instr != '0') {
            	if (instr == '0')
            		instr = ch;
            	else if (instr == ch)
            		instr = '0';
                return stringToken;

            } else if (wordDetector.isWordPart(ch)) {
            	StringBuffer strBuffer = new StringBuffer();
System.out.println("JQueryRule.evaluate(2-do-while)");
            	do {
                	strBuffer.append(ch);
                    ch = (char) scanner.read();
System.out.println("JQueryRule.evaluate(2); ch=" + ch);
                } while ( ((int) ch) != 65535 && ((int) ch) != ICharacterScanner.EOF && wordDetector.isWordPart(ch) );
System.out.println("JQueryRule.evaluate(2a); unread" + ch);
                scanner.unread();

                String str = strBuffer.toString();
                
                // some well known modifiers (like 'and' 'or' )
                IToken token= (IToken) keywords.get(str);
                if (token != null)
                    return token;
                
                return keywordToken;
            }

        }
        scanner.unread();
        return Token.UNDEFINED;
		} finally {
			System.out.println("+* JQueryRule.evaluate END");
		}
	}
}
