package org.kacprzak.eclipse.django_editor.editors.dj;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.kacprzak.eclipse.django_editor.editors.AbsDjRuleBasedScanner;
import org.kacprzak.eclipse.django_editor.editors.ColorProvider;
import org.kacprzak.eclipse.django_editor.preferences.IDjangoPrefs;

/**
 * @author Zbigniew Kacprzak
*/
public class DjangoVariableScanner extends AbsDjRuleBasedScanner {// implements IDjangoSyntax {

	public DjangoVariableScanner(ColorProvider provider, String defColorName, String defStyleName) {
		super(provider, defColorName, defStyleName);
	}

	@Override
	protected List<IRule> createRules() {
		IToken string = colorProvider.getToken(IDjangoPrefs.DJSTRING_COLOR, store.getInt(IDjangoPrefs.DJSTRING_STYLE));

		List<IRule> rules = new ArrayList<IRule>();

		rules.add(new SingleLineRule("\"", "\"", string, '\\'));
		rules.add(new SingleLineRule("'", "'", string, '\\'));
		//rules.add(new WhitespaceRule(new DjangoWhitespaceDetector()));

		rules.add(new DjangoVariableRule(colorProvider));
		return rules;
	}
}
