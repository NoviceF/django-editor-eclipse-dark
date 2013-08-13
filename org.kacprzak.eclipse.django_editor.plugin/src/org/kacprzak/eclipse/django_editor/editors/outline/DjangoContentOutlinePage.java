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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.kacprzak.eclipse.django_editor.DjangoPlugin;
import org.kacprzak.eclipse.django_editor.IDjangoImages;
import org.kacprzak.eclipse.django_editor.IDjangoPartitions;
import org.kacprzak.eclipse.django_editor.editors.dj.IDjangoSyntax;

/**
 * A content outline page.
 */
public class DjangoContentOutlinePage extends ContentOutlinePage {

	protected Object fInput;
	protected IDocumentProvider fDocumentProvider;
	protected ITextEditor fTextEditor;

	/**
	 * Creates a content outline page using the given provider and the given editor.
	 * 
	 * @param provider the document provider
	 * @param editor the editor
	 */
	public DjangoContentOutlinePage(IDocumentProvider provider, ITextEditor editor) {
		super();
		fDocumentProvider = provider;
		fTextEditor = editor;
	}
	
	/* (non-Javadoc)
	 * Method declared on ContentOutlinePage
	 */
	public void createControl(Composite parent) {

		super.createControl(parent);

		TreeViewer viewer= getTreeViewer();
		//viewer.setContentProvider(new ContentProvider());
		viewer.setContentProvider(new DjangoOutlineProvider(fDocumentProvider, fInput));
		viewer.setLabelProvider(new DjLabelProvider());
		viewer.addSelectionChangedListener(this);

		if (fInput != null)
			viewer.setInput(fInput);
	}
	
	/* (non-Javadoc)
	 * Method declared on ContentOutlinePage
	 */
	public void selectionChanged(SelectionChangedEvent event) {

		super.selectionChanged(event);

		ISelection selection= event.getSelection();
		if (selection.isEmpty())
			fTextEditor.resetHighlightRange();
		else {
//			XMLElement element = (XMLElement) ((IStructuredSelection) selection).getFirstElement();				
			SegmentModel element= (SegmentModel) ((IStructuredSelection) selection).getFirstElement();
			int start = element.getPosition().getOffset();
			int length = element.getPosition().getLength();

			try {
				fTextEditor.setHighlightRange(start, length, true);
			} catch (IllegalArgumentException x) {
				fTextEditor.resetHighlightRange();
			}
		}
	}
	
	/**
	 * Sets the input of the outline page
	 * 
	 * @param input the input of this outline page
	 */
	public void setInput(Object input) {
		fInput = input;
		update();
	}
	
	/**
	 * Updates the outline page.
	 */
	public void update() {
		TreeViewer viewer= getTreeViewer();

		if (viewer != null) {
			Control control= viewer.getControl();
			if (control != null && !control.isDisposed()) {
				control.setRedraw(false);
				viewer.setInput(fInput);
				viewer.expandAll();
				control.setRedraw(true);
			}
		}
	}
}
