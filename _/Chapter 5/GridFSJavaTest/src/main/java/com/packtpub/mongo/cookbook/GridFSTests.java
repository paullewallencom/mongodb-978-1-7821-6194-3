/**
 *
 */
package com.packtpub.mongo.cookbook;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

/**
 * Java Program that tests GridFS API
 *
 * @author Amol Nayak
 *
 */
public class GridFSTests {

	private static final int PORT_NUMBER = 27017;
	//Note:Using localhost works, but prefer not to use localhost and use the Host name instead.
	//This is being set for the code to work for folks who would not bother changing the host name in the code
	//And just run the provided code as is.
	private static final String HOST_NAME = "localhost";


	public static void main(String[] args) throws IOException {
		assertArguments(args);
		MongoClient client = new MongoClient(HOST_NAME, PORT_NUMBER);
		//To Suppress those error written to console causing noise.
		System.setErr(new PrintStream(new ByteArrayOutputStream()));
		if(!isMongoAvailable(client)) {
			System.out.println("Mongo Server should be listening to port "
						+ PORT_NUMBER + " on localhosts for the test");
			System.exit(1);
		}

		System.out.println("Connected successfully..");
		GridFS gfs = new GridFS(client.getDB("test"));
		if("get".equals(args[0])) {
			handleGet(gfs, args);
		}
		else if("put".equals(args[0])) {
			handlePut(gfs, args);
		}
		else {
			handleDelete(gfs, args);
		}

	}

	/**
	 *
	 * @param args
	 */
	private static final void handleGet(GridFS gfs, String[] args) throws IOException {
		GridFSDBFile gridFSFile = gfs.findOne(args[2]);
		if(gridFSFile != null) {
			File file = new File(args[1]);
			boolean successful = file.createNewFile();
			if(successful) {
				//write content to file here
				byte[] content = new byte[256 * 1024];
				InputStream in = gridFSFile.getInputStream();
				FileOutputStream fos = new FileOutputStream(file);
				for(int read = in.read(content) ; read != -1 ;) {
					fos.write(content, 0, read);
					read = in.read(content);
				}
				System.out.println("Successfully written " + gridFSFile.getLength() + " bytes to " + file.getAbsolutePath());
				fos.close();
				in.close();
			}
			else {
				System.out.println("Cannot write to file " + file.getAbsolutePath());
				System.exit(1);
			}
		}
		else {
			System.out.println("File " + args[2] + " doesn't exist in GridFS");
			System.exit(1);
		}
	}

	/**
	 *
	 * @param args
	 */
	private static final void handlePut(GridFS gfs, String[] args) throws IOException {
		File file = new File(args[1]);
		assertLocalFileExistence(file, args);
		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file));
		GridFSInputFile gfsIN = gfs.createFile(bin, args[2], true);
		gfsIN.save();
		System.out.println("Successfully written to " + gfsIN.getFilename() + ", details are:");
		System.out.println("Upload Identifier: " + gfsIN.getId()
				+ "\nLength: " + gfsIN.getLength()
				+ "\nMD5 hash: " + gfsIN.getMD5()
				+ "\nChunk Side in bytes: " + gfsIN.getChunkSize()
				+ "\nTotal Number Of Chunks: " + gfsIN.numChunks());
	}

	/**
	 *
	 * @param args
	 */
	private static final void handleDelete(GridFS gfs, String[] args) throws IOException {
		gfs.remove(args[1]);
		System.out.println("Removed file with name '" + args[1] + "' from GridFS");
	}


	/**
	 *
	 * @param file
	 * @param args
	 */
	private static void assertLocalFileExistence(File file, String[] args) {
		if(!file.exists()) {
			System.out.println("Source file '" + file.getName() + "' doesnt exist");
			System.exit(1);
		}
	}


	/**
	 *
	 * @param args
	 */
	private static void assertArguments(String[] args) {
		if(args.length == 0) {
			System.out.println("One of the following arguments is expected\n\n"
					+ "get <local file name> <upload file name>\n"
					+ "put <local file name> <uploaded file name>\n"
					+ "delete <uploaded file name>");
			System.exit(1);
		}
		List<String> validArgs = Arrays.asList("get", "put", "delete");
		if(!validArgs.contains(args[0])) {
			System.out.println("Invalid operation '" + args[0] + "' provided, one of " + validArgs + " expected");
			System.exit(1);
		}
		if("get".equals(args[0]) && args.length != 3) {
			System.out.println("Expected <local filename> and <uploaded file name> only with get");
			System.exit(1);
		}
		else if ("put".equals(args[0]) && args.length != 3){
			System.out.println("Expected <local filename> and <upload file name> only with put");
			System.exit(1);
		}
		else if ("delete".equals(args[0]) && args.length != 2){
			System.out.println("Expected <uploaded file name> only with put");
			System.exit(1);
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
