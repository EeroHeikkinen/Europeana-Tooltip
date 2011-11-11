package org.springframework.beans.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.util.Map;

import org.springframework.util.CollectionUtils;
import org.springframework.xml.xpath.XPathExpressionFactory;

/**
 * Property editor for XPathExpressions
 * 
 * @author Eero Heikkinen
 * @since 08.02.2011
 */
public class XPathExpressionEditor extends PropertyEditorSupport {
    private Map<String, String> namespaces;

    /** Sets the namespaces for the expressions. The given properties binds string prefixes to string namespaces. */
    public void setNamespaces(Map<String, String> namespaces) {
        this.namespaces = namespaces;
    }
    
	@Override
	public void setAsText(String text) {
        if (CollectionUtils.isEmpty(namespaces)) {
            setValue(XPathExpressionFactory.createXPathExpression(text));
        }
        else {
            setValue(XPathExpressionFactory.createXPathExpression(text, namespaces));
        }
	}

	@Override
	public String getAsText() {
		return getValue().toString();
	}
}
