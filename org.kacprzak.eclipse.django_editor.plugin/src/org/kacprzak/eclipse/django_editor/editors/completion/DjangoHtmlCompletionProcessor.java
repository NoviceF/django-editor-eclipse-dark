package org.kacprzak.eclipse.django_editor.editors.completion;

import org.kacprzak.eclipse.django_editor.IDjangoImages;
import org.kacprzak.eclipse.django_editor.templates.DjangoContextType;

public class DjangoHtmlCompletionProcessor extends DjangoAbsCompletionProcessor {

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] {'<', '{', '|'};
	}

	protected boolean setAlternateContextAndImage(char ch) {
		if (ch == '<') {
			contextTypeId = DjangoContextType.HTML_CONTEXT_TYPE_TAG;
			contextImageId = IDjangoImages.HTML_TAG_IMAGE;
			return true;
		}
		if (ch == '|') {
			contextTypeId = DjangoContextType.DJANGO_CONTEXT_TYPE_FILTER;
			contextImageId = IDjangoImages.FILTER_IMAGE;
			return true;
		}
		return false;
	}
}
