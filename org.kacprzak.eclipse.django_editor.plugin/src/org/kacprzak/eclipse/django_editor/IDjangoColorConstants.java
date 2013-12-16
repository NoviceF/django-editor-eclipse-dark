package org.kacprzak.eclipse.django_editor;

import org.eclipse.swt.graphics.RGB;

/**
 * Default color constants for syntax highlighter
 * @author Zbigniew Kacprzak
*/
public interface IDjangoColorConstants {
	// Django colors
	RGB DJANGO_KEYWORD 		= new RGB(176, 0, 88);
	RGB DJANGO_USRTAG 		= new RGB(153, 2, 17);
	RGB DJANGO_DELIMITER	= new RGB(123, 89, 68);
	RGB DJANGO_TAG_ATTR		= new RGB(255, 128, 64);
	RGB DJANGO_COMMENT 		= new RGB(153, 153, 102);
	RGB DJANGO_STRING 		= new RGB(210, 0, 210);
	RGB DJANGO_FILTER 		= new RGB(179, 58, 0);
	RGB DJANGO_USRFILTER 	= new RGB(210, 0, 0);
	RGB DJANGO_VARIABLE 	= new RGB(0, 128, 192);

	// HTML colors
	RGB PROC_INSTR   		= new RGB(128, 128, 128);
	RGB HTML_TAG          	= new RGB(0, 0, 255);
	RGB HTML_TAG_ATTR      	= new RGB(255, 128, 0);
	RGB HTML_COMMENT 		= new RGB(101, 101, 101);
	RGB HTML_STRING       	= new RGB(210, 0, 210);
	RGB HTML_DOCTYPE		= new RGB(153, 102, 51);
	RGB HTML_SCRIPTLET     	= new RGB(255,   0,   0);
	RGB HTML_SCRIPT			= new RGB(63, 95, 191);

	// CSS colors
	RGB CSS_PROP     		= new RGB(  0,   0, 255);
	RGB CSS_COMMENT  		= new RGB(101, 101, 101);
	RGB CSS_VALUE    		= new RGB(  0, 128,   0);
	RGB CSS_SELECTOR		= new RGB(56, 146, 146);

	// JavaScript colors
	RGB JS_STRING			= new RGB(0, 153, 102);
	RGB JS_KEYWORD 			= new RGB(0,   0, 255);
	RGB JS_KEYWORD2			= new RGB(102, 0, 205);
	RGB JS_COMMENT 			= new RGB(153, 102, 102);
	RGB JS_NUMBER			= new RGB(86, 61, 150);
	RGB JS_OPERATOR         = new RGB(255, 0, 0);
	RGB JS_FUNCTION         = new RGB(210, 105, 0);
	
	// jQuery colors
	RGB JQ_FUNCTION         = new RGB(255, 50, 102);
	RGB JQ_SELECTOR         = new RGB(155, 50, 0);

	// defaults
	RGB DEFAULT_FOREGROUND  = new RGB(20, 20, 150);//Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND).getRGB();
	//RGB DEFAULT_BACKGROUND  = new RGB(255, 255, 255);
	//Color c1 = JFaceResources.getColorRegistry().get(JFacePreferences.
	//DjangoActivator.
	//TextViewer.getControl()
	// Control.getForeground()
}
