/**
 *
 */
package com.packtpub.mongo.cookbook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

/**
 * @author Amol
 *
 */
public class MongoDriverQueryAndInsertTest extends AbstractMongoTest {


	@Test
	public void getDocumentsFromTestCollection() {
		DBCollection collection = getJavaTestCollection();
		addTestDataToCollection(collection);
		DBCursor cursor = collection.find();
		assertNotNull("Expected the cursor to be not null, found null", cursor);
		assertEquals("Unexpected number of documents in the collection. ",
				5, cursor.size());
		cursor.close();
	}

	@Test
	public void findOneDocument() {
		DBCollection collection = getJavaTestCollection();
		addTestDataToCollection(collection);
		DBObject result = collection.findOne(new BasicDBObject("_id", 3));
		assertNotNull("Query needs to return a non null document, found result as null", result);
		Object id = result.get("_id");
		assertTrue("Unexpected document content found, need the _id to be integer with value 3",
				id instanceof Integer && ((Integer)id).intValue() == 3);
	}


	@Test
	public void withQueryProjectionAndSort() {
		DBCollection collection = getJavaTestCollection();
		addTestDataToCollection(collection);
		DBCursor cursor = collection
					.find(null, new BasicDBObject("value", 1).append("_id", 0))
					.sort(new BasicDBObject("_id", 1));
		assertProjectionAndSortResult(cursor);
		cursor.close();
	}



	@Test
	public void withLimitAndSkip() {
		DBCollection collection = getJavaTestCollection();
		addTestDataToCollection(collection);
		DBCursor cursor = collection
					.find(null)
					.sort(new BasicDBObject("_id", -1))
					.limit(2)
					.skip(1);
		assertNotNull("Cursor is null", cursor);
		assertEquals("Unexpected number if documents in the result", 2, cursor.size());
		DBObject next = cursor.next();
		assertEquals(next.get("_id"), Integer.valueOf(4));
		assertEquals("Unexpected id value, results not sorted",
				cursor.next().get("_id"),
				Integer.valueOf(3));
		cursor.close();
	}

	/**
	 *
	 */
	@Test
	public void insertDataTest() {
		DBCollection collection = getJavaTestCollection();
		collection.drop();
		collection.insert(new BasicDBObject("value", "Hello World"));
		long docCount = collection.count();
		assertEquals("Unexpected number of documents, ", 1, docCount);
		DBObject oneDocument = collection.findOne();
		assertTrue("Expected a value field in the document", oneDocument.containsField("value"));
		assertEquals("Unexpected value for 'value' field", "Hello World", oneDocument.get("value"));
	}

	/**
	 *
	 */
	@Test
	public void insertTestDataWithWriteConcern() {
		DBCollection collection = getJavaTestCollection();
		collection.drop();
		WriteResult wr = collection.insert(new BasicDBObject("value", "Hello World"), WriteConcern.JOURNALED);
		assertNotNull(wr);
		assertTrue("Write Concern for {j:true} expected ", wr.getLastConcern().getJ());
		long docCount = collection.count();
		assertEquals("Unexpected number of documents, ", 1, docCount);
		DBObject oneDocument = collection.findOne();
		assertTrue("Expected a value field in the document", oneDocument.containsField("value"));
		assertEquals("Unexpected value for 'value' field", "Hello World", oneDocument.get("value"));
	}


	/**
	 *
	 * @param cursor
	 */
	private void assertProjectionAndSortResult(DBCursor cursor) {
		assertNotNull("Cursor is null", cursor);
		assertEquals("Unexpected number if documents in the result", 5, cursor.count());
		DBObject first = cursor.next();
		assertTrue("Projection expected with just one field 'value' selected ", first.keySet().size() == 1);
		assertTrue("Expected field 'value' not found in the result document", first.containsField("value"));
		String resultString = (String)first.get("value");
		while(cursor.hasNext()) {
			resultString += (String)cursor.next().get("value");
		}
		assertEquals("Hello, How are you doing?", resultString);
	}
}
