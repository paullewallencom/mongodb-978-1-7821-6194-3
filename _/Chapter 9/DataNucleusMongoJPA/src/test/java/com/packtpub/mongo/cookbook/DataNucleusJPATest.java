/**
 *
 */
package com.packtpub.mongo.cookbook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.packtpub.mongo.cookbook.domain.Gender;
import com.packtpub.mongo.cookbook.domain.Person;
import com.packtpub.mongo.cookbook.domain.ResidentialAddress;

/**
 * @author Amol
 *
 */
public class DataNucleusJPATest {

	private EntityManagerFactory emf;

	@Before
	public void setup() throws IOException {
		emf = Persistence.createEntityManagerFactory("DataNucleusMongo");
		createTestData();
	}


	/**
	 * Creates the data for testing, we persist using JPA
	 * Expects the server to be up and running, listening to port 27017
	 */
	public void createTestData() throws IOException {
		MongoClient client = new MongoClient("localhost:27017");
		client.getDB("test").getCollection("personJPA").drop();
		client.close();

		EntityManager em = emf.createEntityManager();
		Person person = new Person();
		person.setId(1l);
		person.setFirstName("Steve");
		person.setLastName("Johnson");
		person.setAge(30);
		person.setGender(Gender.Male);
		ResidentialAddress addr = new ResidentialAddress();
		person.setResidentialAddress(addr);
		addr.setAddressLineOne("20, Central street");
		addr.setCity("Sydney");
		addr.setState("NSW");
		addr.setCountry("Australia");
		em.persist(person);

		person = new Person();
		person.setId(2l);
		person.setFirstName("Amit");
		person.setLastName("Sharma");
		person.setAge(25);
		person.setGender(Gender.Male);
		addr = new ResidentialAddress();
		person.setResidentialAddress(addr);
		addr.setAddressLineOne("20, Central street");
		addr.setCity("Mumbai");
		addr.setState("Maharashtra");
		addr.setCountry("India");
		addr.setZipCode("400101");
		em.persist(person);

		person = new Person();
		person.setId(3l);
		person.setFirstName("Neha");
		person.setLastName("Sharma");
		person.setAge(27);
		person.setGender(Gender.Female);
		addr = new ResidentialAddress();
		person.setResidentialAddress(addr);
		addr.setAddressLineOne("20, Central street");
		addr.setCity("Mumbai");
		addr.setState("Maharashtra");
		addr.setCountry("India");
		addr.setZipCode("400001");
		em.persist(person);

		em.close();
	}

	/**
	 *
	 */
	@Test
	public void queryAustrlians() {
		EntityManager em = emf.createEntityManager();
		TypedQuery<Person> query = em.createQuery(
				"select p from Person p where p.residentialAddress.country = 'Australia'",
				Person.class);
		Person person = query.getSingleResult();
		assertNotNull(person);
		assertEquals("Steve", person.getFirstName());
	}

	/**
	 *
	 */
	@Test
	public void queryById() {
		EntityManager em = emf.createEntityManager();
		Person person = em.find(Person.class, 2l);
		assertNotNull(person);
		assertEquals("Amit", person.getFirstName());
	}
}
