package org.kacprzak.eclipse.django_editor.editors.completion;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.kacprzak.eclipse.django_editor.DjangoActivator;
import org.kacprzak.eclipse.django_editor.DjangoEditorResources;
import org.kacprzak.eclipse.django_editor.IDjangoPartitions;
import org.kacprzak.eclipse.django_editor.editors.dj.IDjangoSyntax;
import org.kacprzak.eclipse.django_editor.editors.dj.WordPartDetector;

/**
 * Django specific completion processor
 * @author Zbigniew Kacprzak
*/
public class DjangoCompletionProcessor implements IContentAssistProcessor {
	private String partition = IDjangoPartitions.DJANGO_DEFAULT;

	public DjangoCompletionProcessor(String partitionName) {
		this.partition = partitionName;
	}
	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int documentOffset)
	{
//		ICompletionProposal[] result = new ICompletionProposal[1];
		List<CompletionProposal> proposalsList = new ArrayList<CompletionProposal>();

		WordPartDetector wordPart = new WordPartDetector(viewer, documentOffset);

		if (this.partition.equals(IDjangoPartitions.DJANGO_TAG)) {
			// iterate over tags
			for (String key: IDjangoSyntax.TAGS) {
				if (key.toLowerCase().startsWith(wordPart.getString().toLowerCase()))
					proposalsList.add(createCompletionProposal(key, wordPart, "TAG"));
			}
		}
		if (this.partition.equals(IDjangoPartitions.DJANGO_VARIABLE)) {
			// iterate over filters
			for (String key: IDjangoSyntax.FILTERS) {
				if (key.toLowerCase().startsWith(wordPart.getString().toLowerCase()))
					proposalsList.add(createCompletionProposal(key, wordPart, "FILTER"));
			}
			// iterate over forloop
			for (String key: IDjangoSyntax.FORLOOP) {
				if (key.toLowerCase().startsWith(wordPart.getString().toLowerCase()))
					proposalsList.add(createCompletionProposal(key, wordPart, "FORLOOP"));
			}
		}
		return proposalsList.toArray(new ICompletionProposal[0]);
	}
	private CompletionProposal createCompletionProposal(String keyWord, WordPartDetector wordPart, String wordType)
	{
		IContextInformation info =
			new ContextInformation(keyWord, getContentInfoString(keyWord));

		// TODO: get descriptions from Django web site
		String descr = "Django template tag";
		if (wordType.equals("FILTER"))
			descr = "Django template filter";
		else if (wordType.equals("FORLOOP"))
			descr = "Django loop variable";
		String dispStr = String.format("%s   - %s", keyWord, descr);
		return
			new CompletionProposal(keyWord, 	//replacementString
					wordPart.getOffset(),		//replacementOffset the offset of the text to be replaced
					wordPart.getString().length(), 	//replacementLength the length of the text to be replaced
					keyWord.length(), 			//cursorPosition the position of the cursor following the insert relative to replacementOffset
					null,						//image to display
					dispStr, 					//displayString the string to be displayed for the proposal
					info,						//contntentInformation the context information associated with this proposal
					"Additional information..."//getContentInfoString(keyWord)
			);
	}

	/**
	 * Method getContentInfoString.
	 * @param keyWord
	 */
	private String getContentInfoString(String keyWord) {
		String resourceString;
		String resourceKey = "ContextString." + keyWord;
		resourceString = DjangoEditorResources.getString(resourceKey);
		if (resourceString == keyWord) {
			resourceString = "No Context Info String";
		}
		return resourceString;
	}


	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return null;//new char[]{ '{', '%', '|'};
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

}
