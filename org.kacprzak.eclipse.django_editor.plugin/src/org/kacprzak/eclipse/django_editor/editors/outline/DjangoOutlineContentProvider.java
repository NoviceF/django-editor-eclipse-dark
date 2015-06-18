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
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;

/**
 */
class DjangoOutlineContentProvider implements ITreeContentProvider {
	
	private IDocumentProvider fDocumentProvider;
	private IEditorInput fEditorInput;
	
	private List<DjDocTag> fContent = new ArrayList<DjDocTag>();

	public DjangoOutlineContentProvider(IDocumentProvider provider, IEditorInput iInput)
	{
		super();
		fDocumentProvider = provider;
		fEditorInput = iInput;
	}
	
	/*
	 * @see ITreeContentProvider#getChildren(Object)
	 */
	public Object[] getChildren(Object element) {
		if (element instanceof DjDocTag)
			return ((DjDocTag)element).children.toArray();
		if (element == fEditorInput)
			return fContent.toArray();
		return new Object[0];
	}

	/*
	 * @see ITreeContentProvider#getParent(Object)
	 */
	public Object getParent(Object element) {
		if (element instanceof DjDocTag)
			return ((DjDocTag)element).parent;
		return null;
	}

	/*
	 * @see ITreeContentProvider#hasChildren(Object)
	 */
	public boolean hasChildren(Object element) {
		if (element instanceof DjDocTag)
			return ((DjDocTag)element).children.size() > 0;
		return element == fEditorInput;
	}

	/*
	 * @see IStructuredContentProvider#getElements(Object)
	 */
	public Object[] getElements(Object element) {
		return fContent.toArray();
	}

	/*
	 * @see IContentProvider#inputChanged(Viewer, Object, Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		fContent.clear();
		if (newInput != null) {
			IDocument document= fDocumentProvider.getDocument(newInput);
			if (document != null) {
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

