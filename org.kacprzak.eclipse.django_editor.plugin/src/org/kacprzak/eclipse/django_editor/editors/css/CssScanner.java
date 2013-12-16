package org.kacprzak.eclipse.django_editor.editors.css;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.kacprzak.eclipse.django_editor.IDjangoPartitions;
import org.kacprzak.eclipse.django_editor.editors.AbsDjRuleBasedScanner;
import org.kacprzak.eclipse.django_editor.editors.ColorProvider;
import org.kacprzak.eclipse.django_editor.editors.dj.DjangoTagRule;
import org.kacprzak.eclipse.django_editor.editors.dj.DjangoVariableRule;
import org.kacprzak.eclipse.django_editor.preferences.IDjangoPrefs;

public class CssScanner extends AbsDjRuleBasedScanner {

	public CssScanner(ColorProvider provider) {
		super(provider);
	}

	public CssScanner(ColorProvider colorProvider, String defColorName, String defStyleName) {
		super(colorProvider, defColorName, defStyleName);
	}

	@Override
	protected List<IRule> createRules() {
		IToken tag = colorProvider.getToken(IDjangoPrefs.HTMLSCRIPT_COLOR, IDjangoPrefs.HTMLSCRIPT_STYLE);
		IToken comment = colorProvider.getToken(IDjangoPrefs.CSSCOMMENT_COLOR, IDjangoPrefs.CSSCOMMENT_STYLE);
		IToken djComment = colorProvider.getToken(IDjangoPrefs.DJCOMMENT_COLOR, IDjangoPrefs.DJCOMMENT_STYLE);

		List<IRule> rules = new ArrayList<IRule>();
		rules.add(new SingleLineRule("<style", ">", tag));
		rules.add(new SingleLineRule("</style", ">", tag));
		rules.add(new MultiLineRule("/*", "*/", comment));

		rules.add(new MultiLineRule("{% comment %}", "{% endcomment %}", djComment));
		rules.add(new DjangoTagRule(colorProvider));
		rules.add(new DjangoVariableRule(colorProvider));

		rules.add(new CssRule(
				colorProvider.getToken(IDjangoPrefs.CSSSELECTOR_COLOR, IDjangoPrefs.CSSSELECTOR_STYLE),
				colorProvider.getToken(IDjangoPrefs.CSSPROP_COLOR, IDjangoPrefs.CSSPROP_STYLE),
				colorProvider.getToken(IDjangoPrefs.CSSVALUE_COLOR, IDjangoPrefs.CSSVALUE_STYLE)));
		return rules;
	}

}
