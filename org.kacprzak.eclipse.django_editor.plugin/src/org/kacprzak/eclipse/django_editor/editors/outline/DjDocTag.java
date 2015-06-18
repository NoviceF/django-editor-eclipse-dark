package org.kacprzak.eclipse.django_editor.editors.outline;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.swt.graphics.Image;
import org.kacprzak.eclipse.django_editor.DjangoPlugin;
import org.kacprzak.eclipse.django_editor.IDjangoImages;
import org.kacprzak.eclipse.django_editor.IDjangoPartitions;
import org.kacprzak.eclipse.django_editor.StringUtils;
import org.kacprzak.eclipse.django_editor.editors.dj.IDjangoSyntax;


/**
 * A segment element.
 */
class DjDocTag {
	enum TokenType {
		START,
		END,
		STARTEND,
		JAVA_SCRIPT,
		CSS_SCRIPT,
		INVALID
	};

	TokenType tokType = TokenType.INVALID;
	String conType;

	DjDocTag parent;
	ArrayList<DjDocTag> children = new ArrayList<DjDocTag>();
	
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
			if (iKeyword.matches(aKey + "\\s*[/]?"))
				return true;
		return false;
	}
	
	private boolean isCssScript() {
		return conType.equals(IDjangoPartitions.HTML_CSS);
	}
	
	private boolean isJavaScript() {
		return conType.equals(IDjangoPartitions.JAVA_SCRIPT);
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
			
		} else if (isCssScript()) {
			tokType = TokenType.CSS_SCRIPT;
			keyword = iTokenValue.substring(1).split(">|\\s+", 2)[0];
			iTokenValue = iTokenValue.substring(keyword.length()+1).split(">", 2)[0];

		} else if (isJavaScript()) {
			tokType = TokenType.JAVA_SCRIPT;
			keyword = iTokenValue.substring(1).split(">|\\s+", 2)[0];
			iTokenValue = iTokenValue.substring(keyword.length()+1).split(">", 2)[0];
			
		} else if (iTokenValue.startsWith("<!--")) {
			tokType = TokenType.STARTEND;
			description = StringUtils.getWord(iTokenValue, 2, StringUtils.DELIM + "=");
			keyword = "!--";
			
		} else if (iTokenValue.startsWith("<")) {
			tokType = TokenType.START;
			iTokenValue = iTokenValue.substring(1, iTokenValue.length()-1).trim();
			name = iTokenValue.split("\\s+|\\|", 2)[0];
			if (iTokenValue.startsWith("/")) {
				iTokenValue = iTokenValue.substring(1).trim();
				tokType = TokenType.END;
			} else if (isNonEndRequiringKeyword(name)) {
				tokType = TokenType.STARTEND;
				if (iTokenValue.endsWith("/"))
					iTokenValue = iTokenValue.substring(0, iTokenValue.length()-1).trim();
			} else if (iTokenValue.endsWith("/") || iTokenValue.endsWith("</"+name)) {
				tokType = TokenType.STARTEND;
			}
		}

		String[] tokArr = iTokenValue.split("\\s+|\\|", 2);
		if (keyword == null || keyword.isEmpty())
			keyword = tokArr[0];
		if (tokArr.length > 1 && description.isEmpty())
			description = tokArr[1].split("\\n|\\r|\\{", 2)[0];
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

	private static DjDocTag findStartingTag(DjDocTag iTag, DjDocTag iCurrent) {
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

	public static DjDocTag addNewTag(String iConType, String iToken, int iLineNo, Position iPosition, List<DjDocTag> ioContent, DjDocTag ioCurrent) {
		
		DjDocTag aTag = new DjDocTag(iConType, iToken, iLineNo, iPosition);
//System.out.println("  DjDocTag; keyword=" + aTag.keyword + "; type=" + aTag.tokType.name());
		if (aTag.keyword.isEmpty())
			return ioCurrent;
		switch (aTag.tokType) {
			case START:
				aTag.parent = ioCurrent;
				if (ioCurrent != null)
					ioCurrent.children.add(aTag);
				ioCurrent = aTag;
				break;
				
			case STARTEND:
				aTag.parent = ioCurrent;
				if (ioCurrent != null)
					ioCurrent.children.add(aTag);
				aTag.posEnd = iPosition;
				break;
				
			case JAVA_SCRIPT:
				aTag.parent = ioCurrent;
				if (ioCurrent != null)
					ioCurrent.children.add(aTag);
				aTag.posEnd = iPosition;
				
				// TODO: parse internal <script> here
				break;
				
			case CSS_SCRIPT:
				aTag.parent = ioCurrent;
				if (ioCurrent != null)
					ioCurrent.children.add(aTag);
				aTag.posEnd = iPosition;
				
				int aLineNo = iLineNo;
				int aPosBr = 0;
				String[] names = iToken.replace("\r","").split("\\{");
				for (int i=0; i < names.length-1; i++) {
					aPosBr = iToken.indexOf("{", aPosBr + 1);
					Position aPosition = new Position(iPosition.offset + aPosBr, 0);
					String[] lines = names[i].split("\n");
					aLineNo += lines.length-1;
					String aName = lines[lines.length-1].trim();
					DjDocTag aCssTag = new DjDocTag(iConType, aName, aLineNo, aPosition);
					aCssTag.parent = aTag;
					aTag.children.add(aCssTag);
				}
				break;
				
			case END:
				aTag.parent = null;
				DjDocTag aTagStart = findStartingTag(aTag, ioCurrent);
//System.out.println("  END Tag; keyword=" + aTag.keyword + ( aTagStart == null ? "" : "; starting: " + aTagStart.lineNumber));
				if (aTagStart != null) {
					aTag.parent = aTagStart.parent;
					ioCurrent = aTagStart.parent;
					aTagStart.posEnd = iPosition;
				} else
					ioCurrent = null;
				break;
				
			case INVALID:
				return ioCurrent;
		} // switch				
		
		if (aTag.parent == null && aTag.tokType != TokenType.END)
			ioContent.add(aTag);
		return ioCurrent;
	}
}