package org.kacprzak.eclipse.django_editor.editors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.kacprzak.eclipse.django_editor.Logging;

/**
 * Predefined and set colors manager
 * @author Zbigniew Kacprzak
*/
public class ColorProvider {

	private Map<RGB, Color> colorTable = new HashMap<RGB, Color>(10);
	private Map<String, Token> tokenTable = new HashMap<String, Token>(10);
	IPreferenceStore store;

	public ColorProvider(IPreferenceStore store) {
		this.store = store;
	}

	public void clear() {
		removeColors();
        removeTokens();
	}

	private void removeColors() {
		Iterator<Color> it = colorTable.values().iterator();
		while (it.hasNext()){
			it.next().dispose();
		}
        colorTable.clear();
	}
	private void removeTokens() {
        tokenTable.clear();
	}

    public void reloadColor(String key)
    {
    	String prefKey = key.replaceAll("_style", "_color");
    	// in fact color is just removed, will be created on next getToken or getColor
        if( tokenTable.containsKey(prefKey) ) {
            tokenTable.remove(prefKey);
        }
        RGB rgb = StringConverter.asRGB(store.getString(prefKey));
        if (colorTable.containsKey(rgb)) {
        	//colorTable.get(rgb).dispose();
        	colorTable.remove(rgb);
        }
    }

	public IToken getToken(String prefKey){
		return getToken(prefKey, 0);
	}

	public IToken getToken(String prefKey, String styleName){
		return getToken(prefKey, store.getInt(styleName));
	}

//	public IToken getToken(String prefKey, boolean bold){
//		return getToken(prefKey, bold, false);
//	}
//
//	public IToken getToken(String prefKey, boolean bold, boolean italic) {
//	   int style = 0;
//       if (bold) 	style |= SWT.BOLD;
//       if (italic) 	style |= SWT.ITALIC;
//       return getToken(prefKey, style);
//	}

	public IToken getToken(String prefKey, int style) {
	   Token token = (Token) tokenTable.get(prefKey);
	   if (token == null){
	       String colorName = store.getString(prefKey);
	       if (colorName.equals(""))
	    	   Logging.error("No such preference stored: " + prefKey);
	       RGB rgb = StringConverter.asRGB(colorName);
           token = new Token(new TextAttribute(getColor(rgb), null, style));
	       tokenTable.put(prefKey, token);
	   }
	   return token;
	}

	public TextAttribute getTextAttribute(String prefKey) {
		String colorName = store.getString(prefKey);
		RGB rgb = StringConverter.asRGB(colorName);
		return getTextAttribute(rgb, 0);
	}

	public TextAttribute getTextAttribute(String prefKey, int style) {
		String colorName = store.getString(prefKey);
		RGB rgb = StringConverter.asRGB(colorName);
		return getTextAttribute(rgb, style);
	}

	public TextAttribute getTextAttribute(RGB rgb, int style) {
		return new TextAttribute(getColor(rgb), null, style);
	}

	public Color getColor(String prefKey){
		String colorName = store.getString(prefKey);
		RGB rgb = StringConverter.asRGB(colorName);
		return getColor(rgb);
	}

	public Color getColor(RGB rgb) {
		Color color = (Color) colorTable.get(rgb);
		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			colorTable.put(rgb, color);
		}
		return color;
	}
}
