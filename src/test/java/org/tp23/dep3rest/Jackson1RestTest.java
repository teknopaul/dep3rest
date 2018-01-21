package org.tp23.dep3rest;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;


public class Jackson1RestTest {

	@Test
	public void testRest() throws IOException {
		Jackson1Rest<User, OK> jackson1Rest1 = new Jackson1Rest<>("http://localhost:8023", OK.class);
		OK jsonBack = jackson1Rest1.post("/data/teknopaul.json", new User("teknopaul"));
		Assert.assertEquals(jsonBack, new OK());

		Jackson1Rest<User, User> jackson1Rest2 = new Jackson1Rest<>("http://localhost:8023", User.class);
		User user = jackson1Rest2.get("/data/teknopaul.json");
		Assert.assertEquals("teknopaul", user.getName());
	}
}
