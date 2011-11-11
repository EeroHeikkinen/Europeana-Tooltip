package fi.nba.europeana.service;

import java.util.List;

import com.digibis.europeana4j.EuropeanaFullItem;

/**
 * The Interface BBPrimoSearchService 
 */
public interface EuropeanaSearchService {
	public EuropeanaFullItem getResult(String id)
		throws ServiceUnavailableException;
}