package org.kacprzak.eclipse.django_editor.editors.outline;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;

class DjLabelProvider extends StyledCellLabelProvider {
    @Override
    public void update(ViewerCell cell) {
    	DjDocTag element = (DjDocTag) cell.getElement();
        StyledString text = new StyledString();
        cell.setImage(element.image);
        
        text.append(element.keyword, StyledString.COUNTER_STYLER);
        text.append("  [" + element.description + "]", StyledString.QUALIFIER_STYLER);
        text.append(" - line " + element.lineNumber, StyledString.DECORATIONS_STYLER);
        text.append(" (" + element.conType + ")", StyledString.DECORATIONS_STYLER);
        cell.setText(text.toString());
//        cell.setStyleRanges(text.getStyleRanges());
        super.update(cell);
    }
}

/*
class DjLabelProvider extends LabelProvider {
	public DjLabelProvider() {
		super();
	}
	public Image getImage(Object element) {
		return ((DjDocTag) element).image;
	}
	public String getText(Object element) {
		return element == null ? "" : element.toString();//$NON-NLS-1$
	}
    public boolean isLabelProperty(Object element, String property) {
        return true;
    }
}
*/