/**
 *
 */
package org.kacprzak.eclipse.django_editor.templates;

import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.texteditor.templates.TemplatePreferencePage;
import org.kacprzak.eclipse.django_editor.DjangoActivator;

/**
 * @author Zbigniew Kacprzak
 */
public class DjangoTemplatesPreferencesPage extends TemplatePreferencePage implements IWorkbenchPreferencePage {

	public DjangoTemplatesPreferencesPage() {
		setDescription("Templates for Django Editor");
		setPreferenceStore(DjangoActivator.getDefault().getPreferenceStore());
		setTemplateStore(TemplateManager.getDjangoTemplateStore());
		setContextTypeRegistry(TemplateManager.getDjangoContextTypeRegistry());
	}

    protected boolean isShowFormatterSetting() {
        return false;
    }

    @SuppressWarnings("deprecation")
	public boolean performOk() {
        boolean ok = super.performOk();
        DjangoActivator.getDefault().savePluginPreferences();
        return ok;
    }

}
