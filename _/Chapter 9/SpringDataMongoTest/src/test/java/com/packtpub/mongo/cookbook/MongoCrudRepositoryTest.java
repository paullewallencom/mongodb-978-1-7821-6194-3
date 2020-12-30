/**
 *
 */
package com.packtpub.mongo.cookbook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.packtpub.mongo.cookbook.domain.Gender;
import com.packtpub.mongo.cookbook.domain.Person;


/**
 * @author Amol
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:MongoSpringDataTests-context.xml")
public class MongoCrudRepositoryTest extends AbstractMongoTestCase{

	@Autowired
	private PersonRepository personRepository;


	/**
	 * Saves a person using the repository and then queries, asserts the {@link Person} instance
	 *
	 */
	@Test
	public void saveAndQueryPerson() {
		savePerson();
		Person person= personRepository.findByLastName("Johnson");
		assertNotNull(person);
		assertEquals("Steve", person.getFirstName());
		assertCommonValues(person);
	}

	/**
	 *Finds a saved {@link Person} instance and update its first name, queries again and asserts the values
	 *
	 */
	@Test
	public void updateFirstName() {
		savePerson();
		Person person= personRepository.findByLastName("Johnson");
		assertNotNull(person);
		assertEquals("Steve", person.getFirstName());
		person.setFirstName("Mike");
		personRepository.save(person);
		person = personRepository.findByLastName("Johnson");
		assertEquals("Mike", person.getFirstName());
		assertCommonValues(person);


	}


	/**
	 * Test case that adds a person, uses the delete method provided by the repository and then
	 * asserts if the {@link Person} instance is indeed deleted.
	 *
	 */
	@Test
	public void removeById() {
		savePerson();
		Person person = personRepository.findByLastName("Johnson");
		assertNotNull(person);
		personRepository.delete(1);
		person = personRepository.findByLastName("Johnson");
		assertNull(person);
	}

	/**
	 * Saves a {@link Person} instance in the collection
	 */
	private void savePerson() {
		Person person = new Person();
		person.setId(1);
		person.setAge(20);
		person.setFirstName("Steve");
		person.setLastName("Johnson");
		person.setGender(Gender.Male);
		personRepository.save(person);
	}
}
