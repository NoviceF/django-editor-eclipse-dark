package org.kacprzak.eclipse.django_editor.editors.js;

import org.kacprzak.eclipse.django_editor.editors.ColorProvider;

public class JQueryRule extends JavaScriptBuiltInFunctionRule {

	public JQueryRule(ColorProvider colorProvider) {
		super(colorProvider);
		//setKeywords();
	}

	protected void setKeywords() {
    	objFunctions.clear();
    	gloFunctions.clear();

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
}
