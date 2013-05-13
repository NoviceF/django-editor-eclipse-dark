package org.kacprzak.eclipse.django_editor.preferences;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.SWT;
import org.kacprzak.eclipse.django_editor.DjangoActivator;
import org.kacprzak.eclipse.django_editor.IDjangoColorConstants;

/**
 * @author Zbigniew Kacprzak
*/
public class DjangoPreferenceInitializer extends AbstractPreferenceInitializer
{
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = DjangoActivator.getDefault().getPreferenceStore();

		setDefaults(store);
	}

	private static HashMap<String, String> defaultColors = new HashMap<String, String>();
	static {
		defaultColors.put(IDjangoPrefs.DJKEYWORD_COLOR, 	StringConverter.asString(IDjangoColorConstants.DJANGO_KEYWORD));
		defaultColors.put(IDjangoPrefs.DJUSRTAG_COLOR, 		StringConverter.asString(IDjangoColorConstants.DJANGO_USRTAG));
		defaultColors.put(IDjangoPrefs.DJDELIMITER_COLOR,	StringConverter.asString(IDjangoColorConstants.DJANGO_DELIMITER));
	    defaultColors.put(IDjangoPrefs.DJTAG_ATTR_COLOR, 	StringConverter.asString(IDjangoColorConstants.DJANGO_TAG_ATTR));
		defaultColors.put(IDjangoPrefs.DJCOMMENT_COLOR, 	StringConverter.asString(IDjangoColorConstants.DJANGO_COMMENT));
		defaultColors.put(IDjangoPrefs.DJSTRING_COLOR, 		StringConverter.asString(IDjangoColorConstants.DJANGO_STRING));
		defaultColors.put(IDjangoPrefs.DJFILTER_COLOR,		StringConverter.asString(IDjangoColorConstants.DJANGO_FILTER));
		defaultColors.put(IDjangoPrefs.DJUSRFILTER_COLOR,   StringConverter.asString(IDjangoColorConstants.DJANGO_USRFILTER));
		defaultColors.put(IDjangoPrefs.DJVARIABLE_COLOR, 	StringConverter.asString(IDjangoColorConstants.DJANGO_VARIABLE));

		defaultColors.put(IDjangoPrefs.DJKEYWORD_STYLE, 	StringConverter.asString(SWT.BOLD));
		defaultColors.put(IDjangoPrefs.DJUSRTAG_STYLE, 		StringConverter.asString(SWT.BOLD));
		defaultColors.put(IDjangoPrefs.DJDELIMITER_STYLE, 	StringConverter.asString(0));
	    defaultColors.put(IDjangoPrefs.DJTAG_ATTR_STYLE, 	StringConverter.asString(0));
		defaultColors.put(IDjangoPrefs.DJCOMMENT_STYLE, 	StringConverter.asString(SWT.ITALIC));
		defaultColors.put(IDjangoPrefs.DJSTRING_STYLE, 		StringConverter.asString(SWT.ITALIC));
		defaultColors.put(IDjangoPrefs.DJFILTER_STYLE, 		StringConverter.asString(SWT.BOLD));
		defaultColors.put(IDjangoPrefs.DJUSRFILTER_STYLE, 	StringConverter.asString(SWT.ITALIC));
		defaultColors.put(IDjangoPrefs.DJVARIABLE_STYLE, 	StringConverter.asString(SWT.BOLD));

		// HTML colors
		defaultColors.put(IDjangoPrefs.HTMLTAG_COLOR, 		StringConverter.asString(IDjangoColorConstants.HTML_TAG));
	    defaultColors.put(IDjangoPrefs.HTMLTAG_ATTR_COLOR, 	StringConverter.asString(IDjangoColorConstants.HTML_TAG_ATTR));
		defaultColors.put(IDjangoPrefs.HTMLCOMMENT_COLOR, 	StringConverter.asString(IDjangoColorConstants.HTML_COMMENT));
		defaultColors.put(IDjangoPrefs.HTMLSTRING_COLOR, 	StringConverter.asString(IDjangoColorConstants.HTML_STRING));
		defaultColors.put(IDjangoPrefs.HTMLDOCTYPE_COLOR, 	StringConverter.asString(IDjangoColorConstants.HTML_DOCTYPE));
		defaultColors.put(IDjangoPrefs.HTMLSCRIPTLET_COLOR, StringConverter.asString(IDjangoColorConstants.HTML_SCRIPTLET));
		defaultColors.put(IDjangoPrefs.HTMLSCRIPT_COLOR, 	StringConverter.asString(IDjangoColorConstants.HTML_SCRIPT));

		defaultColors.put(IDjangoPrefs.HTMLTAG_STYLE, 		StringConverter.asString(SWT.BOLD));
	    defaultColors.put(IDjangoPrefs.HTMLTAG_ATTR_STYLE, 	StringConverter.asString(SWT.ITALIC));
		defaultColors.put(IDjangoPrefs.HTMLCOMMENT_STYLE, 	StringConverter.asString(SWT.ITALIC));
		defaultColors.put(IDjangoPrefs.HTMLSTRING_STYLE, 	StringConverter.asString(SWT.ITALIC));
		defaultColors.put(IDjangoPrefs.HTMLDOCTYPE_STYLE, 	StringConverter.asString(0));
		defaultColors.put(IDjangoPrefs.HTMLSCRIPTLET_STYLE, StringConverter.asString(0));
		defaultColors.put(IDjangoPrefs.HTMLSCRIPT_STYLE, 	StringConverter.asString(SWT.BOLD));

		// CSS colors
		defaultColors.put(IDjangoPrefs.CSSSELECTOR_COLOR, 	StringConverter.asString(IDjangoColorConstants.CSS_SELECTOR));
		defaultColors.put(IDjangoPrefs.CSSPROP_COLOR, 		StringConverter.asString(IDjangoColorConstants.CSS_PROP));
		defaultColors.put(IDjangoPrefs.CSSCOMMENT_COLOR, 	StringConverter.asString(IDjangoColorConstants.CSS_COMMENT));
		defaultColors.put(IDjangoPrefs.CSSVALUE_COLOR, 		StringConverter.asString(IDjangoColorConstants.CSS_VALUE));

		defaultColors.put(IDjangoPrefs.CSSSELECTOR_STYLE, 	StringConverter.asString(SWT.BOLD));
		defaultColors.put(IDjangoPrefs.CSSPROP_STYLE, 		StringConverter.asString(0));
		defaultColors.put(IDjangoPrefs.CSSCOMMENT_STYLE, 	StringConverter.asString(SWT.ITALIC));
		defaultColors.put(IDjangoPrefs.CSSVALUE_STYLE, 		StringConverter.asString(0));

		// default colors
		defaultColors.put(IDjangoPrefs.DEFAULT_FG_COLOR, 	StringConverter.asString(IDjangoColorConstants.DEFAULT_FOREGROUND));
		defaultColors.put(IDjangoPrefs.DEFAULT_FG_STYLE, 	StringConverter.asString(0));

		// JavaScript colors
		defaultColors.put(IDjangoPrefs.JSSTRING_COLOR, 		StringConverter.asString(IDjangoColorConstants.JS_STRING));
		defaultColors.put(IDjangoPrefs.JSKEYWORD_COLOR, 	StringConverter.asString(IDjangoColorConstants.JS_KEYWORD));
		defaultColors.put(IDjangoPrefs.JSCOMMENT_COLOR, 	StringConverter.asString(IDjangoColorConstants.JS_COMMENT));
		defaultColors.put(IDjangoPrefs.JSNUMBER_COLOR, 		StringConverter.asString(IDjangoColorConstants.JS_NUMBER));

		defaultColors.put(IDjangoPrefs.JSSTRING_STYLE, 		StringConverter.asString(SWT.ITALIC));
		defaultColors.put(IDjangoPrefs.JSKEYWORD_STYLE, 	StringConverter.asString(0));
		defaultColors.put(IDjangoPrefs.JSCOMMENT_STYLE, 	StringConverter.asString(SWT.ITALIC));
		defaultColors.put(IDjangoPrefs.JSNUMBER_STYLE, 		StringConverter.asString(0));
	}

	public static void setDefaults(IPreferenceStore store) {
		Iterator<String> it = defaultColors.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String val = defaultColors.get(key);
			store.setDefault(key, val);
		}
	}

	public static void setToDefaults(IPreferenceStore store) {
		Iterator<String> it = defaultColors.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String val = defaultColors.get(key);
			store.setValue(key, val);
		}
	}
}
