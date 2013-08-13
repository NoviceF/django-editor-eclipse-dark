package org.kacprzak.eclipse.django_editor.editors.dj;

/**
 * Django tags and filters
 * @author Zbigniew Kacprzak
*/
public interface IDjangoSyntax {

	//
	// WARNING: all arrays MUST BE sorted
	//

	public static final String[] TAGS = {
		  "autoescape"
		, "block"
		, "comment"
		, "csrf_token"
		, "cycle"
		, "debug"
		, "else"
		, "empty"
		, "extends"
		, "filter"
		, "firstof"
		, "for"
		, "if"
		, "ifchanged"
		, "ifequal"
		, "ifnotequal"
		, "include"
		, "load"
		, "now"
		, "regroup"
		, "spaceless"
		, "ssi"
		, "templatetag"
		, "url"
		, "verbatim"
		, "widthratio"
		, "with"
	};

	public static final String[] END_TAGS = {
		"endautoescape"
		, "endblock"
		, "endcomment"
		, "endfilter"
		, "endfor"
		, "endif"
		, "endifchanged"
		, "endifequal"
		, "endifnotequal"
		, "endspaceless"
	};
	
	public static final String[] FILTERS = {
		  "add"
		, "addslashes"
		, "capfirst"
		, "center"
		, "cut"
		, "date"
		, "default"
		, "default_if_none"
		, "dictsort"
		, "dictsortreversed"
		, "divisibleby"
		, "escape"
		, "escapejs"
		, "filesizeformat"
		, "first"
		, "fix_ampersands"
		, "floatformat"
		, "force_escape"
		, "get_digit"
		, "iriencode"
		, "join"
		, "last"
		, "length"
		, "length_is"
		, "linebreaks"
		, "linebreaksbr"
		, "linenumbers"
		, "ljust"
		, "lower"
		, "make_list"
		, "phone2numeric"
		, "pluralize"
		, "pprint"
		, "random"
		, "removetags"
		, "rjust"
		, "safe"
		, "safeseq"
		, "slice"
		, "slugify"
		, "stringformat"
		, "striptags"
		, "time"
		, "timesince"
		, "timeuntil"
		, "title"
		, "truncatewords"
		, "truncatewords_html"
		, "unordered_list"
		, "upper"
		, "urlencode"
		, "urlize"
		, "urlizetrunc"
		, "wordcount"
		, "wordwrap"
		, "yesno"
	};

	public static final String[] FORLOOP = {
		  "forloop.counter"
		, "forloop.counter0"
		, "forloop.first"
		, "forloop.last"
		, "forloop.parentloop"
		, "forloop.revcounter"
		, "forloop.revcounter0"
	};

	public static final String[] PREDICATES =
	{
		"and",
		"in",
		"not",
		"or",
	};
}
