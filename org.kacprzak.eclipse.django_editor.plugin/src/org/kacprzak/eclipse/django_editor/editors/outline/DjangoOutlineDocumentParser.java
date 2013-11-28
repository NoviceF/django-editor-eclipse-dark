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
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.swt.graphics.Image;
import org.kacprzak.eclipse.django_editor.DjangoPlugin;
import org.kacprzak.eclipse.django_editor.IDjangoImages;
import org.kacprzak.eclipse.django_editor.IDjangoPartitions;
import org.kacprzak.eclipse.django_editor.editors.dj.IDjangoSyntax;
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
			//fContent.add(new Segment("POSCAT-name: " + dPosCat, new Position(++iPos, 1)));
			try {
				// now get only "allowed" content types for given position
				// skip all other types "unsupported" in this view
				for (Position aPos : document.getPositions(dPosCat)) {
					String aConType, aToken;
					int aLineNr;
					try {
						aConType = document.getContentType(aPos.offset);
						if (!isConTypeSupported(aConType))
							continue;
//System.out.println("Content type supported: " + aConType);
						aToken = document.get(aPos.offset, aPos.length).trim();
						if (aToken == null || aToken.isEmpty())
							continue;
						aLineNr = document.getLineOfOffset(aPos.offset) + 1;
//System.out.println("  Line " + aLineNr + ": " + aToken);
					} catch (BadLocationException e) {
						continue;
					}
					
					DjDocTag aTag = new DjDocTag(aConType, aToken, aLineNr, aPos);
//System.out.println("  DjDocTag; keyword=" + aTag.keyword + "; type=" + aTag.tokType.name());
					if (aTag.keyword.isEmpty())
						continue;
					switch (aTag.tokType) {
						case START:
							aTag.parent = aCurrent;
							if (aCurrent != null)
								aCurrent.children.add(aTag);
							aCurrent = aTag;
							break;
							
						case STARTEND:
							aTag.parent = aCurrent;
							if (aCurrent != null)
								aCurrent.children.add(aTag);
							aTag.posEnd = aPos;
							break;
							
						case END:
							aTag.parent = null;
							DjDocTag aTagStart = findStartingTag(aTag, aCurrent);
//System.out.println("  END Tag; keyword=" + aTag.keyword + ( aTagStart == null ? "" : "; starting: " + aTagStart.lineNumber));
							if (aTagStart != null) {
								aTag.parent = aTagStart.parent;
								aCurrent = aTagStart.parent;
								aTagStart.posEnd = aPos;
							} else
								aCurrent = null;
							break;
							
						case INVALID:
							continue;
					}				
					if (aTag.parent == null && aTag.tokType != TokenType.END)
						fContent.add(aTag);
				}
			} catch (BadPositionCategoryException e) {}
		}
//System.out.println("\n**** END parseDocument\n");		
	}

	private DjDocTag findStartingTag(DjDocTag iTag, DjDocTag iCurrent) {
		DjDocTag starting = iCurrent;
		if (starting == null)
			return null;
		do {
			if (starting.tokType == TokenType.START) {
				if (starting.keyword.equals(iTag.keyword))
					return starting;
				if ( ("end" + starting.keyword).equals(iTag.keyword))
					return starting;
			}
			starting = starting.parent;
		} while (starting != null);
		return null;
	}
	private boolean isConTypeSupported(String iConType) {
		for (String aAllowedConType : IDjangoPartitions.OUTLINE_CONTENT_TYPES)
			if (aAllowedConType.equals(iConType))
				return true;
		return false;
	}
}

/**
 * A segment element.
 */
class DjDocTag {
	enum TokenType {
		START,
		END,
		STARTEND,
		INVALID
	};

	TokenType tokType = TokenType.INVALID;
	String conType;

	DjDocTag parent;
	ArrayList<DjDocTag> children = new ArrayList<DjDocTag>();
	
	String tokenValue;
	String keyword;
	String description = "";
	Position posStart;
	Position posEnd;
	int lineNumber = -1;
	Image image;

