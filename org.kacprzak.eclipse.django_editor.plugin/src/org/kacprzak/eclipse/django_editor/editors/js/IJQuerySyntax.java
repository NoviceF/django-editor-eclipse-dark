package org.kacprzak.eclipse.django_editor.editors.js;

/**
 * jQuery
 * @author Zbigniew Kacprzak
*/
public interface IJQuerySyntax {

	public static final String HTML[] = {
		"addClass", "after", "append", "appendTo", "attr", 
		"before", 
		"clone", "css", 
		"detach", 
		"empty", 
		"hasClass", "height", "html", 
		"innerHeight", "innerWidth", "insertAfter", "insertBefore", 
		"offset", "offsetParent", "outerHeight", "outerWidth", 
		"position", "prepend", "prependTo", "prop", 
		"remove", "removeAttr", "removeClass", "removeProp", "replaceAll", "replaceWith", 
		"scrollLeft", "scrollTop", 
		"text", "toggleClass", 
		"unwrap", 
		"val", "width", "wrap", "wrapAll", "wrapInner"
	};

	public static final String TRAVERSING[] = {
		"add", "addBack", "andSelf", 
		"children", "closest", "contents", 
		"each", "end", "eq", 
		"filter", "find", "first", 
		"has", "is", "last", 
		"map", "next", "nextAll", "nextUntil", "not", 
		"offsetParent", 
		"parent", "parents", "parentsUntil", "prev", "prevAll", "prevUntil", 
		"siblings", "slice"		
	};
	
	public static final String EVENTS[] = {
		"bind", "blur", 
		"change", "click", 
		"dblclick", "delegate", "die", 
		"error", 
		"focus", "focusin", "focusout", 
		"hover", 
		"keydown", "keypress", "keyup", 
		"live", "load", 
		"mousedown", "mouseenter", "mouseleave", "mousemove", "mouseout", "mouseover", "mouseup", 
		"off", "on", "one", 
		"ready", "resize", 
		"scroll", "select", "submit", 
		"toggle", "trigger", "triggerHandler", 
		"unbind", "undelegate", "unload"
	};
	
	public static final String EFFECTS[] = {
		"animate", 
		"clearQueue", 
		"delay", "dequeue", 
		"fadeIn", "fadeOut", "fadeTo", "fadeToggle", "finish", 
		"hide", 
		"queue", 
		"show", "slideDown", "slideToggle", "slideUp", "stop", 
		"toggle"
	};
	
	public static final String AJAX[] = {
		"ajaxComplete", "ajaxError", "ajaxSend", "ajaxStart", "ajaxStop", "ajaxSuccess", 
		"load", "serialize", "serializeArray"		
	};
	
	public static final String MISC[] = {
		"data", "each", "get", "index",
		"removeData", "size", "toArray",
		
		"ajax", "ajaxPrefilter", "ajaxSetup", "ajaxTransport", 
		"get", "getJSON", "getScript", 
		"noConflict", 	
		"param", "post", "proxy" 
		
	};
	
	public static final String GLOBAL[] = {
		"$.ajax", "$.ajaxPrefilter", "$.ajaxSetup",	"$.ajaxTransport", 
		"$.get", "$.getJSON", "$.getScript", 
		"$.noConflict", 	
		"$.param", "$.post", "$.proxy" 
	};
}
