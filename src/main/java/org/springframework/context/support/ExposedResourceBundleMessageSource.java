package org.springframework.context.support;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class ExposedResourceBundleMessageSource extends
		ResourceBundleMessageSource {
	
	public Set<String> getKeys(String basename, Locale locale) {
		ResourceBundle bundle = getResourceBundle(basename, locale);
		return bundle.keySet();
	}
	
	public Map<String, String> getMessages(String basename, Locale locale)
	{
		ResourceBundle bundle = getResourceBundle(basename, locale);
		Map<String, String> messages = new HashMap<String, String>();
		
	    for(String key : bundle.keySet())
	    	messages.put(key, bundle.getString(key));
	    
	    return messages;
	}
}
