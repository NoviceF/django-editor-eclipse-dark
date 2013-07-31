package org.kacprzak.eclipse.django_editor.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.DefaultRangeIndicator;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.kacprzak.eclipse.django_editor.DjangoPlugin;
import org.kacprzak.eclipse.django_editor.editors.outline.DjangoContentOutlinePage;

/**
 * @author Zbigniew Kacprzak
*/
public class DjangoEditor extends TextEditor implements IPropertyChangeListener {

	/** The outline page */
	private DjangoContentOutlinePage outlinePage;

	private ColorProvider colorProvider;
	private DjangoSourceViewerConfiguration viewerConfiguration;
	private DjangoDocumentProvider documentProvider;

	public DjangoEditor() {
		super();
		colorProvider = DjangoPlugin.getDefault().getColorProvider();
		viewerConfiguration = new DjangoSourceViewerConfiguration(colorProvider);
		documentProvider = new DjangoDocumentProvider();

		setSourceViewerConfiguration(viewerConfiguration);
		setDocumentProvider(documentProvider);

		setRangeIndicator(new DefaultRangeIndicator());
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException
	{
		DjangoPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(this);
		super.init(site, input);
	}

	@Override
	public void dispose() {
		DjangoPlugin.getDefault().getPreferenceStore().removePropertyChangeListener(this);
		if (outlinePage != null)
			outlinePage.setInput(null);
		super.dispose();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String name = event.getProperty();
//Logging.error("propertyChange: " + name + "; new=" + event.getNewValue() + "; old=" + event.getOldValue());
		if (name.endsWith("_style") || name.endsWith("_color")) {
			colorProvider.reloadColor(event.getProperty()); //all reference this cache
			viewerConfiguration.updateSyntaxColorAndStyle(); //the style needs no reloading
	        getSourceViewer().invalidateTextPresentation();
		}
	}

	public void doSetInput(IEditorInput input) throws CoreException {
		super.doSetInput(input);
		if (outlinePage != null)
			outlinePage.setInput(input);
	}

	@Override
	public void doRevertToSaved() {
		super.doRevertToSaved();
		update();
	}
	
	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		super.doSave(progressMonitor);
		update();
	}

	@Override
	public void doSaveAs() {
		super.doSaveAs();
		update();
	}

	@Override
	protected void editorSaved() {
		super.editorSaved();
		if (outlinePage != null)
			outlinePage.update();	
	}

	protected void update() {
		if (outlinePage != null)
			outlinePage.update();
	}

	@Override
	public Object getAdapter(Class required) {
		if (IContentOutlinePage.class.equals(required)) {
			if (outlinePage == null) {
				//outlinePage= new DjangoContentOutlinePage(getDocumentProvider(), this);
				//if (getEditorInput() != null)
				//	outlinePage.setInput(getEditorInput());
			}
			return outlinePage;
		}
		return super.getAdapter(required);
	}
}
