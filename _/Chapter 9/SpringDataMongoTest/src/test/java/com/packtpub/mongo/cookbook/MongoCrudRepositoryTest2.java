package com.packtpub.mongo.cookbook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.MongoClient;
import com.packtpub.mongo.cookbook.domain.Address;
import com.packtpub.mongo.cookbook.domain.Gender;
import com.packtpub.mongo.cookbook.domain.Person2;

/**
 *
 * @author Amol
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:MongoSpringDataTests-context.xml")
public class MongoCrudRepositoryTest2 {


	@Autowired
	private PersonRepositoryTwo repository;

	@Autowired
	private MongoTemplate template;

	@Before
	public void setup() throws IOException {
		MongoClient client = new MongoClient("localhost:27017");
		client.getDB("test").getCollection("personTwo").drop();
		client.close();
		prepareTestData();
	}

	/**
	 * Dummy data setup
	 */
	private void prepareTestData() {
		Person2 p = new Person2(1,
				"Steve",
				"Johnson",
				30,
				Gender.Male,
				new Address("20, Central street",
						null,
						"Sydney",
						"NSW",
						"Australia",
						null));
		template.save(p);

		p = new Person2(2,
				"Amit",
				"Sharma",
				25,
				Gender.Male,
				new Address("20, Central street",
						null,
						"Mumbai",
						"Maharashtra",
						"India",
						"400101"));
		template.save(p);

		p = new Person2(3,
				"Neha",
				"Sharma",
				27,
				Gender.Female,
				new Address("20, Central street",
						null,
						"Mumbai",
						"Maharashtra",
						"India",
						"400001"));
		template.save(p);


	}

	/**
	 * Finds the people by a country
	 */
	@Test
	public void findByCountry() {
		Collection<Person2> indians = repository.findByResidentialAddressCountry("India");
		assertNotNull(indians);
		assertEquals(2, indians.size());
	}

	/**
	 * Finds the people by a country
	 */
	@Test
	public void findByCountry2() {
		Collection<Person2> indians = repository.findByResidentialAddress_country("India");
		assertNotNull(indians);
		assertEquals(2, indians.size());
	}

	/**
	 *
	 */
	@Test
	public void findByAgeBetween() {
		Collection<Person2> matchedResults = repository.findByAgeBetween(26, 40);
		assertNotNull(matchedResults);
		assertEquals(2, matchedResults.size());

	}

	/**
	 *
	 */
	@Test
	public void findByAgeSorted() {
		Collection<Person2> matchedResults = repository.findByAgeGreaterThan(20, new Sort(Sort.Direction.ASC, "age"));
		assertNotNull(matchedResults);
		assertEquals(3, matchedResults.size());
		Iterator<Person2> iter = matchedResults.iterator();
		assertEquals(25, iter.next().getAge().intValue());
		assertEquals(27, iter.next().getAge().intValue());
		assertEquals(30, iter.next().getAge().intValue());
	}


	/**
	 *
	 */
	@Test
	public void findByAgeBetween2() {
		Collection<Person2> matchedResults = repository.findByAgeBetween(27, 30);
		assertNotNull(matchedResults);
		assertEquals(0, matchedResults.size());

	}


	/**
	 * Find all people in the database whose last name contain the characters "son"
	 */
	@Test
	public void findByNamePattern() {
		Collection<Person2> bySurame = repository.findPeopleByLastNameLike("son");
		assertNotNull(bySurame);
		assertEquals(1, bySurame.size());
		assertEquals("Johnson", bySurame.iterator().next().getLastName());
	}

	/**
	 * Find all people with age greater than or equal to 27
	 */
	@Test
	public void findByMinAge() {
		Collection<Person2> people = repository.findByAgeGreaterThanEqual(27);
		assertNotNull(people);
		assertEquals(2, people.size());
	}

	/**
	 *
	 */
	@Test
	public void findByNameAndCountry() {
		Collection<Person2> results= repository.findByFirstNameAndCountry("Steve", "Australia");
		assertNotNull(results);
		assertEquals(1, results.size());
	}
}
