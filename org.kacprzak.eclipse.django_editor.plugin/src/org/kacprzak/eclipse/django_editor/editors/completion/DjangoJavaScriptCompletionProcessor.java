package org.kacprzak.eclipse.django_editor.editors.completion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.text.templates.Template;
import org.kacprzak.eclipse.django_editor.IDjangoImages;
import org.kacprzak.eclipse.django_editor.editors.js.IJQuerySyntax;
import org.kacprzak.eclipse.django_editor.templates.DjangoContextType;
import org.kacprzak.eclipse.django_editor.templates.TemplateManager;

public class DjangoJavaScriptCompletionProcessor extends DjangoAbsCompletionProcessor {

	private List<Template> templates = null;
	
	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] {'.', '{'};
	}

	@Override
	protected boolean shouldEatActivationCharacter(char ch) {
		if (ch == '.')
			return false;
		return true;
	}
	
	@Override
	protected boolean setAlternateContextAndImage(char ch) {
		if (ch == '.') {
			contextTypeId = DjangoContextType.JS_CONTEXT_TYPE_TAG;
			contextImageId = IDjangoImages.OUTL_JQUERY_IMAGE;
			return true;
		}
		return false;
	}
	
//	@Override
//	protected void computeProposals(String qualifier, int documentOffset, int lineOffset, List<ICompletionProposal> propList) {
//		// public Template(String name, String description, String contextTypeId, String pattern, boolean isAutoInsertable)
//	}
	
	class TemplateComparator implements Comparator<Template> {

	    public int compare(Template tpl1, Template tpl2) {
	    	String t1 = tpl1.getName();
	    	String t2 = tpl2.getName();
	    	if (t1.startsWith("$") && !t2.startsWith("$"))
	    		return 1;
	    	if (!t1.startsWith("$") && t2.startsWith("$"))
	    		return -1;
	    	return t1.compareTo(t2);
	    }
	}
	
	protected Template[] getTemplates() {
		if (! alternateTemplate)
			return TemplateManager.getDjangoTemplateStore().getTemplates(contextTypeId);
		
		if (templates != null)
			return templates.toArray(new Template[0]); 
	
		templates = new ArrayList<Template>();
		addTemplates(IJQuerySyntax.EVENTS, "Event");
		addTemplates(IJQuerySyntax.EFFECTS, "Effect");
		addTemplates(IJQuerySyntax.HTML, "HTML Helper");
		addTemplates(IJQuerySyntax.TRAVERSING, "Traversing");
		addTemplates(IJQuerySyntax.AJAX, "Ajax");
		addTemplates(IJQuerySyntax.MISC, "Misc");
		addTemplates(IJQuerySyntax.GLOBAL, "Misc");
		
		Collections.sort(templates, new TemplateComparator());
		return templates.toArray(new Template[0]); 
	}

	private void addTemplates(String[] words, String descr) {
		for (String word : words) {
			templates.add(creteTemplate(word, descr));
		}
	}
	
	private Template creteTemplate(String word, String descr) {
		return new Template(word, "jQuery " + descr, contextTypeId, word + "(${cursor})", false);		
	}
}
