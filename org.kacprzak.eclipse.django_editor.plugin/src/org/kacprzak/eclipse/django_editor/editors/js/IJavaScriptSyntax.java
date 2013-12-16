package org.kacprzak.eclipse.django_editor.editors.js;

/**
 * JavaScript
 * @author Zbigniew Kacprzak
*/
public interface IJavaScriptSyntax {

	public static final String KEYWORDS[] = {
		"break", 
		"case", "catch", "continue", 
		"debugger", "default", "delete", "do",
		"else", 
		"false", "finally", "for", "function", 
		"if", "in", "instanceof", "Infinity",  
		"new", "null", "NaN", 
		"return", 
		"switch", 
		"true", "this", "throw", "try", "typeof", 
		"undefined", 
		"var", "void", "while", "with" 
	};

	public static final String RESERVED_WORDS[] = {
		"abstract", 
		"boolean", "byte", 
		"char", "class", "const", 
		"debugger", "double", 
		"enum", "export", "extends", 
		"final", "float", 
		"goto", 
		"implements", "import", "int", "interface", 
		"label", "let", "long", 
		"native", 
		"package", "private", "protected", "public", 
		"short", "static", "super", "synchronized", 
		"throws", "transient", 
		"volatile",
		"yield",
		
		// for browser
		"blur", 
		"closed", 
		"document", 
		"focus", "frames", 
		"history", 
		"innerHeight", "innerWidth", 
		"length", "location", 
		"navigator", 
		"open", "outerHeight", "outerWidth", 
		"parent", 
		"screen", "screenX", "screenY", "statusbar", 
		"window" 
	};
	
	public static final String GLO_FUNCTIONS[] = {
		"alert", "confirm", "prompt", 
		"decodeURI", "decodeURIComponent",
		"encodeURI", "encodeURIComponent", "escape", "eval",
		"isFinite", "isNaN", "Number",
		"parseFloat", "parseInt", "String",
		"unescape", 
	};
	public static final String OBJ_FUNCTIONS[] = {
		"alert", "confirm", "prompt", 
		"abs", "acos", "anchor", "asin", "atan", "atan2", 
		"big", "blink", "bold", 
		"ceil", "charAt", "charCodeAt", "concat", "constructor", "cos", 
		"Date", "Date.parse ", "Date.UTC ", 
		"every", "exec", "exp", 
		"filter", "fixed", "floor", "fontcolor", "fontsize", "forEach", 
		"getDate", "getDay", "getFullYear", "getHours", "getMilliseconds", 
		"getMinutes", "getMonth", "getSeconds", "getTime", "getTimezoneOffset", 
		"getUTCDate", "getUTCDay", "getUTCFullYear", "getUTCHours", "getUTCMilliseconds", 
		"getUTCMinutes", "getUTCMonth", "getUTCSeconds", "getYear", 
		"indexOf", "italics", 
		"join", 
		"lastIndexOf", "length", "link", "localeCompare", "log", 
		"map", "match", "max", "min", 
		"pop", "pow", "push", 
		"random", "reduce", "reduceRight", "replace", "reverse", "round", 
		"search", "setDate", "setFullYear", "setHours", "setMilliseconds", "setMinutes", "setMonth", "setSeconds", "setTime", 
		"setUTCDate", "setUTCFullYear", "setUTCHours", "setUTCMilliseconds", "setUTCMinutes", "setUTCMonth", "setUTCSeconds", 
		"setYear", 
		"shift", "sin", "slice", "small", "some", "sort", "splice", "split", "sqrt", "strike", "sub", "substr", "substring", "sup", 
		"tan", "test", "toDateString", "toExponential", "toFixed", "toGMTString", 
		"toLocaleDateString", "toLocaleFormat", "toLocaleLowerCase", "toLocaleString", "toLocaleTimeString", "toLocaleUpperCase", 
		"toLowerCase", "toPrecision", "toSource", "toString", "toTimeString", "toUpperCase", "toUTCString", 
		"unshift", 
		"valueOf", 
	};

	public static final String OPERATORS[] = {
		"+", "-", "*", "/", "%", "=",
		"++", "--", "+=", "-=", "*=", "/=",
		">>=", "<<=", ">>>=", "&=", "|=", "^=", "&", "|", "^", "~", 
		">>", "<<", ">>>", "==", "!=", "===", "!==", 
		">", ">=", "<", "<=", "&&", "||", "!", "?", ":"
	};
}
