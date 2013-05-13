package org.kacprzak.eclipse.django_editor.editors;

import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;

/**
 * Rule scanner with colors scheme specified
 * @author Zbigniew Kacprzak
*/
public class DjangoColoredScanner extends RuleBasedScanner {
    protected ColorProvider colorProvider;
    private String colorName;
    private String styleName;

    public DjangoColoredScanner(ColorProvider colorProvider, String colorName, String styleName) {
        super();
        this.colorProvider = colorProvider;
        this.colorName = colorName;
        this.styleName = styleName;
        updateColors();
    }

    public void updateColors() {
    	int style = colorProvider.store.getInt(styleName);
        setDefaultReturnToken(new Token(colorProvider.getTextAttribute(colorName, style)));
    }


}
