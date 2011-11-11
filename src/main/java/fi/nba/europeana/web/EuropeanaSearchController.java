package fi.nba.europeana.web;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ExposedResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digibis.europeana4j.*;

import fi.nba.europeana.service.EuropeanaSearchService;
import fi.nba.europeana.service.ServiceUnavailableException;

/**
 * Handles requests for the application home page.
 */
@Controller
public class EuropeanaSearchController {
	EuropeanaConnection europeana = new EuropeanaConnection ("IAVQBBDOQQ");
	Random generator = new Random();
	
	@Autowired
	private EuropeanaSearchService searchService;
	
	@Autowired
	private ExposedResourceBundleMessageSource messageSource;
	
	private static final Logger logger = LoggerFactory.getLogger(EuropeanaSearchController.class);	

	/* 
	 * Get results and render them as json
	 * Use this when you want only the results and not the facets
	 * Otherwise use getSearch
	 */
	@RequestMapping(value = "/results.json", method = RequestMethod.GET)
	public @ResponseBody EuropeanaResults getResults(@ModelAttribute("query") EuropeanaQuery query,
			@RequestParam(value="bulkSize", defaultValue="12") int bulkSize) {
		EuropeanaResults res;
		try {
			
			if(bulkSize > 120) bulkSize = 120;
			res = europeana.search (query, bulkSize);
			return res;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@RequestMapping(value = "/get.json", method = RequestMethod.GET)
	@ResponseBody
	public EuropeanaFullItem getResult(@RequestParam("id") String id,
			Model model) {
		try {
			EuropeanaFullItem result = searchService.getResult(id);
			return result;
		} catch (ServiceUnavailableException e) {
			return null;
		}
	}
	
	/* 
	 * Get random result and render as json
	 * NOTE: getting results with a high index is slow 
	 * so getting random results this way is not feasible right now.
	 */
	@RequestMapping(value = "/random.json", method = RequestMethod.GET)
	public @ResponseBody EuropeanaResults getRandomPage(
			@ModelAttribute("query") EuropeanaQuery query) {
		EuropeanaResults res;
		try {
			if(query.getWholeSubQuery() == null)
				query.setWholeSubQuery("*:*");
			res = europeana.search (query, 1);
			query.setStartPage(generator.nextInt());
			return europeana.search (query, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * Prints all messages from current locale as javascript variables. message.foo=400 generates "message=message||{} message.foo=
	 */
	@RequestMapping(value = "/localization.js", method = RequestMethod.GET)
	public ResponseEntity<String> localization(Model model, Locale locale) {	
		Map<String, String> messages = 
			messageSource.getMessages("messages", locale);
		
		StringBuilder messageStr = new StringBuilder();
		for(Entry<String, String> entry : messages.entrySet()) {
			if(messageStr.length() > 0)
				messageStr.append(',');
			messageStr
				.append('"')
				.append(entry.getKey())
				.append('"')
				.append(':')
				.append('"')
				.append(entry.getValue())
				.append('"');
		}
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/javascript; charset=utf-8");

		return new ResponseEntity<String>(
				"localization={" + messageStr + "};",
				responseHeaders, 
				HttpStatus.CREATED);
	}
}
