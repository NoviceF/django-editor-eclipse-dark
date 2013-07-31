package org.kacprzak.eclipse.django_editor.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.kacprzak.eclipse.django_editor.IDjangoPartitions;
import org.kacprzak.eclipse.django_editor.editors.html.DocTypeRule;

/**
 * Django file partitions scanner
 * @author Zbigniew Kacprzak
*/
public class DjangoPartitionScanner extends RuleBasedPartitionScanner {

	public DjangoPartitionScanner() {

		IToken djTag 		 = new Token(IDjangoPartitions.DJANGO_TAG);
		IToken djVariable 	 = new Token(IDjangoPartitions.DJANGO_VARIABLE);
		IToken djComment 	 = new Token(IDjangoPartitions.DJANGO_COMMENT);

		IToken htmlComment 	 = new Token(IDjangoPartitions.HTML_COMMENT);
		IToken htmlTag 		 = new Token(IDjangoPartitions.HTML_TAG);
		IToken htmlScriptlet = new Token(IDjangoPartitions.HTML_SCRIPTLET);
		IToken htmlDoctype   = new Token(IDjangoPartitions.HTML_DOCTYPE);
		IToken htmlDirective = new Token(IDjangoPartitions.HTML_DIRECTIVE);
		IToken javaScript    = new Token(IDjangoPartitions.JAVA_SCRIPT);
		IToken htmlCss       = new Token(IDjangoPartitions.HTML_CSS);

		List<IPredicateRule> rules = new ArrayList<IPredicateRule>();

		rules.add(new MultiLineRule("{% comment %}", "{% endcomment %}", djComment));
		rules.add(new SingleLineRule("{#", "#}", djComment));
		rules.add(new MultiLineRule("{%", "%}", djTag));
		rules.add(new MultiLineRule("{{", "}}", djVariable));

		rules.add(new MultiLineRule("<!--", "-->", htmlComment));
		rules.add(new MultiLineRule("<%--", "--%>", htmlComment));
		rules.add(new DocTypeRule(htmlDoctype));
		rules.add(new MultiLineRule("<%@", "%>", htmlDirective));
		rules.add(new MultiLineRule("<%", "%>", htmlScriptlet));
		rules.add(new MultiLineRule("<![CDATA[", "]]>", htmlDoctype));
		rules.add(new MultiLineRule("<?xml", "?>", htmlDoctype));
		rules.add(new MultiLineRule("<script", "</script>", javaScript));
		rules.add(new MultiLineRule("<style", "</style>", htmlCss));
		rules.add(new MultiLineRule("<", ">", htmlTag));

		setPredicateRules(rules.toArray(new IPredicateRule[0]));
	}
}
