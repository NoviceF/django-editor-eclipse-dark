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

import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.kacprzak.eclipse.django_editor.IDjangoPartitions;
import org.kacprzak.eclipse.django_editor.editors.outline.DjDocTag.TokenType;

/**
 */
class DjangoOutlineDocumentParser {
	
	private List<DjDocTag> fContent;// = new ArrayList<DjDocTag>();

	DjangoOutlineDocumentParser(List<DjDocTag> iContent) {
		fContent = iContent;
	}
	
	void parseDocument(IDocument document) {

//System.out.println("\n**** START parseDocument");		
		DjDocTag aCurrent = null;
		String[] docPosCats = document.getPositionCategories();
		for (String dPosCat : docPosCats) {
			if (!dPosCat.startsWith("__content_types_category"))
				continue;
			try {
				Position[] positions = document.getPositions(dPosCat);
				// now get only "allowed" content types for given position
				// skip all other types "unsupported" in this view
				for (Position aPos : positions) {
					String aConType, aToken;
					int aLineNr;
					try {
						aConType = document.getContentType(aPos.offset);
//						if (!isConTypeSupported(aConType))
//							continue;
//System.out.println("[" + dPosCat + "] Content type supported: " + aConType);
						aToken = document.get(aPos.offset, aPos.length).trim();
						if (aToken == null || aToken.isEmpty())
							continue;
						aLineNr = document.getLineOfOffset(aPos.offset) + 1;
//System.out.println("  Line " + aLineNr + ": " + aToken);
					} catch (BadLocationException e) {
						continue;
					}
					
					aCurrent = DjDocTag.addNewTag(aConType, aToken, aLineNr, aPos, fContent, aCurrent);
				}
			} catch (BadPositionCategoryException e) {}
		}
//System.out.println("\n**** END parseDocument\n");		
	}

	private boolean isConTypeSupported(String iConType) {
		for (String aAllowedConType : IDjangoPartitions.OUTLINE_CONTENT_TYPES)
			if (aAllowedConType.equals(iConType))
				return true;
		return false;
	}
}

