package org.kacprzak.eclipse.django_editor.editors.dj;

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

/**
 * @author Zbigniew Kacprzak
*/
public class DjangoTagRule implements IRule, IDjangoSyntax {

    protected IWordDetector 		wordDetector;
    protected ColorProvider 		colorProvider;

    protected IToken 				delimiterToken;
    protected IToken 				usertagToken;
    protected IToken 				keywordToken;
    protected IToken 				defaultToken;
    protected IToken				userFilterToken;
    protected IToken				filterToken;
    protected IToken 				stringToken;

    protected Map<String, IToken> 	operators = new HashMap<String, IToken>();
    protected Map<String, IToken> 	keywords = new HashMap<String, IToken>();
    protected Map<String, IToken> 	filters = new HashMap<String, IToken>();
    
    private StringBuffer 			strBuffer = new StringBuffer();

	public DjangoTagRule(ColorProvider provider) {
        wordDetector = new GenericWordDetector();
        colorProvider = provider;

        defaultToken   = colorProvider.getToken(IDjangoPrefs.DEFAULT_FG_COLOR, IDjangoPrefs.DEFAULT_FG_STYLE);
        delimiterToken = colorProvider.getToken(IDjangoPrefs.DJDELIMITER_COLOR, IDjangoPrefs.DJDELIMITER_STYLE);
        usertagToken   = colorProvider.getToken(IDjangoPrefs.DJUSRTAG_COLOR, IDjangoPrefs.DJUSRTAG_STYLE);
        keywordToken   = colorProvider.getToken(IDjangoPrefs.DJKEYWORD_COLOR, IDjangoPrefs.DJKEYWORD_STYLE);
        filterToken		= colorProvider.getToken(IDjangoPrefs.DJFILTER_COLOR, IDjangoPrefs.DJFILTER_STYLE);
        userFilterToken = colorProvider.getToken(IDjangoPrefs.DJUSRFILTER_COLOR, IDjangoPrefs.DJUSRFILTER_STYLE);
        stringToken 	= colorProvider.getToken(IDjangoPrefs.DJSTRING_COLOR, IDjangoPrefs.DJSTRING_STYLE);

        setKeywords();
	}

    private void setKeywords() {
		for(String word: IDjangoSyntax.TAGS){
			keywords.put(word, keywordToken);
		}
		for(String word: IDjangoSyntax.END_TAGS){
			keywords.put(word, keywordToken);
		}
		for(String word: IDjangoSyntax.PREDICATES){
			operators.put(word, keywordToken);
		}
		for(String word: IDjangoSyntax.FILTERS) {
			filters.put(word, filterToken);
		}
    }

    private boolean isTagDelimiterStart(char c) {
    	return c == '{' || c == '%';
    }

    private char instr = '0';
    private boolean insideDjangoTag = false;
    private boolean prevWasTagDelimiter = false;
    private boolean prevWasFilterDelimiter = false;

	@Override
	public IToken evaluate(ICharacterScanner scanner) {
//		System.out.println("@@@ DjangoTagRule.evaluate BEGIN");
		try {
        char ch = (char) scanner.read();
        if (isTagDelimiterStart(ch)) {
            char nch = (char) scanner.read();
            if (ch == '{' && nch == '%') {
            	insideDjangoTag = true;
            	prevWasTagDelimiter = true;
            	return delimiterToken;
            } else if (ch == '%' && nch == '}') {
            	insideDjangoTag = false;
            	return delimiterToken;
            }
            scanner.unread();
            scanner.unread();
            return Token.UNDEFINED;
        }
        if (!insideDjangoTag) {
            scanner.unread();
            return Token.UNDEFINED;
        }

        if (ch == '"' || ch == '\'' || instr != '0') {
        	if (instr == '0')
        		instr = ch;
        	else if (instr == ch)
        		instr = '0';
        	return stringToken;

        } else if (ch == '|') {
        	prevWasFilterDelimiter = true;
        

        } else if (wordDetector.isWordStart(ch)) {
        	strBuffer = new StringBuffer();
            do {
            	strBuffer.append(ch);
                ch = (char) scanner.read();
            } while ( ((int) ch) != 65535 && ((int) ch) != ICharacterScanner.EOF && wordDetector.isWordPart(ch) );
            scanner.unread();

            String str = strBuffer.toString();

            if (prevWasFilterDelimiter) {
	            IToken token= (IToken) filters.get(str);
	            if (token != null)
	                return token;
	
	            prevWasFilterDelimiter = false;
	            return userFilterToken;
            }
            
            if (prevWasTagDelimiter) {
            	// expected tag's name here: either built-in or custom
            	prevWasTagDelimiter = false;
	            IToken token= (IToken) keywords.get(str);
	            if (token != null)
	                return token;
            	return usertagToken;
            }
            
            // some well known modifiers (like 'and' 'or' )
            IToken token= (IToken) operators.get(str);
            if (token != null) {
                return token;
            }

            prevWasTagDelimiter = false;
            return defaultToken;

        } else {
        	prevWasFilterDelimiter = false;
        } 

        scanner.unread();
        return Token.UNDEFINED;
		} finally {
//			System.out.println("@@* DjangoTagRule.evaluate END");
		}
	}
}
