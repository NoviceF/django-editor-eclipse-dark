package org.kacprzak.eclipse.django_editor.editors;

import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.DefaultTextHover;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.formatter.ContentFormatter;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.hyperlink.DefaultHyperlinkPresenter;
import org.eclipse.jface.text.hyperlink.IHyperlinkPresenter;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
//import org.eclipse.ui.examples.javaeditor.java.JavaAutoIndentStrategy;
import org.kacprzak.eclipse.django_editor.IDjangoPartitions;
import org.kacprzak.eclipse.django_editor.editors.completion.DjangoCssCompletionProcessor;
import org.kacprzak.eclipse.django_editor.editors.completion.DjangoFilterCompletionProcessor;
import org.kacprzak.eclipse.django_editor.editors.completion.DjangoHtmlCompletionProcessor;
import org.kacprzak.eclipse.django_editor.editors.completion.DjangoJavaScriptCompletionProcessor;
import org.kacprzak.eclipse.django_editor.editors.css.CssScanner;
import org.kacprzak.eclipse.django_editor.editors.dj.DjangoTagScanner;
import org.kacprzak.eclipse.django_editor.editors.dj.DjangoVariableScanner;
import org.kacprzak.eclipse.django_editor.editors.format.TextFormattingStrategy;
import org.kacprzak.eclipse.django_editor.editors.html.HtmlTagScanner;
import org.kacprzak.eclipse.django_editor.editors.js.JavaScriptScanner;
import org.kacprzak.eclipse.django_editor.preferences.IDjangoPrefs;

/**
 * @author Zbigniew Kacprzak
*/
public class DjangoSourceViewerConfiguration extends TextSourceViewerConfiguration {

//	public final static String PREFERENCE_TAB_WIDTH= AbstractDecoratedTextEditorPreferenceConstants.EDITOR_TAB_WIDTH;
//	public final static String EDITOR_SPACES_FOR_TABS= "spacesForTabs"; //$NON-NLS-1$
//	public final static String SPACES_FOR_TABS= PreferenceConstants.EDITOR_SPACES_FOR_TABS;
	
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
//	private IPreferenceStore 		store;

	public DjangoSourceViewerConfiguration(ColorProvider colorProvider) {
		this.colorProvider = colorProvider;
//		this.store = DjangoPlugin.getDefault().getPreferenceStore();
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return IDjangoPartitions.CONFIGURED_CONTENT_TYPES;
	}
	
	/*
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getAutoEditStrategies(org.eclipse.jface.text.source.ISourceViewer, java.lang.String)
	 */
//	public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
//		IAutoEditStrategy strategy= (IDocument.DEFAULT_CONTENT_TYPE.equals(contentType) ? new JavaAutoIndentStrategy() : new DefaultIndentLineAutoEditStrategy());
//		return new IAutoEditStrategy[] { strategy };
//	}
	
	public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new DjangoDoubleClickStrategy();
		return doubleClickStrategy;
	}
	
	@Override
	public IContentFormatter getContentFormatter(ISourceViewer sourceViewer) {
		ContentFormatter formatter = new ContentFormatter();
		
		for (String aPartition : IDjangoPartitions.CONFIGURED_CONTENT_TYPES)
			formatter.setFormattingStrategy(new TextFormattingStrategy(), aPartition);	
		return formatter;
	}
	
	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();

		//assistant.setDocumentPartitioning(IDjangoPartitions.CONFIGURED_CONTENT_TYPES);

		// default templates
//		assistant.setContentAssistProcessor(new DjAndHtmlCompletionProcessor(), IDocument.DEFAULT_CONTENT_TYPE);
		assistant.setContentAssistProcessor(new DjangoHtmlCompletionProcessor(), IDjangoPartitions.DJANGO_DEFAULT);
