/**
 *
 */
package com.packtpub.mongo.cookbook;

import static org.junit.Assert.fail;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

/**
 * @author Amol
 *
 */
public abstract class AbstractMongoTest {

	/**
	 *
	 * @return
	 */
	protected MongoClient getReplicaSetClient() throws UnknownHostException {
		MongoClient client = new MongoClient(Arrays.asList(new ServerAddress("localhost", 27000)));
		return client;
	}


	/**
	 *
	 */
	protected void createReplicaSetTestData() {
		try {
			MongoClient rs = getReplicaSetClient();
			DBCollection collection = rs.getDB("test").getCollection("rsTestCollection");
			setupUpdateTestData(collection);
			Thread.sleep(10);
		} catch (Exception e) {
			fail("Caught exception with message " + e.getMessage());
		}
	}

	/**
	 *
	 * @return
	 */
	protected MongoClient getSlaveClientInstance() {
		try {
			MongoClient rs = getReplicaSetClient();
			//Allow Auto discovery if needed
			Thread.sleep(50);
			ServerAddress master = rs.getReplicaSetStatus().getMaster();
			List<ServerAddress> allHosts = rs.getServerAddressList();
			ServerAddress slaveAddress = null;
			for(ServerAddress sa : allHosts) {
				if(!sa.equals(master)) {
					slaveAddress = sa;
					break;
				}
			}
			MongoClient slave = new MongoClient(slaveAddress);
			return slave;
		} catch (Exception e) {
			fail("Caught exception with message " + e.getMessage());
			return null;
		}
	}


	/**
	 *
	 */
	protected void setupUpdateTestData(DBCollection collection) {
		collection.drop();
		List<DBObject> objects = new ArrayList<DBObject>(30);
		for(int i = 1 ; i <= 20; i ++) {
			objects.add(new BasicDBObject("i", i));
		}
		collection.insert(objects);
	}

	/**
	 * Adding test data to the collection
	 */
	protected void addTestDataToCollection(DBCollection collection) {
		collection.drop();
		String[] values = {"Hello, ", "How ", "are ", "you ", "doing?"};
		List<DBObject> objects = new ArrayList<DBObject>();
		for(int i = 1; i <= 5 ; i++) {
			DBObject obj = new BasicDBObject("_id", i).append("value", values[i - 1]);
			objects.add(obj);
		}
		collection.insert(objects);
	}


	/**
	 *
	 * @return
	 */
	protected MongoClient getClient() {
		try {
			MongoClient client = new MongoClient("localhost:27017");
			//Invoked to ensure that the client is indeed able to connect to the server
			client.getDatabaseNames();
			return client;
		}
		catch (Exception e) {
			fail("Caught Exception "+ e.getClass() +
					" with message, " + e.getMessage() +", No Exception expected");
		}
		return null;
	}

	/**
	 *
	 * @return
	 */
	protected DB getJavaDriverTestDatabase() {
		return getClient().getDB("javaDriverTest");
	}


	/**
	 *
	 * @return
	 */
	protected DBCollection getJavaTestCollection() {
		return getJavaDriverTestDatabase().getCollection("javaTest");
	}

}
