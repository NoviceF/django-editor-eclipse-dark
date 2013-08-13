package org.kacprzak.eclipse.django_editor.editors.js;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.NumberRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.WordRule;
import org.kacprzak.eclipse.django_editor.editors.AbsDjRuleBasedScanner;
import org.kacprzak.eclipse.django_editor.editors.ColorProvider;
import org.kacprzak.eclipse.django_editor.editors.GenericWordDetector;
import org.kacprzak.eclipse.django_editor.editors.dj.DjangoTagRule;
import org.kacprzak.eclipse.django_editor.editors.dj.DjangoVariableRule;
import org.kacprzak.eclipse.django_editor.editors.dj.NonDjangoStringRule;
import org.kacprzak.eclipse.django_editor.preferences.IDjangoPrefs;

public class JavaScriptScanner extends AbsDjRuleBasedScanner {

	public static final String KEYWORDS[] = {
			"abstract",
			"boolean", "break", "byte",
			"case", "catch", "char", "class", "const", "continue",
			"default", "do", "double",
			"else", "extends",
			"false", "final", "finally", "float", "for", "function",
			"goto", "if", "implements", "import", "in", "instanceof", "int", "interface",
			"long",
			"native", "new", "null",
			"package", "private", "protected", "prototype", "public",
			"return", "short", "static", "super", "switch", "synchronized",
			"this", "throw", "throws", "transient", "true", "try",
			"var", "void", "while", "with"
	};

	public JavaScriptScanner(ColorProvider provider, String defColorName, String defStyleName) {
		super(provider, defColorName, defStyleName);
	}

	@Override
	protected List<IRule> createRules() {
		IToken normal  = colorProvider.getToken(IDjangoPrefs.DEFAULT_FG_COLOR, IDjangoPrefs.DEFAULT_FG_STYLE);
		IToken script  = colorProvider.getToken(IDjangoPrefs.HTMLSCRIPT_COLOR, IDjangoPrefs.HTMLSCRIPT_STYLE);
		IToken string  = colorProvider.getToken(IDjangoPrefs.JSSTRING_COLOR, IDjangoPrefs.JSSTRING_STYLE);
		IToken comment = colorProvider.getToken(IDjangoPrefs.JSCOMMENT_COLOR, IDjangoPrefs.JSCOMMENT_STYLE);
		IToken keyword = colorProvider.getToken(IDjangoPrefs.JSKEYWORD_COLOR, IDjangoPrefs.JSKEYWORD_STYLE);
		IToken number  = colorProvider.getToken(IDjangoPrefs.JSNUMBER_COLOR, IDjangoPrefs.JSNUMBER_STYLE);

		List<IRule> rules = new ArrayList<IRule>();
		rules.add(new EndOfLineRule("//", comment));
		rules.add(new MultiLineRule("/*", "*/", comment));
		rules.add(new SingleLineRule("<script", ">", script));
		rules.add(new SingleLineRule("</script", ">", script));

		//rules.add(new JQueryRule(colorProvider));

		rules.add(new NonDjangoStringRule(colorProvider, string));
		rules.add(new DjangoVariableRule(colorProvider));
		rules.add(new DjangoTagRule(colorProvider));

		//rules.add(new SingleLineRule("$(", ")", script));
		
		WordRule wordRule = new WordRule(new GenericWordDetector(), normal);
		for(int i=0;i<KEYWORDS.length;i++){
			wordRule.addWord(KEYWORDS[i], keyword);
		}
		rules.add(wordRule);
		rules.add(new NumberRule(number));
		
		return rules;
	}

}
