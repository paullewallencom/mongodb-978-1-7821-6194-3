/**
 *
 */
package com.packtpub.mongo.cookbook;

import static org.junit.Assert.assertEquals;

import java.net.UnknownHostException;

import org.junit.Before;

import com.mongodb.MongoClient;
import com.packtpub.mongo.cookbook.domain.Gender;
import com.packtpub.mongo.cookbook.domain.Person;

/**
 * @author Amol
 *
 */
public abstract class AbstractMongoTestCase {


	/**
	 *
	 * @throws UnknownHostException
	 */
	@Before
	public void setup() throws UnknownHostException {
		MongoClient client = new MongoClient("localhost:27017");
		client.getDB("test").getCollection("person").drop();
		client.close();
	}


	/**
	 *
	 * @param person
	 */
	protected void assertCommonValues(Person person) {
		assertEquals(Gender.Male, person.getGender());
		assertEquals(1, person.getId().intValue());
		assertEquals("Johnson", person.getLastName());
	}
}
