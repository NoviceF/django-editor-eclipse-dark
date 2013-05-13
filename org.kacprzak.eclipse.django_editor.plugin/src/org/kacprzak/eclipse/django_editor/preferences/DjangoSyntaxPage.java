package org.kacprzak.eclipse.django_editor.preferences;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.kacprzak.eclipse.django_editor.DjangoActivator;

/**
 * Old solution...
 * @author Zbigniew Kacprzak
*/
public class DjangoSyntaxPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage  {

	public DjangoSyntaxPage() {
		super(FLAT);
	}

	@Override
	public void init(IWorkbench workbench) {
		setDescription("\nComing soon...");

		IPreferenceStore store = DjangoActivator.getDefault().getPreferenceStore();
		setPreferenceStore(store);
	}

	@Override
	protected void createFieldEditors() {
		Composite parent = getFieldEditorParent();
/*
		addField(new ColorFieldEditor(IDjangoPrefs.DEFAULT_FG_COLOR, "Default Foreground", parent));

		addField(new ColorFieldEditor(IDjangoPrefs.DJKEYWORD_COLOR, "Django Tag", parent));
		addField(new ColorFieldEditor(IDjangoPrefs.DJUSRTAG_COLOR, "Django Custom Tag", parent));
		addField(new ColorFieldEditor(IDjangoPrefs.DJVARIABLE_COLOR, "Django Variable", parent));
		addField(new ColorFieldEditor(IDjangoPrefs.DJSTRING_COLOR, "Django String", parent));
		addField(new ColorFieldEditor(IDjangoPrefs.DJCOMMENT_COLOR, "Django Comment", parent));
		addField(new ColorFieldEditor(IDjangoPrefs.DJFILTER_COLOR, "Django Filter", parent));
		addField(new ColorFieldEditor(IDjangoPrefs.DJUSRFILTER_COLOR, "Django Custom Filter", parent));
		addField(new ColorFieldEditor(IDjangoPrefs.DJDELIMITER_COLOR, "Django Delimiter {{, {%", parent));
		//addField(new ColorFieldEditor(IDjangoPrefs.DJTAG_ATTR_COLOR, "Django Tag Attribute", parent));

		addField(new ColorFieldEditor(IDjangoPrefs.HTMLTAG_COLOR, "HTML Tag", parent));
		//addField(new ColorFieldEditor(IDjangoPrefs.HTMLTAG_ATTR_COLOR, "HTML Attribute Name", parent));
		addField(new ColorFieldEditor(IDjangoPrefs.HTMLCOMMENT_COLOR, "HTML Comment", parent));
		addField(new ColorFieldEditor(IDjangoPrefs.HTMLSTRING_COLOR, "HTML String", parent));
		addField(new ColorFieldEditor(IDjangoPrefs.HTMLDOCTYPE_COLOR, "DOCTYPE Color", parent));
		addField(new ColorFieldEditor(IDjangoPrefs.HTMLSCRIPTLET_COLOR, "HTML <% .. %>", parent));
		addField(new ColorFieldEditor(IDjangoPrefs.HTMLSCRIPT_COLOR, "HTML <script>", parent));

		addField(new ColorFieldEditor(IDjangoPrefs.JSKEYWORD_COLOR, "JavaScript Keyword", parent));
		addField(new ColorFieldEditor(IDjangoPrefs.JSSTRING_COLOR, "JavaScript String", parent));
		addField(new ColorFieldEditor(IDjangoPrefs.JSCOMMENT_COLOR, "JavaScript Comment", parent));

		addField(new ColorFieldEditor(IDjangoPrefs.CSSSELECTOR_COLOR, "CSS Selector", parent));
		addField(new ColorFieldEditor(IDjangoPrefs.CSSPROP_COLOR, "CSS Property", parent));
		addField(new ColorFieldEditor(IDjangoPrefs.CSSVALUE_COLOR, "CSS Value", parent));
		addField(new ColorFieldEditor(IDjangoPrefs.CSSCOMMENT_COLOR, "CSS Comments", parent));
*/
	}

}
