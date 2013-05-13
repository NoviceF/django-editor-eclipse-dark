package org.kacprzak.eclipse.django_editor.editors;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateCompletionProcessor;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.swt.graphics.Image;
import org.kacprzak.eclipse.django_editor.templates.DjangoContextType;
import org.kacprzak.eclipse.django_editor.templates.TemplateManager;

/**
 * Django tags completion processor
 * @author Zbigniew Kacprzak
*/
public class DjangoTagCompletionProcessor extends TemplateCompletionProcessor implements IContentAssistProcessor
{

	@Override
	protected TemplateContextType getContextType(ITextViewer viewer, IRegion region) {
		String id = DjangoContextType.DJANGO_CONTEXT_TYPE_TAG;
		return TemplateManager.getDjangoContextTypeRegistry().getContextType(id);
	}

	@Override
	protected Image getImage(Template template) {
		return null;
	}

	@Override
	protected Template[] getTemplates(String contextTypeId) {
		return TemplateManager.getDjangoTemplateStore().getTemplates();
	}

}
