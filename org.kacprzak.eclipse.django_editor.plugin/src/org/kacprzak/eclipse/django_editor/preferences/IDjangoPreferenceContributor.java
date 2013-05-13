package org.kacprzak.eclipse.django_editor.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

public interface IDjangoPreferenceContributor {

	public void initializeDefaultPreferences(IPreferenceStore store );

}
