/**
 *
 */
package com.packtpub.mongo.cookbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

/**
 * @author Amol
 *
 */
public class BinaryDataTest {

	private static final int PORT_NUMBER = 27017;
	//Note:Using localhost works, but prefer not to use localhost and use the Host name instead.
	//This is being set for the code to work for folks who would not bother changing the host name in the code
	//And just run the provided code as is.

	private static final String HOST_NAME = "localhost";

	public static void main(String[] args) throws IOException {
		MongoClient client = new MongoClient(HOST_NAME, PORT_NUMBER);
		//To Suppress those error written to console causing noise.
		System.setErr(new PrintStream(new ByteArrayOutputStream()));
		if(!isMongoAvailable(client)) {
			System.out.println("Mongo Server should be listening to port "
						+ PORT_NUMBER + " on localhosts for the test");
			System.exit(1);
		}

		System.out.println("Connected successfully..");
		System.out.println("Writing to collection");
		DB db = client.getDB("test");
		DBCollection collection = db.getCollection("binaryDataTest");
		collection.drop();
		String resourceName = "mongodb-logo.png";
		byte[] imageBytes = readBinaryData(resourceName);
		DBObject doc = new BasicDBObject("_id", 1);
		doc.put("fileName", resourceName);
		doc.put("size", imageBytes.length);
		doc.put("data", imageBytes);
		collection.insert(doc);
		System.out.println("Successfully written binary data to collection");

	}

	private static byte[] readBinaryData(String resourceName) throws IOException {
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
		if(in != null) {
			int available = in.available();
			byte[] bytes = new byte[available];
			in.read(bytes);
			return bytes;
		}
		else {
			throw new IllegalArgumentException("Resource " + resourceName + " notr found");
		}
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
