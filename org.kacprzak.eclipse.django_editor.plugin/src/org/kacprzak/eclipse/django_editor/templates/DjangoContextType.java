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

	public DjangoContextType(){
		addResolver(new GlobalTemplateVariables.Cursor());
		addResolver(new GlobalTemplateVariables.WordSelection());
		addResolver(new GlobalTemplateVariables.LineSelection());
	}
}
