package org.kacprzak.eclipse.django_editor.editors;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.graphics.RGB;
import org.kacprzak.eclipse.django_editor.DjangoActivator;
import org.kacprzak.eclipse.django_editor.IDjangoPartitions;
import org.kacprzak.eclipse.django_editor.editors.completion.DjangoFilterCompletionProcessor;
import org.kacprzak.eclipse.django_editor.editors.completion.DjangoTagCompletionProcessor;
import org.kacprzak.eclipse.django_editor.editors.css.CssScanner;
import org.kacprzak.eclipse.django_editor.editors.dj.DjangoTagScanner;
import org.kacprzak.eclipse.django_editor.editors.dj.DjangoVariableScanner;
import org.kacprzak.eclipse.django_editor.editors.html.HtmlTagScanner;
import org.kacprzak.eclipse.django_editor.editors.js.JavaScriptScanner;
import org.kacprzak.eclipse.django_editor.preferences.IDjangoPrefs;

/**
 * @author Zbigniew Kacprzak
*/
public class DjangoSourceViewerConfiguration extends SourceViewerConfiguration {

	private PresentationReconciler reconciler;

	private DjangoDoubleClickStrategy doubleClickStrategy;
	private DjangoColoredScanner 	defaultScanner;
	private HtmlTagScanner 			htmlTagScanner;
	private DjangoColoredScanner 	htmlCommentScanner;
	private DjangoColoredScanner 	htmlScriptletScanner;
	private DjangoColoredScanner 	htmlDirectiveScanner;
	private DjangoColoredScanner 	htmlDoctypeScanner;
	private AbsDjRuleBasedScanner 	cssScanner;
	private AbsDjRuleBasedScanner 	jsScanner;

	private DjangoColoredScanner 	djCommentScanner;
	private DjangoTagScanner 		djTagScanner;
	private DjangoVariableScanner 	djVariableScanner;

	private ColorProvider 			colorProvider;
	private IPreferenceStore 		store;

	public DjangoSourceViewerConfiguration(ColorProvider colorProvider) {
		this.colorProvider = colorProvider;
		this.store = DjangoActivator.getDefault().getPreferenceStore();
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return IDjangoPartitions.CONFIGURED_CONTENT_TYPES;
	}
	public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new DjangoDoubleClickStrategy();
		return doubleClickStrategy;
	}
	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();

		// default templates
		assistant.setContentAssistProcessor(new DjangoTagCompletionProcessor(), IDocument.DEFAULT_CONTENT_TYPE);
		assistant.setContentAssistProcessor(new DjangoTagCompletionProcessor(), IDjangoPartitions.DJANGO_TAG);
		assistant.setContentAssistProcessor(new DjangoFilterCompletionProcessor(), IDjangoPartitions.DJANGO_VARIABLE);
		assistant.setContentAssistProcessor(new DjangoTagCompletionProcessor(), IDjangoPartitions.HTML_TAG);
		assistant.setContentAssistProcessor(new DjangoTagCompletionProcessor(), IDjangoPartitions.JAVA_SCRIPT);
		assistant.setContentAssistProcessor(new DjangoTagCompletionProcessor(), IDjangoPartitions.HTML_CSS);

