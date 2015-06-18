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

import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * A content outline page.
 */
public class DjangoContentOutlinePage extends ContentOutlinePage {

	protected IEditorInput fEditorInput;
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
		viewer.setContentProvider(new DjangoOutlineContentProvider(fDocumentProvider, fEditorInput));
		viewer.setLabelProvider(new DjLabelProvider());
		viewer.addSelectionChangedListener(this);
		
//		fTextEditor.getSelectionProvider().addSelectionChangedListener(this);

		if (fEditorInput != null) {
			viewer.setInput(fEditorInput);
			viewer.expandAll();
		}
	}
	
	/* (non-Javadoc)
	 * Method declared on ContentOutlinePage
	 */
	public void selectionChanged(SelectionChangedEvent event) {

		if (event.getSelection() instanceof TextSelection) {
			TextSelection selection = (TextSelection) event.getSelection();
			if (selection.getLength() > 0)
				return;
			
//			System.out.println("DjangoContentOutlinePage.selectionChanged; selection=" + selection.getLength());
			return;
		}
		
		super.selectionChanged(event);

		ISelection selection= event.getSelection();
		if (selection.isEmpty())
			fTextEditor.resetHighlightRange();
		else {
			DjDocTag element= (DjDocTag) ((IStructuredSelection) selection).getFirstElement();
			int start = element.getPositionStart().getOffset();
			int end = 0;
			int length = element.getPositionStart().getLength();
			if (element.getPositionEnd() != null) {
				end = element.getPositionEnd().getOffset();
				length = end - start + element.getPositionEnd().getLength();
			}

			try {
				fTextEditor.setHighlightRange(start, length, true);
				// TODO: PREFERENCES: IF HIGHLIGHT up to end tag
				fTextEditor.selectAndReveal(start, length);
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
	public void setInput(IEditorInput input) {
		fEditorInput = input;
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
				viewer.setInput(fEditorInput);
				viewer.expandAll();
				control.setRedraw(true);
			}
		}
	}

//	@Override
//	public void propertyChanged(Object source, int propId) {
//		System.out.println("DjangoContentOutlinePage.propertyChanged; source=" + source + "; propId=" + propId);
//	}
}
