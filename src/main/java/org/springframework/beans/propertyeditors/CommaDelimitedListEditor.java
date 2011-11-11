package org.springframework.beans.propertyeditors;

import java.util.Arrays;
import java.util.List;

import org.springframework.util.StringUtils;

/**
 * Property editor for comma delimited lists of <String>
 * 
 * @author Eero Heikkinen
 * @since 08.02.2011
 */
public class CommaDelimitedListEditor extends CustomCollectionEditor {
	CommaDelimitedListEditor()
	{
		super(List.class);
	}
	
	@Override
	public void setAsText(String text)
	{
		String[] array = StringUtils.commaDelimitedListToStringArray(text);
		array = StringUtils.trimArrayElements(array);
		setValue(Arrays.asList(array));
	}
}
