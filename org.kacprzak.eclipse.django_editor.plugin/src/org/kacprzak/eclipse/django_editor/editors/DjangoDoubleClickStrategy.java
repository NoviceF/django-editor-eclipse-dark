package org.kacprzak.eclipse.django_editor.editors;

import org.eclipse.jface.text.*;

public class DjangoDoubleClickStrategy implements ITextDoubleClickStrategy {
    protected ITextViewer fText;

    public void doubleClicked(ITextViewer part) {
        int pos = part.getSelectedRange().x;

        if (pos < 0)
            return;

        fText = part;

        if (selectComment(pos)) return;
        if (selectBlock(pos)) return;
        if (selectWord(pos)) return;
    }
    
    protected boolean selectComment(int caretPos) {
        // comment starting with {# and ending with #} within one line
        IDocument doc = fText.getDocument();
        int startPos, endPos;

        try {
            int pos = caretPos;
            char c = ' ';

            boolean started = false;
            while (pos >= 0) {
                c = doc.getChar(pos);
                if (c == '#')
                    started = true;
                else if (c == '{' && started) {
                    --pos;
                    break;
                } else
                    started = false;
                if (c == Character.LINE_SEPARATOR)
                    break;
                --pos;
            }

            if (!started)
                return false;

            startPos = pos;

            pos = caretPos;
            int length = doc.getLength();
            c = ' ';

            started = false;
            while (pos < length) {
                c = doc.getChar(pos);
                if (c == '#')
                    started = true;
                else if (c == '}' && started) {
                    ++pos;
                    break;
                } else
                    started = false;
                if (c == Character.LINE_SEPARATOR)
                    break;
                ++pos;
            }
            if (!started)
                return false;

            endPos = pos;

            int offset = startPos + 1;
            int len = endPos - offset;
            fText.setSelectedRange(offset, len);
            return true;
        } catch (BadLocationException x) {
        }

        return false;
    }
    
    protected boolean selectBlock(int caretPos) {
        // TODO: implement Django block selection
        return false;
    }
    
    protected boolean selectWord(int caretPos) {

        IDocument doc = fText.getDocument();
        int startPos, endPos;

        try {
            int pos = caretPos;
            char c;

            while (pos >= 0) {
                c = doc.getChar(pos);
                if (!Character.isJavaIdentifierPart(c))
                    break;
                --pos;
            }

            startPos = pos;

            pos = caretPos;
            int length = doc.getLength();

            while (pos < length) {
                c = doc.getChar(pos);
                if (!Character.isJavaIdentifierPart(c))
                    break;
                ++pos;
            }

            endPos = pos;
            selectRange(startPos, endPos);
            return true;

        } catch (BadLocationException x) {
        }

        return false;
    }

    private void selectRange(int startPos, int stopPos) {
        int offset = startPos + 1;
        int length = stopPos - offset;
        fText.setSelectedRange(offset, length);
    }
}