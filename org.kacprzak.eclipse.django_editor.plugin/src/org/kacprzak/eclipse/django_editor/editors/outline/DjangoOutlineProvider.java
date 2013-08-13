/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.kacprzak.eclipse.django_editor.editors.outline;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.kacprzak.eclipse.django_editor.DjangoPlugin;
import org.kacprzak.eclipse.django_editor.IDjangoImages;
import org.kacprzak.eclipse.django_editor.IDjangoPartitions;
import org.kacprzak.eclipse.django_editor.editors.dj.IDjangoSyntax;
import org.kacprzak.eclipse.django_editor.editors.outline.OLD_CopyOfDjangoContentOutlinePage.Segment;

/**
 */
class DjangoOutlineProvider implements ITreeContentProvider {
	
	private final static String SEGMENTS = "__django_editor_segments"; //$NON-NLS-1$
	private IPositionUpdater fPositionUpdater = new DefaultPositionUpdater(SEGMENTS);
	
	private IDocumentProvider fDocumentProvider;
	private Object fInput;
	
	private SegmentModel rootElement = null;
	private IEditorInput editorInput;
	
	private List<SegmentModel> fContent = new ArrayList<SegmentModel>();

	public DjangoOutlineProvider(IDocumentProvider provider, Object iInput)
	{
		super();
		fDocumentProvider = provider;
		fInput = iInput;
	}
	
	/*
	 * @see ITreeContentProvider#getChildren(Object)
	 */
	public Object[] getChildren(Object element) {
		if (element == fInput)
			return fContent.toArray();
		return new Object[0];
//		if (element == editorInput) {
//			if (rootElement == null) return new Object[0];
//			return getElements(element);
//		} else {
//			return getElements(element);
//		}
//		//return new Object[0];
	}

	/*
	 * @see ITreeContentProvider#getParent(Object)
	 */
	public Object getParent(Object element) {
		if (element instanceof SegmentModel)
			return fInput;
		return null;
//		if( element == null)
//			return null;
//		
//		if (element instanceof SegmentModel)
//			return ((SegmentModel)element).parent;
//		return null;
	}

	/*
	 * @see ITreeContentProvider#hasChildren(Object)
	 */
	public boolean hasChildren(Object element) {
		return element == fInput;
//		if (element == editorInput) 
//			return true;
//		return ((SegmentModel)element).children.size() > 0;
	}

	/*
	 * @see IStructuredContentProvider#getElements(Object)
	 */
	public Object[] getElements(Object element) {
		return fContent.toArray();
//		if (element instanceof SegmentModel)
//			return ((SegmentModel)element).children.toArray();
//		return new Object[0];
	}

	/*
	 * @see IContentProvider#inputChanged(Viewer, Object, Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (oldInput != null) {
			IDocument document= fDocumentProvider.getDocument(oldInput);
			if (document != null) {
				try {
					document.removePositionCategory(SEGMENTS);
				} catch (BadPositionCategoryException x) {}
				document.removePositionUpdater(fPositionUpdater);
			}
		}

		fContent.clear();

		if (newInput != null) {
			IDocument document= fDocumentProvider.getDocument(newInput);
			if (document != null) {
				document.addPositionCategory(SEGMENTS);
				document.addPositionUpdater(fPositionUpdater);
				parseDocument(document);
			}
		}
//		if (oldInput != null) {
//			IDocument document = fDocumentProvider.getDocument(oldInput);
//			if (document != null) {
//				try {
//					document.removePositionCategory(SEGMENTS);
//				} catch (BadPositionCategoryException x) { }
//				document.removePositionUpdater(fPositionUpdater);
//			}
//		}
//
//		fContent.clear();
//
//		if (newInput != null) {
//			IDocument document = fDocumentProvider.getDocument(newInput);
//			if (document != null) {
//				//document.addPositionCategory(SEGMENTS);
//				//document.addPositionUpdater(fPositionUpdater);
//				parseDocument(document);
//			}
//		}
	}

	/*
	 * @see IContentProvider#dispose
	 */
	public void dispose() {
		if (fContent != null) {
			fContent.clear();
			fContent= null;
		}
	}

	/*
	 * @see IContentProvider#isDeleted(Object)
	 */
	public boolean isDeleted(Object element) {
		return false;
	}

