package org.kacprzak.eclipse.django_editor.templates;

import java.io.IOException;

import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.ui.editors.text.templates.ContributionContextTypeRegistry;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;
import org.kacprzak.eclipse.django_editor.DjangoPlugin;

/**
 * Predefined templates manager
 * @author Zbigniew Kacprzak
*/
public class TemplateManager {

	private static final String DJANGO_CUSTOM_TEMPLATES_KEY = "_django.editor.custom.templates";

    private static TemplateStore 					templatesStore = null;
    private static ContributionContextTypeRegistry	contextTypeRegistry = null;


    public static TemplateStore getDjangoTemplateStore() {
        if (templatesStore == null) {
        	templatesStore = new ContributionTemplateStore(
        							getDjangoContextTypeRegistry(),
        							DjangoPlugin.getDefault().getPreferenceStore(),
        							DJANGO_CUSTOM_TEMPLATES_KEY
        						);
            try {
            	templatesStore.load();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return templatesStore;
    }

    public static ContextTypeRegistry getDjangoContextTypeRegistry() {
        if (contextTypeRegistry == null) {
        	contextTypeRegistry = new ContributionContextTypeRegistry();
        	contextTypeRegistry.addContextType(DjangoContextType.DJANGO_CONTEXT_TYPE_TAG);
        	contextTypeRegistry.addContextType(DjangoContextType.DJANGO_CONTEXT_TYPE_FILTER);
        	contextTypeRegistry.addContextType(DjangoContextType.HTML_CONTEXT_TYPE_TAG);
        }
    	return contextTypeRegistry;
    }

	@SuppressWarnings("deprecation")
	public static void savePluginPreferences(){
		DjangoPlugin.getDefault().savePluginPreferences();
	}

}
