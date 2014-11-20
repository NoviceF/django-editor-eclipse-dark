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

import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.texteditor.IDocumentProvider;

/**
 */
class DjangoOutlineContentProvider implements ITreeContentProvider {
	
	private final static String SEGMENTS = "__django_editor_segments"; //$NON-NLS-1$
	private IPositionUpdater fPositionUpdater = new DefaultPositionUpdater(SEGMENTS);
	
	private IDocumentProvider fDocumentProvider;
	private Object fInput;
	
	private List<DjDocTag> fContent = new ArrayList<DjDocTag>();

	public DjangoOutlineContentProvider(IDocumentProvider provider, Object iInput)
	{
		super();
		fDocumentProvider = provider;
		
		// fix bug 251682 - auto-expand outline view
//		fOutlineViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
		fInput = iInput;
	}
	
	/*
	 * @see ITreeContentProvider#getChildren(Object)
	 */
	public Object[] getChildren(Object element) {
		if (element instanceof DjDocTag)
			return ((DjDocTag)element).children.toArray();
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
		if (element instanceof DjDocTag)
			return ((DjDocTag)element).parent;
			//return fInput;
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
		if (element instanceof DjDocTag)
			return ((DjDocTag)element).children.size() > 0;
		return element == fInput;
//		if (element == fInput) 
//			return true;
//		return ((DjDocTag)element).children.size() > 0;
	}

	/*
	 * @see IStructuredContentProvider#getElements(Object)
	 */
	public Object[] getElements(Object element) {
		return fContent.toArray();
//		if (element instanceof DjDocTag)
//			return ((DjDocTag)element).children.toArray();
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
				(new DjangoOutlineDocumentParser(fContent)).parseDocument(document);
			}
		}
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

}

