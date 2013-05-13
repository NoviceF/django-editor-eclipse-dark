package org.kacprzak.eclipse.django_editor.preferences;

/**
 * @author Zbigniew Kacprzak
*/
public interface IDjangoPrefs {
	// see below: COLOR_NAMES, STYLE_NAMES

	// Django colors
	public static final String DJKEYWORD_COLOR     = "_pref_djkeyword_color";
	public static final String DJUSRTAG_COLOR      = "_pref_djusertag_color";
	public static final String DJDELIMITER_COLOR   = "_pref_djtag_delimiter_color";
    public static final String DJTAG_ATTR_COLOR    = "_pref_djtag_attr_color";
	public static final String DJCOMMENT_COLOR     = "_pref_djcomment_color";
	public static final String DJSTRING_COLOR      = "_pref_djstring_color";
	public static final String DJFILTER_COLOR      = "_pref_djfilter_color";
	public static final String DJUSRFILTER_COLOR   = "_pref_djuserfilter_color";
	public static final String DJVARIABLE_COLOR    = "_pref_djvariable_color";

	public static final String DJKEYWORD_STYLE      = "_pref_djkeyword_style";
	public static final String DJUSRTAG_STYLE       = "_pref_djusertag_style";
	public static final String DJDELIMITER_STYLE    = "_pref_djtag_delimiter_style";
    public static final String DJTAG_ATTR_STYLE     = "_pref_djtag_attr_style";
	public static final String DJCOMMENT_STYLE      = "_pref_djcomment_style";
	public static final String DJSTRING_STYLE       = "_pref_djstring_style";
	public static final String DJFILTER_STYLE       = "_pref_djfilter_style";
	public static final String DJUSRFILTER_STYLE    = "_pref_djusrfilter_style";
	public static final String DJVARIABLE_STYLE     = "_pref_djvariable_style";

	// HTML colors
	public static final String HTMLTAG_COLOR        = "_pref_htmltag_color";
    public static final String HTMLTAG_ATTR_COLOR   = "_pref_htmltag_attr_color";
	public static final String HTMLCOMMENT_COLOR    = "_pref_htmlcomment_color";
	public static final String HTMLSTRING_COLOR     = "_pref_htmlstring_color";
	public static final String HTMLDOCTYPE_COLOR    = "_pref_htmldoctype_color";
	public static final String HTMLSCRIPTLET_COLOR  = "_pref_htmlscriptlet_color";
	public static final String HTMLSCRIPT_COLOR 	= "_pref_htmlscript_color";

	public static final String HTMLTAG_STYLE        = "_pref_htmltag_style";
	public static final String HTMLTAG_ATTR_STYLE   = "_pref_htmltag_attr_style";
	public static final String HTMLCOMMENT_STYLE    = "_pref_htmlcomment_style";
	public static final String HTMLSTRING_STYLE     = "_pref_htmlstring_style";
	public static final String HTMLDOCTYPE_STYLE    = "_pref_htmldoctype_style";
	public static final String HTMLSCRIPTLET_STYLE  = "_pref_htmlscriptlet_style";
	public static final String HTMLSCRIPT_STYLE 	= "_pref_htmlscript_style";

	// CSS colors
	public static final String CSSSELECTOR_COLOR	= "_pref_cssselector_color";
	public static final String CSSPROP_COLOR        = "_pref_cssprop_color";
	public static final String CSSCOMMENT_COLOR     = "_pref_csscomment_color";
	public static final String CSSVALUE_COLOR       = "_pref_cssvalue_color";

	public static final String CSSSELECTOR_STYLE	= "_pref_cssselector_style";
	public static final String CSSPROP_STYLE        = "_pref_cssprop_style";
	public static final String CSSCOMMENT_STYLE     = "_pref_csscomment_style";
	public static final String CSSVALUE_STYLE       = "_pref_cssvalue_style";

	// default colors
	public static final String DEFAULT_FG_COLOR     = "_pref_foreground_color";
	public static final String DEFAULT_FG_STYLE     = "_pref_foreground_style";

	// JavaScript colors
	public static final String JSSTRING_COLOR       = "_pref_jsstring_color";
	public static final String JSKEYWORD_COLOR      = "_pref_jskeyword_color";
	public static final String JSCOMMENT_COLOR      = "_pref_jscomment_color";
	public static final String JSNUMBER_COLOR		= "_pref_jsnumber_color";

	public static final String JSSTRING_STYLE       = "_pref_jsstring_style";
	public static final String JSKEYWORD_STYLE      = "_pref_jskeyword_style";
	public static final String JSCOMMENT_STYLE      = "_pref_jscomment_style";
	public static final String JSNUMBER_STYLE		= "_pref_jsnumber_style";

	public static final String[] COLOR_NAMES = {
		  DJKEYWORD_COLOR
		, DJUSRTAG_COLOR
		, DJDELIMITER_COLOR
		, DJTAG_ATTR_COLOR
		, DJCOMMENT_COLOR
		, DJSTRING_COLOR
		, DJFILTER_COLOR
		, DJUSRFILTER_COLOR
		, DJVARIABLE_COLOR

		, HTMLTAG_COLOR
		, HTMLTAG_ATTR_COLOR
		, HTMLCOMMENT_COLOR
		, HTMLSTRING_COLOR
		, HTMLDOCTYPE_COLOR
		, HTMLSCRIPTLET_COLOR
		, HTMLSCRIPT_COLOR

		, CSSSELECTOR_COLOR
		, CSSPROP_COLOR
		, CSSCOMMENT_COLOR
		, CSSVALUE_COLOR

		, DEFAULT_FG_COLOR

		, JSSTRING_COLOR
		, JSKEYWORD_COLOR
		, JSCOMMENT_COLOR
		, JSNUMBER_COLOR
	};

	public static final String[] STYLE_NAMES = {
		  DJKEYWORD_STYLE
		, DJUSRTAG_STYLE
		, DJDELIMITER_STYLE
		, DJTAG_ATTR_STYLE
		, DJCOMMENT_STYLE
		, DJSTRING_STYLE
		, DJFILTER_STYLE
		, DJUSRFILTER_STYLE
		, DJVARIABLE_STYLE

		, HTMLTAG_STYLE
		, HTMLTAG_ATTR_STYLE
		, HTMLCOMMENT_STYLE
		, HTMLSTRING_STYLE
		, HTMLDOCTYPE_STYLE
		, HTMLSCRIPTLET_STYLE
		, HTMLSCRIPT_STYLE

		, CSSSELECTOR_STYLE
		, CSSPROP_STYLE
		, CSSCOMMENT_STYLE
		, CSSVALUE_STYLE

		, DEFAULT_FG_STYLE

		, JSSTRING_STYLE
		, JSKEYWORD_STYLE
		, JSCOMMENT_STYLE
		, JSNUMBER_STYLE
	};

}
