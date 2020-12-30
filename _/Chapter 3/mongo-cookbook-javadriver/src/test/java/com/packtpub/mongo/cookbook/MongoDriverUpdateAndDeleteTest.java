/**
 *
 */
package com.packtpub.mongo.cookbook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

/**
 * @author Amol
 *
 */
public class MongoDriverUpdateAndDeleteTest extends AbstractMongoTest {

	/**
	 *
	 */
	@Test
	public void basicUpdateTest() {
		DBCollection collection = getJavaTestCollection();
		setupUpdateTestData(collection);
		WriteResult wr = collection.update(new BasicDBObject("i",
				new BasicDBObject("$gt", 10)),
				new BasicDBObject("$set", new BasicDBObject("gtTen", true)));
		assertTrue("Did not update one document, expected only one update", wr.getN() == 1);
		DBObject query = QueryBuilder.start("gtTen").exists(true).get();
		assertEquals("gtTen key not found in expected document", 1, collection.count(query));
	}

	/**
	 *
	 */
	@Test
	public void multiUpdateTest() {
		DBCollection collection = getJavaTestCollection();
		setupUpdateTestData(collection);
		WriteResult wr = collection.updateMulti(new BasicDBObject("i",
						new BasicDBObject("$gt", 10)),
							new BasicDBObject("$set", new BasicDBObject("gtTen", true)));
//		Above update is same as the following
//		WriteResult wr = collection.update(new BasicDBObject("i",
//		new BasicDBObject("$gt", 10)),
//		new BasicDBObject("$set", new BasicDBObject("gtTen", true)),
//		false,
//		true);
		assertEquals("Unexpected number of documents updated", 10, wr.getN());
		DBObject query = QueryBuilder.start("gtTen").exists(true).get();
		assertEquals("gtTen key not found", 10, collection.count(query));
	}

	/**
	 *
	 */
	@Test
	public void deleteTest() {
		DBCollection collection = getJavaTestCollection();
		setupUpdateTestData(collection);
		WriteResult wr = collection.remove(new BasicDBObject("i",
				new BasicDBObject("$gt", 10)), WriteConcern.JOURNALED);
		assertEquals("Delete not executed successfully, incorrect number deleted", 10, wr.getN());
		assertTrue("Expected the write concern with {j:true}", wr.getLastConcern().getJ());
	}

	/**
	 *
	 */
	@Test
	public void findAndModifyTest() {
		DBCollection collection = getJavaTestCollection();
		setupUpdateTestData(collection);
		DBObject old = collection.findAndModify(new BasicDBObject("i", 10),
				new BasicDBObject("i", 100));
		assertEquals(10, old.get("i"));
		assertEquals("No document with value of i = 10 expected", 0, collection.count(new BasicDBObject("i", 10)));
		assertEquals("Document with i = 100 not found", 1, collection.count(new BasicDBObject("i", 100)));
	}


	/**
	 * Sample Query builder for a query where i > 10 and i < 15
	 */
	@Test
	public void queryBuilderSample() {
		DBCollection collection = getJavaTestCollection();
		setupUpdateTestData(collection);
		DBObject query = QueryBuilder.start("i")
				.greaterThan(10)
				.and("i")
				.lessThan(15)
				.get();

//		Following is the ugly way to write the same query
//		DBObject query = new BasicDBObject("$and",
//				new BasicDBObject[] {
//					new BasicDBObject("i", new BasicDBObject("$gt", 10)),
//					new BasicDBObject("i", new BasicDBObject("$lt", 15))
//		});
		assertEquals(4, collection.count(query));
	}

}
