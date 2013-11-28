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
public class DjangoVariableRule implements IRule, IDjangoSyntax {

    protected IWordDetector 		wordDetector;
    protected IToken 				defaultToken;
    protected IToken 				delimiterToken;
    protected IToken				userFilterToken;
    protected IToken				filterToken;
    protected Map<String, IToken> 	ruleWords = new HashMap<String, IToken>();
    private StringBuffer 			strBuffer = new StringBuffer();

	public DjangoVariableRule(ColorProvider colorProvider) {
		wordDetector 	= new GenericWordDetector();
        defaultToken 	= colorProvider.getToken(IDjangoPrefs.DJVARIABLE_COLOR, IDjangoPrefs.DJVARIABLE_STYLE);
        delimiterToken 	= colorProvider.getToken(IDjangoPrefs.DJDELIMITER_COLOR, IDjangoPrefs.DJDELIMITER_STYLE);
        filterToken		= colorProvider.getToken(IDjangoPrefs.DJFILTER_COLOR, IDjangoPrefs.DJFILTER_STYLE);
        userFilterToken = colorProvider.getToken(IDjangoPrefs.DJUSRFILTER_COLOR, IDjangoPrefs.DJUSRFILTER_STYLE);

        setKeywords();
	}

    public void setKeywords() {
		for(String word: IDjangoSyntax.FILTERS) {
			ruleWords.put(word, filterToken);
		}
    }

    private boolean isTagDelimiterStart(char c) {
    	return c == '{' || c == '}';
    }

    private boolean prevWasFilterDelimiter = false;
    private boolean insideDjangoTag = false;

	@Override
	public IToken evaluate(ICharacterScanner scanner) {
//		System.out.println("### DjangoVariableRule.evaluate BEGIN");
		try {
        char ch = (char) scanner.read();
        if (isTagDelimiterStart(ch)) {
            char nch = (char) scanner.read();
            if (ch == '{' && nch == '{') {
            	insideDjangoTag = true;
            	return delimiterToken;
            } else if (ch == '}' && nch == '}') {
            	insideDjangoTag = false;
            	prevWasFilterDelimiter = false;
            	return delimiterToken;
            }
            scanner.unread();

        } else if (insideDjangoTag && ch == '|') {
        	prevWasFilterDelimiter = true;
        
        } else if ( insideDjangoTag && wordDetector.isWordStart(ch)) {
        	strBuffer = new StringBuffer();
            do {
            	strBuffer.append(ch);
                ch = (char) scanner.read();
            } while ( ((int) ch) != 65535 && ((int) ch) != ICharacterScanner.EOF && wordDetector.isWordPart(ch) );
            scanner.unread();

            String str = strBuffer.toString();
            IToken token= (IToken) ruleWords.get(str);
            if (token != null && prevWasFilterDelimiter) {
                return token;
            }

            if (prevWasFilterDelimiter)
            	return userFilterToken;

            prevWasFilterDelimiter = false;
            return defaultToken;

        } else {
        	prevWasFilterDelimiter = false;
        }

        scanner.unread();
        return Token.UNDEFINED;
		} finally {
//			System.out.println("##* DjangoVariableRule.evaluate END");
		}
	}
}
