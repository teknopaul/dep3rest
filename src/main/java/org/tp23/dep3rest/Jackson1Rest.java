package org.tp23.dep3rest;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Extending the base class to show using jackson1, and of course introducing dependencies. Typically Jackson solutions
 * will be also dependent on the domain model.
 *
 * You will notice in the test code that doing REST with jackson ala RESTEasy means you cant have return types
 * that are potentially different objects in the way nodetoy supports. Jackson supports @class attributes,
 * but that makes the server side dependent on the client technology, we dont want that.
 *
 * There are way around this, using "type" attributes, but this class indicates that by introducing Jackson as a dependency we have
 * limited the types of JSON responses we can handle significantly.
 *
 * The clear benefit is that client code is now typesafe, if you do have a domain model changes will generate
 * compile time errors, and not usually runtime errors.
 *
 * N.B. Jackson2 code is identical, just with different imports.
 *
 * @author teknopaul
 */
public class Jackson1Rest<I, O> extends Dep3Rest<I, O> {

	private static ObjectMapper mapper = new ObjectMapper();

	private Class<O> outClass;

	public Jackson1Rest(String urlBase, Class<O> outClass) {
		super(urlBase);
		this.outClass = outClass;
	}

	@Override
	protected void write(HttpURLConnection con, I json) throws IOException {
		con.setChunkedStreamingMode(DEFAULT_BUFFER_SIZE);
		mapper.writeValue(con.getOutputStream(), json);
		con.getOutputStream().close();
	}

	@Override
	protected O read(HttpURLConnection con) throws IOException {
		return mapper.readValue(con.getInputStream(), outClass);
	}
}