	private void parseDocument(IDocument document) {

		String[] docPosCats = document.getPositionCategories();
		for (String dPosCat : docPosCats) {
			//fContent.add(new Segment("POSCAT-name: " + dPosCat, new Position(++iPos, 1)));
			try {
				// now get only "allowed" content types for given position
				// skip all other types "unsupported" in this view
				for (Position aPos : document.getPositions(dPosCat)) {
					String aConType;
					try {
						aConType = document.getContentType(aPos.offset);
						if (!isConTypeSupported(aConType))
							continue;
					} catch (BadLocationException e) {
						continue;
					}

					SegmentModel aModel = createSegmentModel(document, aPos, aConType);
					if (aModel == null)
						continue;
					
					fContent.add(aModel);
				}
			} catch (BadPositionCategoryException e) {}
		}
	}

	private boolean isConTypeSupported(String iConType) {
		for (String aAllowedConType : IDjangoPartitions.OUTLINE_CONTENT_TYPES)
			if (aAllowedConType.equals(iConType))
				return true;
		return false;
	}
	
	private Image getConTypeImage(String iConType) {
		String aImageName = null;
		if (iConType.equals(IDjangoPartitions.HTML_TAG))
			aImageName = IDjangoImages.OUTL_HTML_TAG_IMAGE;
		else if (iConType.equals(IDjangoPartitions.DJANGO_TAG))
			aImageName = IDjangoImages.OUTL_DJ_TAG_IMAGE;
		else if (iConType.equals(IDjangoPartitions.DJANGO_VARIABLE))
			aImageName = IDjangoImages.OUTL_DJ_VAR_IMAGE;
		else if (iConType.equals(IDjangoPartitions.JAVA_SCRIPT))
			aImageName = IDjangoImages.OUTL_JS_IMAGE;
		else if (iConType.equals(IDjangoPartitions.HTML_CSS))
			aImageName = IDjangoImages.OUTL_CSS_IMAGE;
							
		if (aImageName != null) {
			ImageRegistry registry = DjangoPlugin.getDefault().getImageRegistry();
			return registry.get(aImageName);
		}
		return null;
	}

	private boolean isWordOK(String iConType, String iValue) {
		String aKeyword;
		for (int i=0; i<iValue.length()-1; i++) {
            //if ( iValue.charAt(i);
        } 
		if (iConType.equals(IDjangoPartitions.HTML_TAG)) {
			if (iValue.startsWith("</") && iValue.endsWith(">"))
				return false;
			return true;
	    } else if (iConType.equals(IDjangoPartitions.DJANGO_TAG)) {
			for (String key: IDjangoSyntax.END_TAGS) {
				if (iValue.indexOf(key) >= 0)
					return false;
			}
		} //else if (iConType.equals(IDjangoPartitions.DJANGO_VARIABLE))
			//return true;
		return true;
	}

	private SegmentModel createSegmentModel(IDocument document, Position iPos, String iConType) {
		try {
			String aTokenValue = document.get(iPos.offset, iPos.length).trim();
			if (aTokenValue == null || aTokenValue.isEmpty())
				return null;
			if (!isWordOK(iConType, aTokenValue))
				return null;							
			int aLineNr = document.getLineOfOffset(iPos.offset) + 1;
			//String aDisplay = aConType + ", location: " + aPos.offset + "; " + aValue;
			
			return new SegmentModel(aTokenValue, aLineNr, iPos, getConTypeImage(iConType));		
		} catch (BadLocationException e) {}
		return null;
	}
	
}

/**
 * A segment element.
 */
class SegmentModel {
	public SegmentModel parent;
	public ArrayList<SegmentModel> children = new ArrayList<SegmentModel>();
	
	public String tokenValue;
	public String name;
	public String description = "";
	public Position position;
	public int lineNumber = -1;
	public Image image;

	public SegmentModel() {}
	
	public SegmentModel(String iTokenValue, int iLineNo, Position iPosition, Image iImage) {
		
		if (iTokenValue.startsWith("{%") || iTokenValue.startsWith("{{"))
			iTokenValue = iTokenValue.substring(2, iTokenValue.length()-2).trim();
		else if (iTokenValue.startsWith("<"))
			iTokenValue = iTokenValue.substring(1, iTokenValue.length()-1).trim();
		
		tokenValue= iTokenValue;
		String[] tokArr = iTokenValue.split("\\s+", 2);
		name = tokArr[0];
		if (tokArr.length > 1)
			description = tokArr[1];
		lineNumber = iLineNo;
		position= iPosition;
		image = iImage;
		parent = this;
	}

	public String toString() {
		String aDisplay = name + "[" + description + "] - line " + lineNumber;
		return aDisplay;
	}
	
	public Position getPosition() {
		return position;
	}
}

class DjLabelProvider extends LabelProvider {
	public DjLabelProvider() {
		super();
	}
	public Image getImage(Object element) {
		return ((SegmentModel) element).image;
	}
	
}
