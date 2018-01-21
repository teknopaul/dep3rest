package org.tp23.dep3rest;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Dependency free REST Client.  To use this code copy paste it to your project, it would be silly to package this as a jar and
 * include it as a dependency!
 *
 * Default version expects the client to provide JSON as strings and to handle JSON as strings in the response.
 * Check subclasses for alternatives.
 *
 * Not thread safe use a new instance for each request, instance can be rec¡ycled with reset(), its not heavy enought to be worth caching or pooling
 * java.net may reuse underlying network resources in a thread safe manner.
 *
 * For usage, check the JUnit tests.
 *
 * @author teknopaul
 * Copyright 2016 LGPL
 */
public class Dep3Rest <I, O> {

	/**
	 * Maximum HTTP headers to parse, java.net APIs don't tell us how many there are
	 */
	public static final int MAX_HEADERS = 99;
	/**
	 * Copy buffer size when writing JSON strings to the stream.
	 */
	public static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
	/**
	 * Content-Type header for POSTed data.
	 */
	protected String contentType = "application/json";
	/**
	 * Base URL of the REST API, concatenated to path.
	 * e.g http://someserver:8080/myapi
	 */
	private String urlBase;

	private Map<String, String> requestHeaders;
	private Map<String, String> responseHeaders = new HashMap<>();

	public Dep3Rest(String urlBase) {
		this.urlBase = urlBase;
	}

	/**
	 * Execute a GET method.
	 */
	public O get(String path) throws IOException {
		return execute("GET", path, null);
	}
	public O post(String path, I json) throws IOException {
		return execute("POST", path, json);
	}
	public O put(String path, I json) throws IOException {
		return execute("PUT", path, json);
	}
	public O delete(String path) throws IOException {
		return execute("DELETE", path, null);
	}

	/**
	 * Get the value of a response header, if there are duplicate headers this returns the last one.
	 */
	public String getHeaders(String name) {
		return responseHeaders.get(name);
	}

	/**
	 * Add a request HTTP header, e.g. Authorization.
	 * Clearly, this only works if called before executing the request.
	 */
	public void addHeader(String name, String value) {
		if (requestHeaders == null) requestHeaders = new HashMap<>();
		requestHeaders.put(name, value);
	}

	/**
	 * Reset so this object can be reused.
	 */
	public void reset() {
		contentType = "application/json";
		requestHeaders = null;
		responseHeaders = new HashMap<>();
	}
	/**
	 * Execute the HTTP request and return a response, input and output type defined by the
	 * generics I (input) and O (output) by defauilt these are both java.lang.String of JSON.
	 * Only POST and PUT have input JSON, there is not necessarily an Output object.
	 *
	 * @param method GET POST etc
	 * @param path   /foo/myapi
	 * @param json   Should be a String unless you override read and write methods
	 *
	 * @throws IOException String message will be the HTTP response code if a response was received.
	 */
	protected O execute(String method, String path, I json) throws IOException {
		URL url = new URL(urlBase + path);

		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		if ("POST".equals(method) || "PUT".equals(method)) {
			con.setDoOutput(true);
		}
		con.setRequestMethod(method);
		con.setRequestProperty("Accept", "*/*");
		con.setRequestProperty("Content-Type", contentType);
		con.setRequestProperty("User-Agent", "dep3rest");

		if (requestHeaders != null) {
			writeHeaders(con);
		}

		debug(method + " " + urlBase + path);

		if (json != null) {
			write(con, json);
		}

		readHeaders(con);

		int respCode = con.getResponseCode();

		debug(con.getHeaderField(0));

		if (respCode >= 200 && respCode < 300) {
			return read(con);
		}
		else {
			return handleError(respCode, con);
		}

	}


	/**
	 * Probably want to overwrite this method to handle a different object, e.g. jackson or jaxb.
	 * Default expects a string of JSON.
	 */
	protected void write(HttpURLConnection con, I json) throws IOException {
		String jsonString = (String) json;
		byte[] utf8 = jsonString.getBytes("UTF-8");
		con.setRequestProperty("Content-Length", String.valueOf(utf8.length));
		con.getOutputStream().write(utf8);
		con.getOutputStream().close();
	}

	/**
	 * Probably want to overwrite this method to handle a different object, e.g. jackson or jaxb.
	 * Default expects a string of JSON in the response.
	 */
	@SuppressWarnings("unchecked")
	protected O read(HttpURLConnection con) throws IOException {
		InputStreamReader in = new InputStreamReader(con.getInputStream());
		StringWriter sw = new StringWriter();
		copy(in, sw);
		return (O) sw.toString();
	}

	protected O handleError(int respCode, HttpURLConnection con) throws IOException {
		// You might want to read the response anyway here,
		// often your O will be the wrong type so throwing an exception is easier
		throw new IOException(String.valueOf(respCode));
	}

	/**
	 * Copied from commons IOUtils, copyright Apache
	 */
	public static void copy(Reader input, Writer output) throws IOException {
		char[] buffer = new char[DEFAULT_BUFFER_SIZE];
		int n;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
	}

	private void writeHeaders(HttpURLConnection con) {
		for (String name : requestHeaders.keySet()) {
			con.setRequestProperty(name, requestHeaders.get(name));
			debug(name + ": " + requestHeaders.get(name));
		}
	}

	private void readHeaders(HttpURLConnection con) {
		for (int h = 1; h < MAX_HEADERS; h++) {
			String field = con.getHeaderField(h);
			if (field != null) {
				String name = con.getHeaderFieldKey(h);
				responseHeaders.put(name, con.getHeaderField(name));
				debug(name + ": " + con.getHeaderField(name));
			}
		}
	}

	/**
	 * Dependency free logging.
	 * Subclasses should probably override to do nothing or log with the desired logging framework.
	 */
	protected void debug(String message) {
		System.out.println(message);
	}
}
