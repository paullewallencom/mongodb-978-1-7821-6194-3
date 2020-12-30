/**
 *
 */
package com.packtpub.mongo.cookbook;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.WriteConcernException;
import com.mongodb.WriteResult;

/**
 * Java Program that tests various levels of {@link WriteConcern}
 *
 * @author Amol Nayak
 *
 */
public class WriteConcernTests {

	//Note:Using localhost works, but prefer not to use localhost and use the Host name instead.
	//This is being set for the code to work for folks who would not bother changing the host name in the code
	//And just run the provided code as is.
	private static final String HOST_NAME = "localhost";


	public static void main(String[] args) throws UnknownHostException {
		MongoClient client = new MongoClient(HOST_NAME, 20000);
		//To Suppress those error written to console causing noise.
		System.setErr(new PrintStream(new ByteArrayOutputStream()));
		if(isMongoAvailable(client)) {
			System.out.println("Mongo Server should not be listening to port 20000 on localhosts for initial tests");
			System.exit(1);
		}
		else {
			System.out.println("Trying to connect to server running on port 20000");
		}
		//Server is stopped if we reach here

		DB dbTest = client.getDB("test");
		DBCollection dbCollection = dbTest.getCollection("writeConcernTest");

		//---------------------------------Testing {w:-1}-------------------------------------------
		System.out.println("Trying to write data in the collection with write concern {w:-1}");
		WriteResult wr = dbCollection.insert(new BasicDBObject("Key", "Value"), WriteConcern.NONE);
		String error = (String) wr.getCachedLastError().get("$err");
		if(error != null) {
			System.out.println("Error returned in the WriteResult is " + error);
		}
		else {
			System.out.println("No error caught in the write operation, strange...");
		}


		//---------------------------------Testing {w:0}-------------------------------------------
		System.out.println("\nTrying to write data in the collection with write concern {w:0}");
		try {
			wr = dbCollection.insert(new BasicDBObject("Key", "Value"), WriteConcern.NORMAL);
		} catch (MongoException.Network e) {
			System.out.println("Caught MongoException.Network trying to write to collection, message is " + e.getMessage());
		}


		//-----------------------For subsequent tests, we will need the replica set to be up and running-------------------------
		if(!isReplicaSetCorrectlyConfigured()) {
			System.out.println("Replica set needs to be setup with one server listening to port 27000 and one of the servers"
					+ " configured with a slave delay or 5 seconds");
			System.exit(1);
		}
		client = new MongoClient(Arrays.asList(
				new ServerAddress[] { new ServerAddress(HOST_NAME, 27000)}
				)
			);
		dbTest = client.getDB("test");
		dbCollection = dbTest.getCollection("writeConcernTest");
		System.out.println("Connected to replica set with one node listening on port 27000 locally");

		//-----------------------Unique Constraint violation with {w:0}--------------------------------------
		System.out.println("\nInserting duplicate keys with {w:0}");
		//First drop to ensure the collection is clean for testing
		dbCollection.drop();
		DBObject obj = new BasicDBObject("_id", "a");
		dbCollection.insert(obj, WriteConcern.NONE);
		//Again, a duplicate
		dbCollection.insert(obj, WriteConcern.NONE);
		System.out.println("No exception caught while inserting data with duplicate _id");

		//-----------------------------------Inserting duplicate data with {w:1}------------------------------
		System.out.println("\nNow inserting the same data with {w:1}");
		try {
			dbCollection.insert(obj, WriteConcern.ACKNOWLEDGED);
		} catch (MongoException.DuplicateKey e) {
			System.out.println("Caught Duplicate Exception, exception message is " + e.getMessage());
		}

		//-----------------We will now find average write times for various types of WriteConcerns---------------
		//-------------------------Inserting data with only primary to acknowledge the write---------------------
		findAverageWriteTimes(dbCollection, WriteConcern.ACKNOWLEDGED);
		//-------------------------Inserting data with one secondary to acknowledge the write---------------------
		findAverageWriteTimes(dbCollection, new WriteConcern(2));
		//-------------------------Inserting data with and wait for the data to be written to journal---------------------
		findAverageWriteTimes(dbCollection, WriteConcern.JOURNALED);
		//-------------------------Inserting data with and wait for the data to be flushed to the disk---------------------
		findAverageWriteTimes(dbCollection, WriteConcern.FSYNCED);
		//---Inserting data with and wait for an acknowledgement from 4 secondaries, should timeout eventually---------------------
		//-------------------------Inserting data with and wait for an acknowledgement from 2 secondaries---------------------
		findAverageWriteTimes(dbCollection, new WriteConcern(3));
		try {
			findAverageWriteTimes(dbCollection, new WriteConcern(5, 1000));
		} catch (WriteConcernException e) {
			System.out.println("Caught WriteConcern exception for {w:5}, with following message " + e.getMessage());
		}
		//Uncomment the following line and the write operation will hang indefinitely till there
		//are 5 nodes in the replica set and the write operation reaches them.
		//findAverageWriteTimes(dbCollection, new WriteConcern(5));
	}

	/**
	 * Utility function that writes to a collection with the specified WriteConcern and tests the average time to
	 * write to the collection
	 *
	 */
	private static void findAverageWriteTimes(DBCollection dbCollection, WriteConcern wc) {
		DBObject obj = new BasicDBObject("key", "value");
		long totalRunningTime = 0;
		for(int i = 0; i < 5 ; i++) {
			long startTime = System.currentTimeMillis();
			obj.removeField("_id");
			dbCollection.insert(obj, wc);
			long endTime = System.currentTimeMillis();
			totalRunningTime += (endTime - startTime);
		}
		System.out.println("Average running time with WriteConcern {w:" + wc.getW()
				+ ", fsync:" + wc.getFsync() + ", j:" + wc.getJ() + "} is " + (totalRunningTime/5) + " ms");

	}

	/**
	 *
	 * @return
	 */
	private static boolean isReplicaSetCorrectlyConfigured() throws UnknownHostException {
		MongoClient client = new MongoClient(HOST_NAME, 27000);
		boolean configured = false;
		if(isMongoAvailable(client)) {
			client.setReadPreference(ReadPreference.secondaryPreferred());
			DB localDB = client.getDB("local");
			DBCollection dbCollection = localDB.getCollection("system.replset");
			DBObject conf = dbCollection.findOne();
			if(conf != null) {
				@SuppressWarnings("unchecked")
				List<DBObject> members = (List<DBObject>)conf.get("members");
				if(members.size() == 3) {
					//See if atleast one of them has slave delay
					boolean hasSlaveDelay = false;
					for(DBObject obj : members) {
						if(obj.containsField("slaveDelay")) {
							hasSlaveDelay = true;
							break;
						}
					}
					if(!hasSlaveDelay) {
						System.out.println("Expected a atleast one node in replica set to have a slaveDelay setup");
					}
					else {
						configured = true;
					}
				}
				else {
					System.out.println("Expected a 3 node replica set, found one of size " + members.size());
				}

			}
			else {
				System.out.println("Server listening to port 27000 is not a part of replica set, please read the instructions in "
					+ "the book and setup appropriately");
			}
		}
		else {
			System.out.println("No server configured to listen on port 27000, replica set "
					+ "expected to have atleast one node listening to port 27000, please read the instructions in "
					+ "the book and setup appropriately");
		}
		return configured;
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