	DjDocTag(String iConType, String iToken, int iLineNo, Position iPosition) {

		posStart = iPosition;
		conType = iConType;
		lineNumber = iLineNo;
		posEnd = null;
		
		setFields(iToken);
		image = getConTypeImage();
	}


	private boolean isNonEndRequiringKeyword(String iKeyword) {
		String[] keywords = {
				"br", "hr"
		};
		for (String aKey: keywords)
			if (aKey.equalsIgnoreCase(iKeyword))
				return true;
		return false;
	}
	
	private void setFields(String iTokenValue) {
		String name = "";
		if (iTokenValue.startsWith("{%")) {
			tokType = TokenType.STARTEND;
			iTokenValue = iTokenValue.substring(2, iTokenValue.length()-2).trim();
			for (String key: IDjangoSyntax.END_TAGS) {
				if (iTokenValue.startsWith(key))
					tokType = TokenType.END;
				else if (("end" + iTokenValue).startsWith(key))
					tokType = TokenType.START;
			}
		} else if (iTokenValue.startsWith("{{")) {
			iTokenValue = iTokenValue.substring(2, iTokenValue.length()-2).trim();
			tokType = TokenType.STARTEND;
		} else if (iTokenValue.startsWith("<")) {
			tokType = TokenType.START;
			iTokenValue = iTokenValue.substring(1, iTokenValue.length()-1).trim();
			name = iTokenValue.split("\\s+|\\|", 2)[0];
			if (iTokenValue.startsWith("/")) {
				iTokenValue = iTokenValue.substring(1).trim();
				tokType = TokenType.END;
			} else if (isNonEndRequiringKeyword(name)) {
				tokType = TokenType.STARTEND;
			} else if (iTokenValue.endsWith("/") || iTokenValue.endsWith("</"+name)) {
				tokType = TokenType.STARTEND;
			}
		}

		tokenValue= iTokenValue;
		String[] tokArr = iTokenValue.split("\\s+|\\|", 2);
		keyword = tokArr[0];
		if (tokArr.length > 1)
			description = tokArr[1].split("\\n|\\r", 2)[0];
	}

	public String toString() {
		String aDisplay = "";
		//aDisplay += "ch=" + children.size() + " ";
		//aDisplay += (tokType == TokenType.END ? "END" : "");
		aDisplay += keyword;
		// TODO: PREFERENCES:if (preferences.extendedOutlineItemView)
		aDisplay += "   [" + description + "] - line " + lineNumber;
		return aDisplay;
	}
	
	Position getPositionStart() {
		return posStart;
	}

	Position getPositionEnd() {
		return posEnd;
	}

	private Image getConTypeImage() {
		String aImageName = null;
		if (conType.equals(IDjangoPartitions.HTML_TAG)) {
			aImageName = IDjangoImages.OUTL_HTML_TAG_IMAGE;
			if (tokType == TokenType.END)
				aImageName = IDjangoImages.OUTL_HTML_TAG_END_IMAGE;
		
		} else if (conType.equals(IDjangoPartitions.DJANGO_TAG)) {
			aImageName = IDjangoImages.OUTL_DJ_TAG_IMAGE;
			if (tokType == TokenType.END)
				aImageName = IDjangoImages.OUTL_DJ_TAG_END_IMAGE;
			
		} else if (conType.equals(IDjangoPartitions.DJANGO_VARIABLE))
			aImageName = IDjangoImages.OUTL_DJ_VAR_IMAGE;
		
		else if (conType.equals(IDjangoPartitions.JAVA_SCRIPT))
			aImageName = IDjangoImages.OUTL_JS_IMAGE;
		
		else if (conType.equals(IDjangoPartitions.HTML_CSS))
			aImageName = IDjangoImages.OUTL_CSS_IMAGE;
							
		if (aImageName != null) {
			ImageRegistry registry = DjangoPlugin.getDefault().getImageRegistry();
			return registry.get(aImageName);
		}
		return null;
	}
}
