package org.kacprzak.eclipse.django_editor.editors.completion;


public class DjangoCssCompletionProcessor extends DjangoAbsCompletionProcessor {

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] {'{'};
	}

	protected boolean setAlternateContextAndImage(char ch) {
//		if (ch == '#') {
//			contextTypeId = DjangoContextType.HTML_CONTEXT_TYPE_TAG;
//			contextImageId = IDjangoImages.HTML_TAG_IMAGE;
//		}
		return false;
	}

//	protected boolean shouldEatActivationCharacter(char ch) {
//		return true;
//	}
//	
	
//	@Override
//	protected void computeProposals(String qualifier, int documentOffset, int lineOffset, List<ICompletionProposal> propList) {
//		// public Template(String name, String description, String contextTypeId, String pattern, boolean isAutoInsertable)
//	}

}
