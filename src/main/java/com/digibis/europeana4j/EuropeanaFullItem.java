package com.digibis.europeana4j;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EuropeanaFullItem {
	// Multidimensional map, section is key in first dimension
	// eg. ${fields['display']['title']} when calling from jsp gives title from display section
	private Map<String, List<String>> fields;
	
	public Map<String, List<String>> getFields()
	{
		return this.fields;
	}

	public void setFields(Map<String, List<String>> fields) {
		this.fields = fields;
	}
}