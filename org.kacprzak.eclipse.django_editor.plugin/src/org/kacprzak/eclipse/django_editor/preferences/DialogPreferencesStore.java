/**
 *
 */
package org.kacprzak.eclipse.django_editor.preferences;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.resource.StringConverter;

/**
 * Preferences used on preferences dialog to keep temporary changes
 * @author Zbigniew Kacprzak
 */
public class DialogPreferencesStore extends PreferenceStore {

	private class ChangedPref {
		String keyName;
		String newValue;
		String oldValue;
		ChangedPref(String key, String niu, String old) {
			this.keyName = key;
			this.newValue = niu;
			this.oldValue = old;
		}
	}

	private HashMap<String, ChangedPref> changedPrefs = new HashMap<String, ChangedPref>();

	public DialogPreferencesStore() {
	}

	public DialogPreferencesStore(String filename) {
		super(filename);
	}

	public void initFrom(IPreferenceStore from) {
		for (String name: IDjangoPrefs.COLOR_NAMES) {
			this.setDefault(name, from.getString(name));
		}
		for (String name: IDjangoPrefs.STYLE_NAMES) {
			this.setDefault(name, from.getString(name));
		}
	}

	public void copyTo(IPreferenceStore dest) {
		for (String name: IDjangoPrefs.COLOR_NAMES) {
			dest.setValue(name, this.getString(name));
		}
		for (String name: IDjangoPrefs.STYLE_NAMES) {
			dest.setValue(name, this.getString(name));
		}
	}

	public void setKey(String key, int value) {
		setKey(key, StringConverter.asString(value));
	}
	public void setKey(String key, String value) {
		String o = getString(key);
		setValue(key, value);
		changedPrefs.put(key, new ChangedPref(key, value, o));
	}


	public void fireChangeEvents() {
		fireChangeEvents(this);
	}

	public void fireChangeEvents(IPreferenceStore store) {
		Iterator<String> it = changedPrefs.keySet().iterator();
		while ( it.hasNext() ) {
			String name = it.next();
			ChangedPref p = changedPrefs.get(name);
			store.firePropertyChangeEvent(name, p.oldValue, p.newValue);
		}
	}
}
