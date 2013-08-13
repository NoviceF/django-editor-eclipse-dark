package org.kacprzak.eclipse.django_editor;

import java.util.TreeMap;

import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.graphics.Image;

/**
 * Django template file partitions
 * @author Zbigniew Kacprzak
*/
public interface IDjangoPartitions {

	public final static String HTML_COMMENT   	= "_part_html_comment";
	public final static String HTML_TAG       	= "_part_html_tag";
	public final static String HTML_SCRIPTLET 	= "_part_html_script";
	public final static String HTML_DOCTYPE   	= "_part_html_doctype";
	public final static String HTML_DIRECTIVE 	= "_part_html_directive";
	public final static String HTML_CSS       	= "_part_html_css";

	public final static String JAVA_SCRIPT	  	= "_part_html_javascript";

	public final static String DJANGO_COMMENT 	= "_part_dj_comments";
	public final static String DJANGO_TAG 		= "_part_dj_tag";
	public final static String DJANGO_VARIABLE	= "_part_dj_variable";
	public final static String DJANGO_FILTER 	= "_part_dj_filter";


    public final static String DJANGO_DEFAULT = IDocument.DEFAULT_CONTENT_TYPE;

    public final static String[] CONFIGURED_CONTENT_TYPES = new String[] {
    	DJANGO_DEFAULT
		, DJANGO_COMMENT
    	, DJANGO_TAG
		, DJANGO_FILTER
		, DJANGO_VARIABLE
		, JAVA_SCRIPT
		, HTML_COMMENT
		, HTML_TAG
		, HTML_SCRIPTLET
		, HTML_DOCTYPE
		, HTML_DIRECTIVE
		, HTML_CSS
	};

    // these will be shown in Outline View
   //public final static TreeMap<String, Image> OUTLINE_CONTENT_TYPES = new TreeMap<String, Image>();
    public final static String[] OUTLINE_CONTENT_TYPES = new String[] {
//    	DJANGO_DEFAULT,
////		, DJANGO_COMMENT
    	DJANGO_TAG
		, DJANGO_FILTER
		, DJANGO_VARIABLE
		, JAVA_SCRIPT
////		, HTML_COMMENT
		, HTML_TAG
//		, HTML_SCRIPTLET
//		, HTML_DOCTYPE
//		, HTML_DIRECTIVE
		, HTML_CSS
	};
}
