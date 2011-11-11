package fi.nba.europeana.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.xml.xpath.NodeMapper;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.digibis.europeana4j.EuropeanaFullItem;

public class EuropeanaResultNodeMapper implements NodeMapper<EuropeanaFullItem> {

	public EuropeanaFullItem mapNode(Node node, int nodeNum) {
		HashMap<String, List<String>> fields = 
			new HashMap<String, List<String>>();
		NodeList children = node.getChildNodes();

		//Looping through each section (eg. display, control etc)
		for(int j=0; j<children.getLength(); ++j){
		  Node field = children.item(j);
		  String fieldName = field.getNodeName();
		  if(!fieldName.equals("#text"))
		  {
			  List<String> values = fields.get(fieldName);
			  if(values == null)
			  {
				  values = new ArrayList<String>();
				  fields.put(fieldName, values);
			  }
			  values.add(field.getTextContent());
		  }
		}
		
		EuropeanaFullItem result = new EuropeanaFullItem();
		result.setFields(fields);
		return result;
    }
}
