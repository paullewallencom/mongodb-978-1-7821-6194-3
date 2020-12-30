/**
 *
 */
package com.packtpub.mongo.cookbook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.packtpub.mongo.cookbook.domain.Gender;
import com.packtpub.mongo.cookbook.domain.Person;

/**
 * @author Amol
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:MongoSpringDataTests-context.xml")
public class MongoTemplateTest extends AbstractMongoTestCase {

	@Autowired
	private MongoTemplate mongoTemplate;


	@Test
	public void saveAndQueryPerson() {
		savePerson();
		Person person = findByLastName("Johnson");
		assertNotNull(person);
		assertCommonValues(person);

	}

	/**
	 * Test case that adds a person, deletes the person using template,and then
	 * asserts if the {@link Person} instance is indeed deleted.
	 *
	 */
	@Test
	public void removeById() {
		savePerson();
		Person person = findByLastName("Johnson");
		assertNotNull(person);
		mongoTemplate.remove(person);
		person = findByLastName("Johnson");
		assertNull(person);
	}

	/**
	 * Insert multiple {@link Person} instances as part of one call to the server
	 */
	@Test
	public void insertMultiple() {
		insertMultiplePeople();
		assertEquals(3, mongoTemplate.count(null, "person"));

	}

	/**
	 *
	 */
	private void insertMultiplePeople() {
		mongoTemplate.dropCollection("person");
		List<Person> people = new ArrayList<Person>();
		people.add(new Person(1,
				"Steve",
				"Johnson",
				30,
				Gender.Male
				));
		people.add(new Person(2,
				"Amit",
				"Sharma",
				25,
				Gender.Male));
		people.add(new Person(3,
				"Neha",
				"Sharma",
				27,
				Gender.Female));
		mongoTemplate.insert(people, Person.class);
	}


	/**
	 *
	 * @param lastName
	 */
	private Person findByLastName(String lastName) {
		return mongoTemplate.findOne(
				new Query(
						Criteria.where("lastName").is("Johnson")), Person.class);
	}

	/**
	 * Saves a test {@link Person} instance in the collection
	 */
	private void savePerson() {
		Person person = new Person();
		person.setId(1);
		person.setAge(20);
		person.setFirstName("Steve");
		person.setLastName("Johnson");
		person.setGender(Gender.Male);
		mongoTemplate.save(person);
	}

	/**
	 *
	 */
	@Test
	public void updatePerson() {
		savePerson();
		Person person= mongoTemplate.findOne(new Query(
					Criteria.where("firstName").is("Steve")
				),
				Person.class);
		assertNotNull(person);
		assertEquals("Steve", person.getFirstName());
		person.setFirstName("Mike");
		mongoTemplate.save(person);
		person = mongoTemplate.findOne(new Query(
				Criteria.where("lastName").is("Johnson")
				),
				Person.class);
		assertEquals("Mike", person.getFirstName());
		assertCommonValues(person);
	}

	/**
	 *
	 */
	@Test
	public void updateMultiple() {
		insertMultiplePeople();
		Query finder = new Query(Criteria.where("age").lt(30));
		Update update = new Update().set("youngCustomer", true);
		WriteResult result = mongoTemplate.updateMulti(finder, update, Person.class);
		//Amit and Neha should be updated
		assertEquals(2, result.getN());
	}

	/**
	 * Actual pipeline is
	 *
	 * 	{'$project':{'state':1, '_id':0}},
	 *	{'$group':{'_id':'$state', 'count':{'$sum':1}}},
	 *	{'$sort':{'count':-1}},
	 *	{'$limit':5}
	 *
	 */
	@Test
	public void aggregationTest() {
		Aggregation aggregation =
				newAggregation(
						project("state", "_id"),				//{'$project':{'state':1, '_id':0}},
						group("state").count().as("count"),		//{'$group':{'_id':'$state', 'count':{'$sum':1}}}
						sort(Direction.DESC, "count"),			//{'$sort':{'count':-1}},
						limit(5)								//{'$limit':5}
				);
		AggregationResults<DBObject> results = mongoTemplate.aggregate(
				aggregation,
				"postalCodes",
				DBObject.class);
		List<DBObject> resultList = results.getMappedResults();
		assertNotNull(resultList);
		assertEquals(5, resultList.size());
		//assert expected values
		String[] expectedStates = new String[] {"Maharashtra", "Kerala", "Tamil Nadu",
													"Andhra Pradesh", "Karnataka"};
		int[] expectedCounts = new int[] {6446, 4684, 3784, 3550, 3204};
		for(int i = 0; i < 5 ; i++) {
			DBObject obj = resultList.get(i);
			assertEquals(expectedStates[i], obj.get("_id"));
			assertEquals(expectedCounts[i], ((Integer)obj.get("count")).intValue());
		}
	}
}
