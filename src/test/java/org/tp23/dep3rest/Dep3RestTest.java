package org.tp23.dep3rest;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Test using nodetoy
 * http://github.com/teknopaul/nodetoy
 */
public class Dep3RestTest {

	@Test
	public void testRest() throws IOException {
		Dep3Rest<String, String> dep3Rest = new Dep3Rest<>("http://localhost:8023");
		String jsonBack = dep3Rest.post("/data/test.json", "{}");
		Assert.assertEquals("{\"ok\" : true}", jsonBack);

		dep3Rest.reset();
		jsonBack = dep3Rest.get("/data/test.json");
		Assert.assertEquals("{}", jsonBack);
	}
}
