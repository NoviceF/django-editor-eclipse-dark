package org.kacprzak.eclipse.django_editor.editors.html;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IRule;
import org.kacprzak.eclipse.django_editor.editors.AbsDjRuleBasedScanner;
import org.kacprzak.eclipse.django_editor.editors.ColorProvider;
import org.kacprzak.eclipse.django_editor.editors.dj.DjangoTagRule;
import org.kacprzak.eclipse.django_editor.editors.dj.DjangoVariableRule;

/**
 * @author Zbigniew Kacprzak
*/
public class HtmlTagScanner extends AbsDjRuleBasedScanner {

	public HtmlTagScanner(ColorProvider provider, String defColorName, String defStyleName) {
		super(provider, defColorName, defStyleName);
	}

	@Override
	protected List<IRule> createRules() {
		List<IRule> rules = new ArrayList<IRule>();

		rules.add(new DjangoTagRule(colorProvider));
		rules.add(new DjangoVariableRule(colorProvider));

		rules.add(new HtmlTagRule(colorProvider));
		return rules;
	}
}
