package org.kacprzak.eclipse.django_editor.preferences;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.kacprzak.eclipse.django_editor.DjangoPlugin;

/**
 * @author Zbigniew Kacprzak
*/
public class DjangoEditorPreferencesPage extends PreferencePage implements IWorkbenchPreferencePage
{
	private IPreferenceStore  		mStore;
	private DialogPreferencesStore  mLocalStore;

	private StyledText 	mTextPreview;
	private List 		mColorsList;
	private ColorEditor mColorEditor;
	private Button 		mBoldCheckBox;
	private Button 		mItalicCheckBox;
//    private Button 		mUnderlineCheckBox;


	public DjangoEditorPreferencesPage() {
        setDescription("Django Editor syntax highlighting settings.");
        this.mStore = DjangoPlugin.getDefault().getPreferenceStore();
        setPreferenceStore(this.mStore);

        mLocalStore = new DialogPreferencesStore();
        mLocalStore.initFrom(mStore);
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected Control createContents(Composite parent)
	{
		 Control control= createPageFields(parent);
		 initialize();
		 return control;
	}

    protected void initialize() {

        if (mColorsList != null){
            for (SyntaxItem it: mSyntaxItems) {
            	mColorsList.add(it.description);
            }

            mColorsList.getDisplay().asyncExec(new Runnable() {
                public void run() {
                    if (mColorsList != null && !mColorsList.isDisposed()) {
                    	mColorsList.select(0);
                        onColorSelectionChanged();
                    }
                }
            });
        }
    }

    protected void onColorSelectionChanged() {
        int i = mColorsList.getSelectionIndex();

        String colorName = mSyntaxItems.get(i).colorName;
        String styleName = mSyntaxItems.get(i).styleName;
        RGB rgb= PreferenceConverter.getColor(mLocalStore, colorName);
        int style= mLocalStore.getInt(styleName);

        mColorEditor.setColorValue(rgb);
    	mBoldCheckBox.setSelection((style & SWT.BOLD) != 0);
        mItalicCheckBox.setSelection((style & SWT.ITALIC) != 0);
//        mUnderlineCheckBox.setSelection((styleValue & SWT.BORDER) != 0);
    }

    // listener for check style boxes
    protected SelectionListener mCheckBoxListener = new SelectionListener() {
        public void widgetDefaultSelected(SelectionEvent e) { }
        public void widgetSelected(SelectionEvent e) {
            int i = mColorsList.getSelectionIndex();
            int style = 0;

            String styleKey = mSyntaxItems.get(i).styleName;
            if (mBoldCheckBox.getSelection())
                style |= SWT.BOLD;
            if (mItalicCheckBox.getSelection())
                style |= SWT.ITALIC;
//            if (mUnderlineCheckBox.getSelection())
//                style |= SWT.UNDERLINE_SINGLE;
            mLocalStore.setKey(styleKey, style);
            showChangeInPreviewEditor();
        }
    };

    protected SelectionListener mColorButtonChanged = new SelectionListener() {
        public void widgetDefaultSelected(SelectionEvent e) { }
        public void widgetSelected(SelectionEvent e) {
            int i = mColorsList.getSelectionIndex();
            String key = mSyntaxItems.get(i).colorName;

            RGB rgb = mColorEditor.getColorValue();
            mLocalStore.setKey(key, StringConverter.asString(rgb));
            showChangeInPreviewEditor();
        }
    };

	private Control createPageFields(Composite parent)
	{
        Composite composite = new Composite(parent, SWT.NONE );
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        composite.setLayout(layout);

        Label l= new Label(composite, SWT.LEFT );
        GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        gd.horizontalSpan = 2;
        gd.heightHint = convertHeightInCharsToPixels(1) / 2;
        l.setLayoutData(gd);

        l= new Label(composite, SWT.LEFT);
        l.setText("Appearance color options:");
        gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        gd.horizontalSpan = 2;
        l.setLayoutData(gd);

        Composite editorComposite = new Composite(composite, SWT.NONE);
        layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        editorComposite.setLayout(layout);
        gd= new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_VERTICAL);
        gd.horizontalSpan= 2;
        editorComposite.setLayoutData(gd);

        mColorsList = new List(editorComposite, SWT.SINGLE | SWT.V_SCROLL | SWT.BORDER);
        gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
        gd.heightHint = convertHeightInCharsToPixels(10);
        gd.widthHint = convertHeightInCharsToPixels(10);
        mColorsList.setLayoutData(gd);

        Composite stylesComposite= new Composite(editorComposite, SWT.NONE);
        layout= new GridLayout();
        layout.marginHeight= 0;
        layout.marginWidth= 0;
        layout.numColumns= 2;
        stylesComposite.setLayout(layout);
        stylesComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

        l= new Label(stylesComposite, SWT.LEFT);
        l.setText("Color:");
        gd = new GridData();
        gd.horizontalAlignment= GridData.BEGINNING;
        l.setLayoutData(gd);

        mColorEditor = new ColorEditor(stylesComposite);
        Button colorButton = mColorEditor.getButton();
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalAlignment = GridData.BEGINNING;
        colorButton.setLayoutData(gd);

        mBoldCheckBox = new Button(stylesComposite, SWT.CHECK);
        mBoldCheckBox.setText("Bold");
        gd = new GridData();
        gd.horizontalSpan= 2;
        mBoldCheckBox.setLayoutData(gd);

        mItalicCheckBox = new Button(stylesComposite, SWT.CHECK);
        mItalicCheckBox.setText("Italic");
        gd = new GridData();
        gd.horizontalSpan= 2;
        mItalicCheckBox.setLayoutData(gd);

//        mTextPreview = new StyledText(parent, SWT.BORDER);
//        try {
//            FontData fontData = new FontData("Courier New", 9, SWT.NONE);
//            mTextPreview.setFont(new Font(parent.getDisplay(), fontData));
//        } catch (Throwable e) {}
//        updatePreviewText();

//        formatAndStyleRangeHelper = new StyledTextForShowingCodeFactory();
//        labelExample = formatAndStyleRangeHelper.createStyledTextForCodePresentation(parent);
//        updateLabelExample(PyFormatStd.getFormat(), PydevPrefs.getChainedPrefStore());

        // colors on list listener
        mColorsList.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) { }
            public void widgetSelected(SelectionEvent e) {
            	onColorSelectionChanged();
            }
        });
        mBoldCheckBox.addSelectionListener(mCheckBoxListener);
        mItalicCheckBox.addSelectionListener(mCheckBoxListener);
        colorButton.addSelectionListener(mColorButtonChanged);

        return composite;
	}

	private void updatePreviewText() {
        String code =
        	"{% ifequal brand 'sss' %}                              \n" +
        	"  <div id=\"branding\">                                \n" +
        	"    <h1 id=\"si\">{% setting 'YP_NAME' %}</h1>         \n" +
        	"    <h2 id=\"vs\">{{ va.r|myfilter:\"example\" }}</h2> \n" +
        	"    <h2 id=\"ve\">{{ vari|title:example }}</h2>        \n" +
        	"  </div>                                               \n" +
        	"{% endifequal %}                                       \n" +
        	"{{ variable|capfirst }}                                \n" +
        	"{% comment %}                                          \n" +
        	"	I can't see you...                                  \n" +
        	"{% endcomment %}                                       \n" +
        	"<!-- <li class='{% block menuclass-sch %} -->          \n" +
        	"";
        mTextPreview.setText(code);
	}
    /*
     * @see PreferencePage#performOk()
     */
    public boolean performOk() {
        mLocalStore.copyTo(mStore);
        //(new InstanceScope()).getNode(DjangoActivator.PLUGIN_ID).flush();
        //DjangoActivator.getDefault().savePluginPreferences();
		//mLocalStore.fireChangeEvents(mStore);
        return true;
    }

    /*
     * @see PreferencePage#performDefaults()
     */
    protected void performDefaults() {
    	DjangoPreferenceInitializer.setToDefaults(mLocalStore);
    	DjangoPreferenceInitializer.setToDefaults(mStore);

    	onColorSelectionChanged();
        super.performDefaults();
        showChangeInPreviewEditor();
    }

    protected void showChangeInPreviewEditor() {
    	//mLocalStore.copyTo(mStore);
    	//this.updateLabelExample(PyFormatStd.getFormat(), localStore);
    }

	private class SyntaxItem {
		String description;
		String colorName;
		String styleName;

		SyntaxItem(String descr, String color, String style) {
			this.description  = descr;
			this.colorName = color;
			this.styleName = style;
		}
	}

    private ArrayList<SyntaxItem> mSyntaxItems = new ArrayList<SyntaxItem>(Arrays.asList(
    		new SyntaxItem("Default Foreground", 	IDjangoPrefs.DEFAULT_FG_COLOR, IDjangoPrefs.DEFAULT_FG_STYLE),
    		new SyntaxItem("Django Tag", 			IDjangoPrefs.DJKEYWORD_COLOR, IDjangoPrefs.DJKEYWORD_STYLE),
    		new SyntaxItem("Django Custom Tag", 	IDjangoPrefs.DJUSRTAG_COLOR, IDjangoPrefs.DJUSRTAG_STYLE),
    		new SyntaxItem("Django Variable", 		IDjangoPrefs.DJVARIABLE_COLOR, IDjangoPrefs.DJVARIABLE_STYLE),
    		new SyntaxItem("Django String", 		IDjangoPrefs.DJSTRING_COLOR, IDjangoPrefs.DJSTRING_STYLE),
    		new SyntaxItem("Django Comment", 		IDjangoPrefs.DJCOMMENT_COLOR, IDjangoPrefs.DJCOMMENT_STYLE),
    		new SyntaxItem("Django Filter", 		IDjangoPrefs.DJFILTER_COLOR, IDjangoPrefs.DJFILTER_STYLE),
    		new SyntaxItem("Django Custom Filter", 	IDjangoPrefs.DJUSRFILTER_COLOR, IDjangoPrefs.DJUSRFILTER_STYLE),
    		new SyntaxItem("Django Delimiter {{, {%", IDjangoPrefs.DJDELIMITER_COLOR, IDjangoPrefs.DJDELIMITER_STYLE),
    		//new SyntaxItem("Django Tag Attribute", IDjangoPrefs.DJTAG_ATTR_COLOR, IDjangoPrefs.DJTAG_ATTR_STYLE),

    		new SyntaxItem("HTML Tag", 				IDjangoPrefs.HTMLTAG_COLOR, IDjangoPrefs.HTMLTAG_STYLE),
    		new SyntaxItem("HTML Attribute Name", 	IDjangoPrefs.HTMLTAG_ATTR_COLOR, IDjangoPrefs.HTMLTAG_ATTR_STYLE),
    		new SyntaxItem("HTML Comment", 			IDjangoPrefs.HTMLCOMMENT_COLOR, IDjangoPrefs.HTMLCOMMENT_STYLE),
    		new SyntaxItem("HTML String", 			IDjangoPrefs.HTMLSTRING_COLOR, IDjangoPrefs.HTMLSTRING_STYLE),
    		new SyntaxItem("DOCTYPE Color", 		IDjangoPrefs.HTMLDOCTYPE_COLOR, IDjangoPrefs.HTMLDOCTYPE_STYLE),
    		new SyntaxItem("HTML <% .. %>", 		IDjangoPrefs.HTMLSCRIPTLET_COLOR, IDjangoPrefs.HTMLSCRIPTLET_STYLE),
    		new SyntaxItem("HTML <script>", 		IDjangoPrefs.HTMLSCRIPT_COLOR, IDjangoPrefs.HTMLSCRIPT_STYLE),

    		new SyntaxItem("JavaScript Keyword",	IDjangoPrefs.JSKEYWORD_COLOR, IDjangoPrefs.JSKEYWORD_STYLE),
    		new SyntaxItem("JavaScript Number", 	IDjangoPrefs.JSNUMBER_COLOR, IDjangoPrefs.JSNUMBER_STYLE),
    		new SyntaxItem("JavaScript String", 	IDjangoPrefs.JSSTRING_COLOR, IDjangoPrefs.JSSTRING_STYLE),
    		new SyntaxItem("JavaScript Comment", 	IDjangoPrefs.JSCOMMENT_COLOR, IDjangoPrefs.JSCOMMENT_STYLE),

    		new SyntaxItem("CSS Selector", 			IDjangoPrefs.CSSSELECTOR_COLOR, IDjangoPrefs.CSSSELECTOR_STYLE),
    		new SyntaxItem("CSS Property", 			IDjangoPrefs.CSSPROP_COLOR, IDjangoPrefs.CSSPROP_STYLE),
    		new SyntaxItem("CSS Value", 			IDjangoPrefs.CSSVALUE_COLOR, IDjangoPrefs.CSSVALUE_STYLE),
    		new SyntaxItem("CSS Comments", 			IDjangoPrefs.CSSCOMMENT_COLOR, IDjangoPrefs.CSSCOMMENT_STYLE)
    ));

}
