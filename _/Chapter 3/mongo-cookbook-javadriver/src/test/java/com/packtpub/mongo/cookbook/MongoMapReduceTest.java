/**
 *
 */
package com.packtpub.mongo.cookbook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;

import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * @author Amol
 *
 */
public class MongoMapReduceTest {

	@Test
	public void mapReduceTest() throws UnknownHostException {
		MongoClient client = new MongoClient("localhost", 27017);
		DB db = client.getDB("test");
		DBCollection collection = db.getCollection("postalCodes");
		assertEquals("Test Data not setup", 39732, collection.count());
		DBCollection mapReduceOp = db.getCollection("javaMROutput");
		mapReduceOp.drop();

		String mapper = "function() {emit(this.state, 1)}";
		String reducer = "function(key, values){return Array.sum(values)}";
		collection.mapReduce(mapper, reducer, "javaMROutput", null);

		//Assert output
		assertTrue("Zero records found in map reduce output collection", mapReduceOp.count() > 0);
		String[] resultStates = {"Maharashtra", "Kerala", "Tamil Nadu", "Andhra Pradesh", "Karnataka"};
		Integer[] population = {6446, 4684, 3784, 3550, 3204};
		DBCursor cursor = mapReduceOp.find().sort(new BasicDBObject("value", -1)).limit(5);
		int i = 1;
		for (DBObject result : cursor) {
			assertEquals("Unexpected result number " + i, resultStates[i - 1], result.get("_id"));
			int count = ((Number)result.get("value")).intValue();
			assertEquals("Unexpected result number " + i, population[i - 1], Integer.valueOf(count));
			i++;
		}
		assertEquals("Expected number of results not found", 6, i);
	}
}
