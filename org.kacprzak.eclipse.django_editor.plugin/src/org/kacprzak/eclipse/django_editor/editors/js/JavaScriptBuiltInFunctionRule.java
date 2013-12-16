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

/**
 * @author Zbigniew Kacprzak
*/
public class JavaScriptBuiltInFunctionRule implements IRule {

    protected IWordDetector 		wordDetector;
    protected ColorProvider 		colorProvider;

    protected IToken 				functionToken;
    protected IToken 				defaultToken;
    protected IToken 				jqFunctionToken;
    protected IToken 				jqSelectorToken;

    protected Map<String, IToken> 	gloFunctions = new HashMap<String, IToken>();
    protected Map<String, IToken> 	objFunctions = new HashMap<String, IToken>();
    
    protected StringBuffer 			strBuffer = new StringBuffer();
    protected int                   unreadCnt = 0;
    protected String                prefix = "";

	public JavaScriptBuiltInFunctionRule(ColorProvider provider) {
        wordDetector = new GenericWordDetector();
        colorProvider = provider;

        defaultToken  = colorProvider.getToken(IDjangoPrefs.DEFAULT_FG_COLOR, IDjangoPrefs.DEFAULT_FG_STYLE);
		functionToken = colorProvider.getToken(IDjangoPrefs.JSFUNCTION_COLOR, IDjangoPrefs.JSFUNCTION_STYLE);
		jqFunctionToken = colorProvider.getToken(IDjangoPrefs.JQFUNCTION_COLOR, IDjangoPrefs.JQFUNCTION_STYLE);
		jqSelectorToken = colorProvider.getToken(IDjangoPrefs.JQSELECTOR_COLOR, IDjangoPrefs.JQSELECTOR_STYLE);
        setKeywords();
	}

    protected void setKeywords() {
    	objFunctions.clear();
    	gloFunctions.clear();
		for(String word: IJavaScriptSyntax.OBJ_FUNCTIONS){
			objFunctions.put(word, functionToken);
		}
		for(String word: IJavaScriptSyntax.GLO_FUNCTIONS){
			gloFunctions.put(word, functionToken);
		}
		
    	for (String word: IJQuerySyntax.EVENTS)
			objFunctions.put(word, jqFunctionToken);
		for (String word: IJQuerySyntax.EFFECTS)
			objFunctions.put(word, jqFunctionToken);
		for (String word: IJQuerySyntax.HTML)
			objFunctions.put(word, jqFunctionToken);
		for (String word: IJQuerySyntax.TRAVERSING)
			objFunctions.put(word, jqFunctionToken);
		for (String word: IJQuerySyntax.AJAX)
			objFunctions.put(word, jqFunctionToken);
		for (String word: IJQuerySyntax.MISC)
			objFunctions.put(word, jqFunctionToken);

		for (String word: IJQuerySyntax.GLOBAL)
			gloFunctions.put(word, jqFunctionToken);
		
    }

    protected boolean isFunctionNameStart(char c, ICharacterScanner scanner) {
    	prefix = "";
    	if (c == '.') {
    		unreadCnt = 1;
    		functionNameStarted = true;
    		return true;
    	}
//    	if (c == '$') {
//    		unreadCnt = 1;
//    		c = (char)scanner.read();
//    		if (c != '.') {
//    			scanner.unread();
//    			return false;
//    		}
//    		prefix = "$.";
//    		unreadCnt = 2;
//    		functionNameStarted = true;
//    		return true;
//    	}
    	if (Character.isWhitespace(c)) {
    		c = (char)scanner.read();
    		while ( Character.isWhitespace(c) ) {
    			int ich = (int) c;
    			if (ich == 65535 || ich == ICharacterScanner.EOF) {
    				scanner.unread();
    				return false;
    			}
    			c = (char)scanner.read();
    		}
    		functionNameStarted = true;
    		scanner.unread();
    		return true;
//    	} else if (Character.isJavaIdentifierPart(c)) {
//    		//scanner.unread();
//    		functionNameStarted = true;
//    		return false;
    	}
    	return false;
    }

    protected boolean isFunctionNameEnd(char ch, ICharacterScanner scanner) {
    	int aCnt = 0;
    	boolean aRet = true;
    	while (ch != '(') {
    		int ich = (int) ch;
        	if (ich == 65535 || ich == ICharacterScanner.EOF || !Character.isWhitespace(ch)) {
        		aRet = false;
        		break;
        	}
        	ch = (char) scanner.read();
        	aCnt++;
        }
    	for (int i=0; i<aCnt; i++)
    		scanner.unread();
    	return aRet;
    }

    protected boolean functionNameStarted = false;

	@Override
	public IToken evaluate(ICharacterScanner scanner) {
        char ch = (char) scanner.read();
        if (!functionNameStarted) 
        	if (isFunctionNameStart(ch, scanner))        	
        		return defaultToken;
        if (functionNameStarted) {
        	if (wordDetector.isWordStart(ch)) {
	        	strBuffer = new StringBuffer();
	            do {
	            	strBuffer.append(ch);
	                ch = (char) scanner.read();
	            } while ( ((int) ch) != 65535 && ((int) ch) != ICharacterScanner.EOF && wordDetector.isWordPart(ch) );
	            String str = strBuffer.toString();
	
	            if (isFunctionNameEnd(ch, scanner)) {	            	
	            	IToken tokenO = (IToken) objFunctions.get(prefix + str);
	            	IToken tokenG = (IToken) gloFunctions.get(prefix + str);
	            	if (tokenO != null || tokenG != null) {
	            		scanner.unread();
	            		if (tokenG != null)//&& functionNameStarted == false)
	            			return tokenG;
	            		else if (tokenO != null) {// && functionNameStarted == true) {
	        	            functionNameStarted = false;
	            			return tokenO;
	            		}
	            	}
	            }
	            for (int i=0; i<str.length(); i++)
	            	scanner.unread();
        	}
        	for (int i=0; i<unreadCnt; i++)
        		scanner.unread();
        	functionNameStarted = false;
        }

        scanner.unread();
        return Token.UNDEFINED;
	}
}
