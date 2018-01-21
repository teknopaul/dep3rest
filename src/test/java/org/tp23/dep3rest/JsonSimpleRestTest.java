package org.tp23.dep3rest;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Test using nodetoy
 */
public class JsonSimpleRestTest {

	@Test
	public void testRest() throws IOException {
		JsonSimpleRest<JSONObject, JSONObject> dep3Rest = new JsonSimpleRest<>("http://localhost:8023");
		JSONObject json = new JSONObject();
		JSONObject jsonBack = dep3Rest.post("/data/test.json", json);
		Assert.assertEquals(Boolean.TRUE, jsonBack.get("ok"));

		dep3Rest.reset();
		jsonBack = dep3Rest.get("/data/test.json");
		Assert.assertEquals("{}", jsonBack.toJSONString());
	}
}
