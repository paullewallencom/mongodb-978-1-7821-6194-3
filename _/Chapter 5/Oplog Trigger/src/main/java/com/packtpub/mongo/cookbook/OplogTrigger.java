/**
 *
 */
package com.packtpub.mongo.cookbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.types.BSONTimestamp;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.Bytes;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.QueryBuilder;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;

/**
 * Java Program that tests GridFS API
 *
 * @author Amol Nayak
 *
 */
public class OplogTrigger {

	private static final List<Integer> PORT_NUMBERS = Arrays.asList(27001, 27002, 27003);
	//Note:Using localhost works, but prefer not to use localhost and use the Host name instead.
	//This is being set for the code to work for folks who would not bother changing the host name in the code
	//And just run the provided code as is.
	private static final String HOST_NAME = "localhost";


	public static void main(String[] args) throws IOException {
		assertArguments(args);
		MongoClient client = new MongoClient(Arrays.asList(
				new ServerAddress(HOST_NAME, PORT_NUMBERS.get(0)),
				new ServerAddress(HOST_NAME, PORT_NUMBERS.get(1)),
				new ServerAddress(HOST_NAME, PORT_NUMBERS.get(2))
		));
		//To Suppress those error written to console causing noise.
		System.setErr(new PrintStream(new ByteArrayOutputStream()));
		if(!isMongoAvailable(client)) {
			System.out.println("Mongo Server should be listening to ports "
						+ PORT_NUMBERS + " on localhosts for the test");
			System.exit(1);
		}
		System.out.println("Connected successfully..");
		List<String> eligibleCollections = getEligibleCollections(args);
		DB db = client.getDB("local");
		DBCollection collection = db.getCollection("oplog.rs");
		collection.setReadPreference(ReadPreference.primary());
		BSONTimestamp lastreadTimestamp = getLastTSFromOplog(collection);
		System.out.println("Starting tailing oplog...");
		while(true) {
			DBCursor cursor = collection.find(QueryBuilder.start("ts").greaterThan(lastreadTimestamp).get())
					.addOption(Bytes.QUERYOPTION_TAILABLE)
					.addOption(Bytes.QUERYOPTION_AWAITDATA);
			while(cursor.hasNext()) {
				DBObject object = cursor.next();
				lastreadTimestamp = (BSONTimestamp)object.get("ts");
				String namespace = (String)object.get("ns");
				if(eligibleCollections.contains(namespace)) {
					//beware of spawning of threads to handle the notification as the order would then
					//not be guaranteed
					handleNotification(object);
				}
			}
			//Might want to sleep for a moment before we continue to query again
		}
	}


	/**
	 *
	 * @param object
	 */
	private static void handleNotification(DBObject object) {
		String operation = (String)object.get("op");
		ObjectId objectId = null;
		String operationString = null;
		if("u".equals(operation)) {
			DBObject updated = (DBObject)object.get("o2");
			objectId = (ObjectId)updated.get("_id");
			operationString = "Update";
		}
		else if("d".equals(operation) || "i".equals(operation)) {
			DBObject insertedOrDeleted = (DBObject)object.get("o");
			objectId = (ObjectId)insertedOrDeleted.get("_id");
			operationString = "i".equals(operation) ? "Insert" : "Delete";
		}
		//Actual notification to be done here which notifies the Operation as a String
		//and the ObjectId of the document thar got updated, removed or inserted
		System.out.println("Operation is " + operationString + " ObjectId is " + objectId);
	}

	/**
	 *
	 * @param args
	 * @return
	 */
	private static List<String> getEligibleCollections(String[] args) {
		String collectionNames = args[0];
		String[] collections = collectionNames.split(",");
		List<String> eligibleCollections = new ArrayList<String>();
		for(String colName : collections) {
			eligibleCollections.add(colName.trim());
		}
		return eligibleCollections;
	}

	/**
	 *
	 * @param args
	 */
	private static void assertArguments(String[] args) {
		if(args.length != 1) {
			System.out.println("One argument expected, provide comma separated, "
					+ "fully qualified collections to be monitored");
			System.exit(1);
		}
	}


	/**
	 * Attempts to read the last (or near about) from the oplog just to avoid the whole oplog
	 * being replayed
	 *
	 * @param collection
	 * @return
	 */
	private static BSONTimestamp getLastTSFromOplog(DBCollection collection) {
		DBCursor cursor = collection.find().sort(new BasicDBObject("$natural", -1)).limit(1);
		int current = (int) (System.currentTimeMillis() / 1000);
		return cursor.hasNext() ? (BSONTimestamp)cursor.next().get("ts") : new BSONTimestamp(current, 1);
	}
	/**
	 * Checks if the server is running by invoking getDatabaseNames which would fail eventually if server not
	 * available
	 *
	 * @param client
	 * @return
	 */
	public static boolean isMongoAvailable(MongoClient client) {
		try {
			client.getDatabaseNames();
		} catch (MongoException.Network e) {
			return false;
		}
		return true;
	}
}
