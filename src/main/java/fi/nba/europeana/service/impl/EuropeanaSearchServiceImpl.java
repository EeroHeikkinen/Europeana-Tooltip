package fi.nba.europeana.service.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.xml.xpath.NodeMapper;
import org.springframework.xml.xpath.XPathExpression;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.digibis.europeana4j.EuropeanaFullItem;

import fi.nba.europeana.service.EuropeanaSearchService;
import fi.nba.europeana.service.ServiceUnavailableException;

public class EuropeanaSearchServiceImpl implements EuropeanaSearchService {
	
	/*
	 * Options from config.properties
	 */ 
	private String hostname;
	private String servicePath;
	private String fullView;
	
	private String suffix;
	
	public String getFullView() {
		return fullView;
	}

	public void setFullView(String fullView) {
		this.fullView = fullView;
	}

	private List<String> fieldsToDisplay;
	private XPathExpression resultXPath;
	private XPathExpression resultCountXPath;
	
	/* 
	 * Internal
	 */
	private NodeMapper<EuropeanaFullItem> fullItemNodeMapper;
	private Comparator<String> fieldsCmp;
	private DocumentBuilder db;
	private Random rand = new Random(); 

	public void init() throws ParserConfigurationException {
 		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		this.db = dbf.newDocumentBuilder();
	}
	
	
	public Document parseURL(URL url) throws ServiceUnavailableException
	{
		try {
			synchronized (this) {
				
//				String result = urlToString(url);
//				if(result == null)
//					throw new BBPrimoServiceUnavailableException();
				
//				Long after = System.nanoTime();
//				query.setRemoteExecutionTime(Math.round((after - before)/10000)/100.00);
//				StringReader reader = new StringReader( result );
//				InputSource inputSource = new InputSource( reader );				

//				return db.parse(inputSource);
				return db.parse(url.openStream());
			}
		} catch (SAXException e) {
			throw new RuntimeException("Server returned blank response (make sure your IP has access to Primo Web Services)", e);
		} catch (IOException e) {
			// Maybe 503 (temporarily unavailable)
			throw new ServiceUnavailableException();
		}
	}
	
	// Only for debug to measure accurately response time
	// Otherwise should pass url.openStream() directly to parser
	private String urlToString(URL url)
	{
		final char[] buffer = new char[0x10000];
		StringBuilder out = new StringBuilder();
		Reader in;
		try {
			in = new InputStreamReader(url.openStream(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Broken VM: no utf-8");
		} catch (IOException e) {
			return null;
		}
		int read;
		do {
		  try {
			read = in.read(buffer, 0, buffer.length);
		} catch (IOException e) {
			return null;
		}
		  if (read>0) {
		    out.append(buffer, 0, read);
		  }
		} while (read>=0);
		return out.toString();
	}
	
	public Document getDocumentFromURL(URL url)
	{
		try {
			synchronized (this) {
				return db.parse(url.openStream());
			}
		} catch (SAXException e) {
			throw new RuntimeException(
					"Parsing response failed, possibly server returned blank response");
		} catch (IOException e) {
			throw new RuntimeException(
					"Couldn't connect to host (server down?)");
		}
	}

	public EuropeanaFullItem getResult(Document doc) {
		List<EuropeanaFullItem> results = resultXPath.evaluate(doc, fullItemNodeMapper);
		return results.get(0);
	}	
	

	public int getResultCount(Document doc) {
		return (int) resultCountXPath.evaluateAsNumber(doc);
	}
	public XPathExpression getResultXPath() {
		return resultXPath;
	}

	public void setResultXPath(XPathExpression resultXPath) {
		this.resultXPath = resultXPath;
	}
	public XPathExpression getResultCountXPath() {
		return resultCountXPath;
	}

	public void setResultCountXPath(XPathExpression resultCountXPath) {
		this.resultCountXPath = resultCountXPath;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getServicePath() {
		return servicePath;
	}

	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}


	
	public EuropeanaFullItem getResult(String id)
			throws ServiceUnavailableException {
		URL url;
		try {
			// Assume http protocol
			url = new URL("http://" + hostname + fullView + id + suffix);
		}
		catch(MalformedURLException e) {
			throw new RuntimeException("Invalid URL configuration");
		}
		Document doc = parseURL(url);
		return  getResult(doc);
	}
	
	public void setFullItemNodeMapper(NodeMapper<EuropeanaFullItem> fullItemNodeMapper) {
		this.fullItemNodeMapper = fullItemNodeMapper;
	}

	public NodeMapper<EuropeanaFullItem> getFullItemNodeMapper() {
		return fullItemNodeMapper;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getSuffix() {
		return suffix;
	}

}
