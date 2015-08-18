package com.genie.utils;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.genie.enums.ResourceBundles;
import com.genie.pojo.ResourceImageWrapper;

/**
 * @author ccubukcu
 *
 */
public class ResourceUtil {

	public static String getMessage(String messageKey, Object... params) {
		Locale locale = JsfUtil.getCurrentJSFViewLocale();
		if (locale == null) {
			locale = new Locale("en", "US");
		}
		
		String bundleName = ResourceBundles.MESSAGES.getBundleName();
		
		if(params == null || params.length == 0) {
			return getI18NResource(bundleName, messageKey, locale);
		} else {
			return getI18NResource(bundleName, messageKey, locale, params);
		}
	}	
	
	public static String getLabel(String labelKey, Object... params) {
		Locale locale = JsfUtil.getCurrentJSFViewLocale();
		if (locale == null) {
			locale = new Locale("en", "US");
		}
		
		String bundleName = ResourceBundles.LABELS.getBundleName();
		
		if(params == null || params.length == 0) {
			return getI18NResource(bundleName, labelKey, locale);
		} else {
			return getI18NResource(bundleName, labelKey, locale, params);
		}
	}

	private static String getI18NResource(String bundleName, String messageKey,
			Locale locale) {
		String message = "";

		ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);

		try {
			message = bundle.getString(messageKey);
		} catch (Exception e) {
			message = "#{" + messageKey + "}";
			e.printStackTrace();
		}

		return message;
	}

	private static String getI18NResource(String bundleName, String messageKey,
			Locale locale, Object... params) {
		String message = "";

		ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);

		try {
			message = MessageFormat
					.format(bundle.getString(messageKey), params);
		} catch (Exception e) {
			message = "#{" + messageKey + "}";
			e.printStackTrace();
		}

		return message;
	}
	
	public static List<ResourceImageWrapper> getAllImagesBase64EncodedFromResourceFolder(String folderPath) {
		List<ResourceImageWrapper> images = new ArrayList<ResourceImageWrapper>();
		
		File folder = getFileFromResources(folderPath);
		
		if(folder.isDirectory() && folder.listFiles() != null) {
			int id = 1;
			for (File image : folder.listFiles()) {
				ResourceImageWrapper riw = new ResourceImageWrapper();
				String encodedStr = DataFormatter.getBase64EncodedImage(image);
				
				riw.setEncodedImage(encodedStr);
				riw.setFilename(image.getName());
				riw.setId(id);
				images.add(riw);
				id++;
			}
		}
		
		return images;
	}
	
	public static File getFileFromResources(String filePath) {
		try {
	        Resource resource = new ClassPathResource(filePath);
	        return resource.getFile();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
		return null;
	}
}
