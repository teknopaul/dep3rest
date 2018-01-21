package org.tp23.dep3rest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Example using json-simple which accepts abstract JSON.  THere is a dependency on the json-simple tool,
 * which is pretty stable, an no dependency on domain objects.  Of course if things change you will not know about
 * it until the JSON processing code fails.
 *
 * @author teknopaul
 */
public class JsonSimpleRest<I, O> extends Dep3Rest<I, O> {

	public JsonSimpleRest(String urlBase) {
		super(urlBase);
	}

	@Override
	protected void write(HttpURLConnection con, I json) throws IOException {
		JSONObject jsonString = (JSONObject)json;
		byte[] utf8 = jsonString.toJSONString().getBytes("UTF-8");
		con.setRequestProperty("Content-Length", String.valueOf(utf8.length));
		con.getOutputStream().write(utf8);
		con.getOutputStream().close();
	}

	@Override
	protected O read(HttpURLConnection con) throws IOException {
		try {
			JSONParser parser = new JSONParser();
			return (O) parser.parse(new InputStreamReader(con.getInputStream()));
		}
		catch (ParseException pe) {
			throw new IOException(pe);
		}
	}
}