//		assistant.setContentAssistProcessor(new HtmlTagCompletionProcessor(), IDocument.DEFAULT_CONTENT_TYPE);
//		assistant.setContentAssistProcessor(new DjHtCompletionProcessor(), IDocument.DEFAULT_CONTENT_TYPE);
//		assistant.setContentAssistProcessor(new DjangoTagCompletionProcessor(), IDocument.DEFAULT_CONTENT_TYPE);
		assistant.setContentAssistProcessor(new DjangoFilterCompletionProcessor(), IDjangoPartitions.DJANGO_VARIABLE);
		assistant.setContentAssistProcessor(new DjangoFilterCompletionProcessor(), IDjangoPartitions.DJANGO_FILTER);

		assistant.setContentAssistProcessor(new DjangoFilterCompletionProcessor(), IDjangoPartitions.DJANGO_TAG);
		assistant.setContentAssistProcessor(new DjangoHtmlCompletionProcessor(), IDjangoPartitions.HTML_TAG);
		assistant.setContentAssistProcessor(new DjangoJavaScriptCompletionProcessor(), IDjangoPartitions.JAVA_SCRIPT);
		assistant.setContentAssistProcessor(new DjangoCssCompletionProcessor(), IDjangoPartitions.HTML_CSS);
//		assistant.setContentAssistProcessor(new DjangoTagCompletionProcessor(), IDjangoPartitions.HTML_TAG);
//		assistant.setContentAssistProcessor(new DjangoTagCompletionProcessor(), IDjangoPartitions.JAVA_SCRIPT);
//		assistant.setContentAssistProcessor(new DjangoTagCompletionProcessor(), IDjangoPartitions.HTML_CSS);

//    	DJANGO_DEFAULT
//		, DJANGO_COMMENT
//    	, DJANGO_TAG
//		, DJANGO_FILTER
//		, DJANGO_VARIABLE
//		, JAVA_SCRIPT
//		, HTML_COMMENT
//		, HTML_TAG
//		, HTML_SCRIPTLET
//		, HTML_DOCTYPE
//		, HTML_DIRECTIVE
//		, HTML_CSS

		assistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));
		
		assistant.enableAutoInsert(true);
		assistant.enableAutoActivation(true);
		assistant.setAutoActivationDelay(100);

		assistant.enableColoredLabels(true);
		assistant.setStatusLineVisible(true);
		
		assistant.setProposalPopupOrientation(IContentAssistant.CONTEXT_INFO_BELOW);
//		assistant.setProposalPopupOrientation(IContentAssistant.PROPOSAL_OVERLAY);
		assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_BELOW);
//		assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
//		assistant.setContextInformationPopupBackground(colorProvider.getColor(new RGB(0, 0, 0)));


		assistant.setProposalSelectorBackground(colorProvider.getColor(new RGB(255, 255, 250)));

		return assistant;
	}
	
//	public IInformationControlCreator getInformationControlCreator(ISourceViewer sourceViewer) {
//
//		return new IInformationControlCreator() {			
//
//			public IInformationControl createInformationControl(Shell parent) {
//								
//				final DefaultInformationControl.IInformationPresenter
//				   presenter = new DefaultInformationControl.IInformationPresenter() {
//				      public String updatePresentation(Display display, 
//				    		  						   String infoText,
//				    		  						   TextPresentation presentation, 
//				    		  						   int maxWidth, 
//				    		  						   int maxHeight) 
//				      {
//				         int start = -1;
//
//				         // Loop over all characters of information text
//				         for (int i = 0; i < infoText.length(); i++) {
//				            switch (infoText.charAt(i)) {
//				               case '<' :
//
//				                  // Remember start of tag
//				                  start = i;
//				                  break;
//				               case '>' :
//				                  if (start >= 0) {
//
//				                    // We have found a tag and create a new style range
//				                    StyleRange range = 
//				                       new StyleRange(start, i - start + 1, null, null, SWT.BOLD);
//
//				                    // Add this style range to the presentation
//				                    presentation.addStyleRange(range);
//
//				                    // Reset tag start indicator
//				                    start = -1;
//				                  }
//				                  break;
//				         }
//				      }
//				      // Return the information text
//				      return infoText;
//				   }
//				};
//				return new DefaultInformationControl(parent, presenter);
//			} // createInformationControl
//		}; // new IInformationControlCreator
//	} // getInformationControlCreator
	
//	@Override
//	public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType) {
//		System.out.println("getTextHover; contentType: " + contentType);
//		return new DefaultTextHover(sourceViewer);
//	}
	
//	@Override
//	public IHyperlinkPresenter getHyperlinkPresenter(ISourceViewer sourceViewer) {
//		return new DefaultHyperlinkPresenter(new RGB(0, 0, 255));
//	}

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