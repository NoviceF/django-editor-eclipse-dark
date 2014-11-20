package org.kacprzak.eclipse.django_editor.editors.completion;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.jface.text.templates.TemplateException;
import org.eclipse.jface.text.templates.TemplateTranslator;
import org.eclipse.jface.text.templates.TemplateVariable;
import org.eclipse.swt.graphics.Image;
import org.kacprzak.eclipse.django_editor.DjangoPlugin;
import org.kacprzak.eclipse.django_editor.IDjangoImages;
import org.kacprzak.eclipse.django_editor.templates.DjangoContextType;
import org.kacprzak.eclipse.django_editor.templates.TemplateManager;

public abstract class DjangoAbsCompletionProcessor implements IContentAssistProcessor {

	String contextTypeId = DjangoContextType.DJANGO_CONTEXT_TYPE_TAG;
	String contextImageId = IDjangoImages.TAG_IMAGE;
	String prefixChar = "";
	boolean alternateTemplate = false;

	protected boolean inActivationCharacters(char ch) {
		char[] chArr = getCompletionProposalAutoActivationCharacters();
		if (chArr == null)
			return false;
		for (char aCh : chArr) {
			if (aCh == ch)
				return true;
		}
		return false;
	}
	
	protected void setDefaultContextAndImage() {
		contextTypeId = DjangoContextType.DJANGO_CONTEXT_TYPE_TAG;
		contextImageId = IDjangoImages.TAG_IMAGE;
		alternateTemplate = false;
	};

	protected abstract boolean setAlternateContextAndImage(char ch);
	
	protected boolean shouldEatActivationCharacter(char ch) {
		return true;
	}
	
	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		setDefaultContextAndImage();
		prefixChar = "";
		
		IDocument doc = viewer.getDocument();
//		Point selectedRange = viewer.getSelectedRange();
		
		String prefix = getTokenName(doc, offset);
		if (prefix.length() > 0) {
			char ch = prefix.charAt(0);
			if (inActivationCharacters(ch)) {
				if (shouldEatActivationCharacter(ch))
					prefixChar = "" + ch;
				prefix = prefix.substring(1);
			}
			alternateTemplate = setAlternateContextAndImage(ch);
		}
		int loff = countLineOffset(doc, offset) - prefix.length() - prefixChar.length();
		
//System.out.println("PF=" + prefix + " PC=" + prefixChar + " CT=" + contextTypeId);
		List<ICompletionProposal> propList = new ArrayList<ICompletionProposal>();
		computeProposals(prefix, offset, loff, propList);
		
		ICompletionProposal[] proposals = new ICompletionProposal[propList.size()];
		propList.toArray(proposals);
		return proposals; 
	}

//	protected String getTokenName(IDocument doc, int documentOffset) {
//		StringBuffer buf = new StringBuffer(); 				// Use string buffer to collect characters
//		while (true) {
//			try {
//				char c = doc.getChar(--documentOffset); 	// Read character backwards
//				if (c == '>' || c == '}' || Character.isWhitespace(c)) 	// This was not the start of a tag
//					return "";
//				
//				buf.append(c);				// Collect character
//				
//				// Start of tag. Return qualifier
//				if (inActivationCharacters(c))//if (c == '<' || c == '{')
//					return buf.reverse().toString();
//			} catch (BadLocationException e) {
//				return ""; // Document start reached, no tag found
//			}
//		}
//	}
	
	protected String getTokenName(IDocument document, int offset) {
		int i= offset;
		if (i > document.getLength())
			return ""; //$NON-NLS-1$

		try {
			while (i > 0) {
				char ch= document.getChar(i - 1);
				if (!Character.isJavaIdentifierPart(ch) && !inActivationCharacters(ch))
					break;
				i--;
			}

			return document.get(i, offset - i);
		} catch (BadLocationException e) {
			return ""; //$NON-NLS-1$
		}
	}
	
	private int countLineOffset(IDocument doc, int documentOffset) {
		int lineOffset = documentOffset;
		char ch = 0;
		while (lineOffset >= 0) {
			try {
				ch = doc.getChar(--lineOffset); 	// Read character backwards
				if (ch == '\n')
					break;
			} catch (BadLocationException e) {
				return 0;
			}
		}
		return documentOffset-lineOffset-prefixChar.length();
	}
	
	protected void computeProposals(String qualifier, int documentOffset, int lineOffset, List<ICompletionProposal> propList) { 
		int replacementLength = qualifier.length() + prefixChar.length();

		String sp = "";
		for (int ds=0; ds < lineOffset; ds++, sp += " ");
		
		// Loop through all templates/proposals
		for (Template tpl: getTemplates()) {
		    if (tpl.getName().startsWith(qualifier)) {		    	
		        propList.add(createProposal(tpl, documentOffset, replacementLength, sp));
		    }
		}
	}
	
	protected ICompletionProposal createProposal(Template template, int documentOffset, int replacementLength, String sp) {
		String tplName = template.getName();
		String tplDescr = template.getDescription();
		String tplPattern = template.getPattern();

    	String out = "";
    	String[] spl = tplPattern.split("(\r\n|\r|\n)");
    	for (String prt : spl) {
    		out += prt + (spl.length>1 ? "\n"+sp : "");
    	}
    	tplPattern = out;
    	
        int cursorPosition = -1;

        TemplateTranslator translator= new TemplateTranslator();
		try {
			TemplateBuffer buffer= translator.translate(tplPattern);
			for (TemplateVariable tVar : buffer.getVariables()) {
				if (tVar.getName().equals("cursor")) {
					cursorPosition = tVar.getOffsets()[0];
				}
			}
			tplPattern = buffer.getString().replaceAll("cursor", "");
		} catch (TemplateException e) {}
		if (cursorPosition < 0)
			cursorPosition = tplPattern.length();
    	
        IContextInformation contextInfo = new ContextInformation(tplDescr, tplName + " Tag");

        DjangoExtCompletionProposal proposal = new 
        		DjangoExtCompletionProposal(tplPattern, 
		        				   documentOffset - replacementLength, 
		        				   replacementLength, 
		        				   cursorPosition, 
		        				   getImage(template), 
		        				   tplName, tplDescr, 
		        				   contextInfo,//IContextInformation contextInformation, 
		        				   "String additionalProposalInfo"//String additionalProposalInfo)
        						  );
        return proposal;
	}
	
	protected Template[] getTemplates() {
		return TemplateManager.getDjangoTemplateStore().getTemplates(contextTypeId);
	}
	
	protected Image getImage(Template template) {
		ImageRegistry registry = DjangoPlugin.getDefault().getImageRegistry();
		Image image = registry.get(contextImageId);
		return image;
	}
	
	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		return null;
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

}
