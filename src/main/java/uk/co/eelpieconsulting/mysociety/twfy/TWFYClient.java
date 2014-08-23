package uk.co.eelpieconsulting.mysociety.twfy;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.client.utils.URIBuilder;

import uk.co.eelpieconsulting.common.http.HttpBadRequestException;
import uk.co.eelpieconsulting.common.http.HttpFetchException;
import uk.co.eelpieconsulting.common.http.HttpFetcher;
import uk.co.eelpieconsulting.common.http.HttpForbiddenException;
import uk.co.eelpieconsulting.common.http.HttpNotFoundException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

public class TWFYClient {
	
	private static final String API_URL = "http://www.theyworkforyou.com/api";
	private static final String GET_CONSTITUENCIES = "/getConstituencies";
	private static final String GET_GEOMETRY = "/getGeometry";
	private static final String ISO_8859_1 = "iso-8859-1";
	private static final String KEY = "key";
	private static final String NAME = "name";
	
	private final HttpFetcher httpFetcher;
	private final ObjectMapper objectMapper;
	
	private final String apiKey;
	
	public TWFYClient(String apiKey) {
		this.httpFetcher = new HttpFetcher(ISO_8859_1);
		this.objectMapper = new ObjectMapper();
		this.apiKey = apiKey;
	}
	
	public List<String> getConstituencies() throws HttpNotFoundException, HttpBadRequestException, HttpForbiddenException, HttpFetchException, JsonProcessingException, IOException, URISyntaxException {	// TODO handle error JSON		
		URIBuilder url = setupUrlFor(GET_CONSTITUENCIES);
		
		final List<String> constituencyNames = Lists.newArrayList();	
		final Iterator<JsonNode> elements = objectMapper.readTree(httpFetcher.get(url.build())).elements();
		while(elements.hasNext()) {
			JsonNode next = elements.next();
			constituencyNames.add(next.get(NAME).asText());
		}		
		return constituencyNames;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getGeometry(String constituencyName) throws URISyntaxException, HttpNotFoundException, HttpBadRequestException, HttpForbiddenException, HttpFetchException, JsonParseException, JsonMappingException, IOException {
		URIBuilder url = setupUrlFor(GET_GEOMETRY).addParameter(NAME, constituencyName);
		
		return objectMapper.readValue(httpFetcher.get(url.build()), Map.class);
	}
	
	private URIBuilder setupUrlFor(String uri) throws URISyntaxException {
		URIBuilder url = new URIBuilder(API_URL + uri);
		url.addParameter(KEY, apiKey);
		return url;
	}

}
