package org.kacprzak.eclipse.django_editor.editors;

import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.kacprzak.eclipse.django_editor.preferences.IDjangoPrefs;

/**
 * Abstract rule scanner
 * @author Zbigniew Kacprzak
*/
public abstract class AbsDjRuleBasedScanner extends RuleBasedScanner {

	protected IPreferenceStore 	store;
	protected ColorProvider 	colorProvider;
    private String defaultColorName;
    private String defaultStyleName;

	public AbsDjRuleBasedScanner(ColorProvider provider) {
		init(provider, IDjangoPrefs.DEFAULT_FG_COLOR, IDjangoPrefs.DEFAULT_FG_STYLE);
	}

	public AbsDjRuleBasedScanner(ColorProvider provider, String defColorName, String defStyleName) {
		init(provider, defColorName, defStyleName);
	}

	private void init(ColorProvider provider, String defColorName, String defStyleName) {
		this.colorProvider = provider;
		this.store = provider.store;
        this.defaultColorName = defColorName;
        this.defaultStyleName = defStyleName;
		updateColors();
	}

    public void updateColors() {
    	List<IRule> rules = this.createRules();
    	setRules(rules.toArray(new IRule[0]));

    	int style = colorProvider.store.getInt(defaultStyleName);
    	IToken defaultToken = new Token(colorProvider.getTextAttribute(defaultColorName, style));
    	setDefaultReturnToken(defaultToken);
    }

    protected abstract List<IRule> createRules();
}
