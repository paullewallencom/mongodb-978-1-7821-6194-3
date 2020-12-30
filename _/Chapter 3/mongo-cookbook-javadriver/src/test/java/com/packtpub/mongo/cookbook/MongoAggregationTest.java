/**
 *
 */
package com.packtpub.mongo.cookbook;

import static org.junit.Assert.assertEquals;

import java.net.UnknownHostException;

import org.junit.Test;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * @author Amol
 *
 */
public class MongoAggregationTest {

	@Test
	public void aggregationTest() throws UnknownHostException {
		MongoClient client = new MongoClient("localhost", 27017);
		DB db = client.getDB("test");
		DBCollection collection = db.getCollection("postalCodes");
		assertEquals("Test Data not setup", 39732, collection.count());
		AggregationOutput output = collection.aggregate(
				//{'$project':{'state':1, '_id':0}},
				new BasicDBObject("$project", new BasicDBObject("state", 1).append("_id", 0)),
				//{'$group':{'_id':'$state', 'count':{'$sum':1}}}
				new BasicDBObject("$group", new BasicDBObject("_id", "$state")
					.append("count", new BasicDBObject("$sum", 1))),
				//{'$sort':{'count':-1}}
				new BasicDBObject("$sort", new BasicDBObject("count", -1)),
				//{'$limit':5}
				new BasicDBObject("$limit", 5)
		);

		//Assert output
		String[] resultStates = {"Maharashtra", "Kerala", "Tamil Nadu", "Andhra Pradesh", "Karnataka"};
		Integer[] population = {6446, 4684, 3784, 3550, 3204};
		Iterable<DBObject> results = output.results();
		int i = 1;
		for (DBObject result : results) {
			assertEquals("Unexpected result number " + i, resultStates[i - 1], result.get("_id"));
			assertEquals("Unexpected result number " + i, population[i - 1], result.get("count"));
			i++;
		}
		assertEquals("Expected number of results not found", 6, i);
	}
}
