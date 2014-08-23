package uk.co.eelpieconsulting.mysociety.twfy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import uk.co.eelpieconsulting.mysociety.twfy.TWFYClient;

public class TWFYClientIT {
	
	private static final String CENTRE_LON = "centre_lon";
	private static final String CENTRE_LAT = "centre_lat";
	
	private TWFYClient twfyClient;
	
	@Before
	public void setup() throws IOException {
		twfyClient = new TWFYClient(loadApiKey());
	}
	
	@Test
	public void canFetchConstituencyNames() throws Exception {
		final List<String> constituencyNames = twfyClient.getConstituencies();
		assertEquals(650, constituencyNames.size());
		assertTrue(constituencyNames.contains("Twickenham"));
		assertTrue("Could not find Ynys Môn in list of consituency names; possible character encoding issue?", constituencyNames.contains("Ynys Môn"));
	}
	
	@Test
	public void canFetchConstiuencyGeometry() throws Exception {
		Map<String, Object> geometry = twfyClient.getGeometry("Twickenham");
		assertEquals(51.4292224342, geometry.get(CENTRE_LAT));
		assertEquals(-0.344722625583, geometry.get(CENTRE_LON));
	}
	
	@Test
	public void canFetchConstiuencyGeometryEvenWhenNameContainsSpecialCharacters() throws Exception {
		Map<String, Object> geometry = twfyClient.getGeometry("Ynys Môn");
		assertNotNull(geometry.get(CENTRE_LAT));
	}

	private String loadApiKey() throws IOException {
		Properties properties = new Properties();
		properties.load(this.getClass().getClassLoader().getResourceAsStream("twfy.properties"));
		return properties.getProperty("apikey");
	}
	
}
