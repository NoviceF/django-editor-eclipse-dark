package org.kacprzak.eclipse.django_editor.templates;

import org.eclipse.jface.text.templates.GlobalTemplateVariables;
import org.eclipse.jface.text.templates.TemplateContextType;


/**
 * @author Zbigniew Kacprzak
*/
public class DjangoContextType extends TemplateContextType {

	//"org.kacprzak.eclipse.django_editor.templates.context";
	public static final String DJANGO_CONTEXT_TYPE_TAG = 
			"org.kacprzak.eclipse.django_editor.templates.contextType.django_tag";
	public static final String DJANGO_CONTEXT_TYPE_FILTER = 
			"org.kacprzak.eclipse.django_editor.templates.contextType.django_filter";
	public static final String HTML_CONTEXT_TYPE_TAG = 
			"org.kacprzak.eclipse.django_editor.templates.contextType.html_tag";
	public static final String JS_CONTEXT_TYPE_TAG = 
			"org.kacprzak.eclipse.django_editor.templates.contextType.javascript_name";
	public static final String CSS_CONTEXT_TYPE_TAG = 
			"org.kacprzak.eclipse.django_editor.templates.contextType.css_name";

	public DjangoContextType(){
		addResolver(new GlobalTemplateVariables.Cursor());
		addResolver(new GlobalTemplateVariables.WordSelection());
		addResolver(new GlobalTemplateVariables.LineSelection());
	}
}
