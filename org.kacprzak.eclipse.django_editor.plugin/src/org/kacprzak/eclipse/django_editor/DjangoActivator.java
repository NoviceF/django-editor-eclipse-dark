package org.kacprzak.eclipse.django_editor;


import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

//import org.apache.log4j.BasicConfigurator;
//import org.apache.log4j.Logger;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.kacprzak.eclipse.django_editor.editors.ColorProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 * @author Zbigniew Kacprzak
 */
public class DjangoActivator extends AbstractUIPlugin {

//	public static Logger logger = Logger.getLogger("DjangoActivator");

	// The plug-in ID
	public static final String PLUGIN_ID = "org.kacprzak.eclipse.django_editor";

	// The shared instance
	private static DjangoActivator plugin;

	//Resource bundle.
	//private ResourceBundle resourceBundle;

	//Color Provider
	private ColorProvider colorProvider;

	/**
	 * The constructor
	 */
	public DjangoActivator() {
		plugin = this;
		initLog4j();
	}

	private void initLog4j() {
//		BasicConfigurator.configure();
//		logger.debug("initLog4j");
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		colorProvider = new ColorProvider(getPreferenceStore());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		colorProvider.clear();
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static DjangoActivator getDefault() {
		return plugin;
	}

	public String getPluginId(){
		return getBundle().getSymbolicName();
	}

	public ColorProvider getColorProvider(){
		return this.colorProvider;
	}

//	/**
//	 * Returns the string from the plugin's resource bundle,
//	 * or 'key' if not found.
//	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = DjangoEditorResources.getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	protected void initializeImageRegistry(ImageRegistry registry) {
        for (String imageID : IDjangoImages.ALL_KNOWN_IMAGES) {
	        IPath path = new Path(imageID);
	        URL url = FileLocator.find(getBundle(), path, null);
	        ImageDescriptor desc = ImageDescriptor.createFromURL(url);
	        registry.put(imageID, desc);
        }
     }
}