//		// display Django keywords
//		assistant.setContentAssistProcessor(new DjangoCompletionProcessor(IDjangoPartitions.DJANGO_TAG),
//				IDjangoPartitions.DJANGO_TAG);
//
//		// display Django filters
//		assistant.setContentAssistProcessor(new DjangoCompletionProcessor(IDjangoPartitions.DJANGO_VARIABLE),
//				IDjangoPartitions.DJANGO_VARIABLE);

		assistant.enableAutoInsert(true);
		assistant.enableAutoActivation(true);
		assistant.setAutoActivationDelay(100);
		assistant.setProposalPopupOrientation(IContentAssistant.CONTEXT_INFO_BELOW);
		assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_BELOW);
		assistant.setContextInformationPopupBackground(colorProvider.getColor(new RGB(0, 0, 0)));

		//assistant.setDocumentPartitioning(IDjangoPartitions.CONFIGURED_CONTENT_TYPES);

		return assistant;
	}
	protected DjangoColoredScanner getDefaultScanner() {
		if (defaultScanner == null) {
			defaultScanner = new DjangoColoredScanner(colorProvider,
					IDjangoPrefs.DEFAULT_FG_COLOR, IDjangoPrefs.DEFAULT_FG_STYLE);
		}
		return defaultScanner;
	}

	protected AbsDjRuleBasedScanner getJavaScriptScanner() {
		if (jsScanner == null) {
			jsScanner = new JavaScriptScanner(colorProvider,
					IDjangoPrefs.DEFAULT_FG_COLOR, IDjangoPrefs.DEFAULT_FG_STYLE);
		}
		return jsScanner;
	}
	protected HtmlTagScanner getHtmlTagScanner() {
		if (htmlTagScanner == null) {
			htmlTagScanner = new HtmlTagScanner(colorProvider,
					//IDjangoPrefs.HTMLTAG_COLOR, IDjangoPrefs.HTMLTAG_STYLE);
					//IDjangoPrefs.HTMLTAG_ATTR_COLOR, IDjangoPrefs.HTMLTAG_ATTR_STYLE);
					IDjangoPrefs.DEFAULT_FG_COLOR, IDjangoPrefs.DEFAULT_FG_STYLE);
		}
		return htmlTagScanner;
	}

	protected DjangoTagScanner getDjangoTagScanner() {
		if (djTagScanner == null) {
			djTagScanner = new DjangoTagScanner(colorProvider,
					//IDjangoPrefs.DJDELIMITER_COLOR, IDjangoPrefs.DJDELIMITER_STYLE);
					IDjangoPrefs.DEFAULT_FG_COLOR, IDjangoPrefs.DEFAULT_FG_STYLE);
		}
		return djTagScanner;
	}

	protected DjangoVariableScanner getDjangoVarScanner() {
		if (djVariableScanner == null) {
			djVariableScanner = new DjangoVariableScanner(colorProvider,
					//IDjangoPrefs.DJDELIMITER_COLOR, IDjangoPrefs.DJDELIMITER_STYLE);
					IDjangoPrefs.DEFAULT_FG_COLOR, IDjangoPrefs.DEFAULT_FG_STYLE);
		}
		return djVariableScanner;
	}

	protected DjangoColoredScanner getDjangoCommentScanner() {
		if (djCommentScanner == null) {
			djCommentScanner = new DjangoColoredScanner(colorProvider,
					IDjangoPrefs.DJCOMMENT_COLOR,
					IDjangoPrefs.DJCOMMENT_STYLE
					);
		}
		return djCommentScanner;
	}

	protected DjangoColoredScanner getHtmlCommentScanner() {
		if (htmlCommentScanner == null) {
			htmlCommentScanner = new DjangoColoredScanner(colorProvider,
					IDjangoPrefs.HTMLCOMMENT_COLOR,
					IDjangoPrefs.HTMLCOMMENT_STYLE
					);
		}
		return htmlCommentScanner;
	}

	/**
	 * Creates or Returns the scanner for scriptlets (&lt;% ... %&gt;).
	 */
	protected DjangoColoredScanner getHtmlScriptletScanner() {
		if (htmlScriptletScanner == null) {
			htmlScriptletScanner = new DjangoColoredScanner(colorProvider,
					IDjangoPrefs.HTMLSCRIPTLET_COLOR,
					IDjangoPrefs.HTMLSCRIPTLET_STYLE
					);
		}
		return htmlScriptletScanner;
	}

	/**
	 * Creates or Returns the scanner for directives (&lt;%@ ... %&gt;).
	 */
	protected DjangoColoredScanner getHtmlDirectiveScanner(){
		if (htmlDirectiveScanner == null) {
			htmlDirectiveScanner = new DjangoColoredScanner(colorProvider,
					IDjangoPrefs.HTMLSCRIPTLET_COLOR,
					IDjangoPrefs.HTMLSCRIPTLET_STYLE
					);
		}
		return htmlDirectiveScanner;
	}

	/**
	 * Creates or Returns the scanner for DOCTYPE decl.
	 */
	protected DjangoColoredScanner getHtmlDoctypeScanner(){
		if (htmlDoctypeScanner == null) {
			htmlDoctypeScanner = new DjangoColoredScanner(colorProvider,
					IDjangoPrefs.HTMLDOCTYPE_COLOR,
					IDjangoPrefs.HTMLDOCTYPE_STYLE
					);
		}
		return htmlDoctypeScanner;
	}

	/**
	 * Creates or Returns the scanner for inner CSS.
	 */
	protected AbsDjRuleBasedScanner getCSSScanner() {
		if (cssScanner == null) {
			cssScanner = new CssScanner(colorProvider);
		}
		return cssScanner;
	}


	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		if (reconciler != null)
			return reconciler;

		reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = null;

		dr = new DefaultDamagerRepairer(getDefaultScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		dr = new DefaultDamagerRepairer(getDjangoTagScanner());
		reconciler.setDamager(dr, IDjangoPartitions.DJANGO_TAG);
		reconciler.setRepairer(dr, IDjangoPartitions.DJANGO_TAG);

		dr = new DefaultDamagerRepairer(getDjangoVarScanner());
		reconciler.setDamager(dr, IDjangoPartitions.DJANGO_VARIABLE);
		reconciler.setRepairer(dr, IDjangoPartitions.DJANGO_VARIABLE);

		dr = new DefaultDamagerRepairer(getDjangoCommentScanner());
		reconciler.setDamager(dr, IDjangoPartitions.DJANGO_COMMENT);
		reconciler.setRepairer(dr, IDjangoPartitions.DJANGO_COMMENT);

		dr = new DefaultDamagerRepairer(getHtmlTagScanner());
		reconciler.setDamager(dr, IDjangoPartitions.HTML_TAG);
		reconciler.setRepairer(dr, IDjangoPartitions.HTML_TAG);

		dr = new DefaultDamagerRepairer(getHtmlCommentScanner());
		reconciler.setDamager(dr, IDjangoPartitions.HTML_COMMENT);
		reconciler.setRepairer(dr, IDjangoPartitions.HTML_COMMENT);

		dr = new DefaultDamagerRepairer(getHtmlDoctypeScanner());
		reconciler.setDamager(dr, IDjangoPartitions.HTML_DOCTYPE);
		reconciler.setRepairer(dr, IDjangoPartitions.HTML_DOCTYPE);

		dr = new DefaultDamagerRepairer(getHtmlScriptletScanner());
		reconciler.setDamager(dr, IDjangoPartitions.HTML_SCRIPTLET);
		reconciler.setRepairer(dr, IDjangoPartitions.HTML_SCRIPTLET);

		dr = new DefaultDamagerRepairer(getHtmlDirectiveScanner());
		reconciler.setDamager(dr, IDjangoPartitions.HTML_DIRECTIVE);
		reconciler.setRepairer(dr, IDjangoPartitions.HTML_DIRECTIVE);

		dr = new DefaultDamagerRepairer(getJavaScriptScanner());
		reconciler.setDamager(dr, IDjangoPartitions.JAVA_SCRIPT);
		reconciler.setRepairer(dr, IDjangoPartitions.JAVA_SCRIPT);

		dr = new DefaultDamagerRepairer(getCSSScanner());
		reconciler.setDamager(dr, IDjangoPartitions.HTML_CSS);
		reconciler.setRepairer(dr, IDjangoPartitions.HTML_CSS);

		return reconciler;
	}

    //updates the syntax highlighting for the specified preference
    //assumes that that editor colorCache has been updated with the
    //new named color
    public void updateSyntaxColorAndStyle() {
        if (reconciler == null) {
        	return;
        }

    	defaultScanner.updateColors();
    	djCommentScanner.updateColors();
    	djTagScanner.updateColors();
    	djVariableScanner.updateColors();
    	cssScanner.updateColors();
    	jsScanner.updateColors();

        htmlTagScanner.updateColors();
    	htmlCommentScanner.updateColors();
    	htmlScriptletScanner.updateColors();
    	htmlDirectiveScanner.updateColors();
    	htmlDoctypeScanner.updateColors();
    }

}