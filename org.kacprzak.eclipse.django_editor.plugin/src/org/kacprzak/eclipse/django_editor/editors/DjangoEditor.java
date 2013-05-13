package org.kacprzak.eclipse.django_editor.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.DefaultRangeIndicator;
import org.kacprzak.eclipse.django_editor.DjangoActivator;
import org.kacprzak.eclipse.django_editor.Logging;

/**
 * @author Zbigniew Kacprzak
*/
public class DjangoEditor extends TextEditor implements IPropertyChangeListener {

	//IDjangoOutlinePage outlinePage;

	private ColorProvider colorProvider;
	private DjangoSourceViewerConfiguration viewerConfiguration;
	private DjangoDocumentProvider documentProvider;

	public DjangoEditor() {
		super();
		colorProvider = DjangoActivator.getDefault().getColorProvider();
		viewerConfiguration = new DjangoSourceViewerConfiguration(colorProvider);
		documentProvider = new DjangoDocumentProvider();

		setSourceViewerConfiguration(viewerConfiguration);
		setDocumentProvider(documentProvider);

		setRangeIndicator(new DefaultRangeIndicator());

		//outlinePage = new DjangoOutlinePage(this);
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException
	{
		DjangoActivator.getDefault().getPreferenceStore().addPropertyChangeListener(this);
		super.init(site, input);
	}

	@Override
	public void dispose() {
		DjangoActivator.getDefault().getPreferenceStore().removePropertyChangeListener(this);
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

	protected void update() {
		//outlinePage.update();
	}
}
