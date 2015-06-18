package org.kacprzak.eclipse.django_editor.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;
import org.kacprzak.eclipse.django_editor.editors.DjangoEditor;

public class DjangoToggleLineCommentHandler extends AbstractHandler {

    static final String OPEN_COMMENT = "{#"; //$NON-NLS-1$
    static final String CLOSE_COMMENT = "#}"; //$NON-NLS-1$

    @Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
//		System.out.println("DjangoToggleLineCommentHandler.execute fired");
		
		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		if (!(page.getActiveEditor() instanceof DjangoEditor))
			return null;
		
		DjangoEditor editor = (DjangoEditor) page.getActiveEditor();
		IEditorInput editorInput = editor.getEditorInput();
		IDocument document = editor.getDocumentProvider().getDocument(editorInput);
		
	    ISelection pSelection = page.getSelection();
	    if (! (pSelection != null && pSelection instanceof TextSelection))
	    	return null;
	    
	    TextSelection selection = (TextSelection) pSelection;

	    StringBuffer buffer = new StringBuffer();
	    buffer.append("DjangoToggleLineCommentHandler; selection: " + selection.getLength());
	    buffer.append("\nDjangoToggleLineCommentHandler"
    					 + "\n offset: "+ selection.getOffset()
    					 + "\n startLine: "+ selection.getStartLine()
    					 + "\n endLine: "+ selection.getEndLine()
    					 + "\n text: "+ selection.getText()
    					  );
    	System.out.println(buffer);

        boolean allLinesCommented = true;
        for (int i=selection.getStartLine(); i <= selection.getEndLine(); i++) {
            try {
                if (document.getLineLength(i) > 0) {
                    if (!isCommentLine(document, i)) {
                        allLinesCommented = false;
                        break;
                    }
                }
            } catch (BadLocationException e) {
            	e.printStackTrace();
            }
        }
    	
        int aSelectionStart = selection.getOffset();
        int aSelectionEnd = aSelectionStart + selection.getLength();
        boolean aOpPerf = false;
        
        int aStartLine = selection.getStartLine();
        int aEndLine = selection.getEndLine();
    	for (int line=aStartLine; line <= aEndLine; line++) {
    		try {
				IRegion lineRegion = document.getLineInformation(line);
//				System.out.println("Line: " + line + "; offset: " + lineRegion.getOffset() + ", length: " + lineRegion.getLength());
				if (line == aStartLine)
					aSelectionStart = lineRegion.getOffset();
				if (line == aEndLine) {
					int aEndMod = allLinesCommented ? -4 : +4;
					aSelectionEnd = lineRegion.getOffset() + lineRegion.getLength() + aEndMod;
				}
				
				// don't toggle empty lines
				String content = document.get(lineRegion.getOffset(), lineRegion.getLength());
				if (content.trim().length() == 0) {
//					System.out.println("Empty Line " + line + "; skipped");
					continue;
				}
				
				if (allLinesCommented) {
					removeComment(document, lineRegion);
				} else {
					addComment(document, lineRegion);
				}
				aOpPerf = true;

			} catch (BadLocationException e) {
				e.printStackTrace();
			} 
    	}
    	if (!selection.isEmpty() && aOpPerf) {   		
//    		editor.setHighlightRange(aSelectionStart, aSelectionEnd-aSelectionStart, true);
    		editor.selectAndReveal(aSelectionStart, aSelectionEnd-aSelectionStart);
    	}	    
        return null;
	}

    private void removeComment(IDocument document, IRegion lineRegion) {
    	int offset = lineRegion.getOffset();
        int len = lineRegion.getLength();
        //System.out.println("  - removing comment; offset: " + offset + ", length: " + len);    

		try {
			String content = document.get(offset, len);
			int innerOffset = content.indexOf(OPEN_COMMENT);
			if (innerOffset >= 0)
				offset += innerOffset;
			document.replace(offset, 2, "");
			
			offset -= 2;
			innerOffset = content.indexOf(CLOSE_COMMENT);
			if (innerOffset >= 0) {
				offset += innerOffset;
				document.replace(offset, 2, "");
			}
		} catch (BadLocationException e) {} //$NON-NLS-1$
    }
     
    private void addComment(IDocument document, IRegion lineRegion) {
    	int offset = lineRegion.getOffset();
        int len = lineRegion.getLength();
        try {
        	String string = document.get(lineRegion.getOffset(), lineRegion.getLength());
        	if (!string.startsWith(OPEN_COMMENT)) {
        		document.replace(offset, 0, OPEN_COMMENT);// + " ");
        		len += 2;//3;
        	}
			if (!string.trim().endsWith(CLOSE_COMMENT))
				document.replace(offset + len, 0, CLOSE_COMMENT);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
    }
    
    private boolean isCommentLine(IDocument document, int line)
    {
        boolean isComment = false;

        try {
            IRegion region = document.getLineInformation(line);
            String string = document.get(region.getOffset(), region.getLength()).trim();

            // empty line
            if (string.length() == 0)
                return true;

            isComment = string.startsWith(OPEN_COMMENT) && string.endsWith(CLOSE_COMMENT);

        } catch (BadLocationException e) {
        	e.printStackTrace();
        }
        return isComment;
    }

}
