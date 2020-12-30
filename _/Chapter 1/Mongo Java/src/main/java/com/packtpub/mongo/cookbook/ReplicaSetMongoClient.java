package com.packtpub.mongo.cookbook;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import java.util.Arrays;

/**
 *
 */
public class ReplicaSetMongoClient {

    /**
     * Main method for the test client connecting to the replica set.
     * @param args
     */
    public static final void main(String[] args) throws Exception {
        MongoClient client = new MongoClient(
                Arrays.asList(
                        new ServerAddress("localhost", 27000),
                        new ServerAddress("localhost", 27001),
                        new ServerAddress("localhost", 27002)
                )
        );
        DB testDB = client.getDB("test");
        System.out.println("Dropping replTest collection");
        DBCollection collection = testDB.getCollection("replTest");
        collection.drop();
        DBObject object = new BasicDBObject("_id", 1).append("value", "abc");
        System.out.println("Adding a test document to replica set");
        collection.insert(object);
        System.out.println("Retrieving document from the collection, this one comes from primary node");
        DBObject doc = collection.findOne();
        showDocumentDetails(doc);
        System.out.println("Now Retrieving documents in a loop from the collection.");
        System.out.println("Stop the primary instance after few iterations");
        for(int i = 0 ; i < 10; i++) {
            try {
                doc = collection.findOne();
                showDocumentDetails(doc);
            } catch (Exception e) {
                //Ignoring or log a message
            }
            Thread.sleep(5000);
        }
    }

    /**
     *
     * @param obj
     */
    private static void showDocumentDetails(DBObject obj) {
        System.out.printf("_id: %d, value is %s\n", obj.get("_id"), obj.get("value"));
    }
}
