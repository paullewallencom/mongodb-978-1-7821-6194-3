/**
 *
 */
package com.packtpub.mongo.cookbook;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.bson.BSONObject;

/**
 * @author Amol
 *
 */
public class TopStatesMapper extends Mapper<Object, BSONObject, Text, IntWritable> {

	@Override
	protected void map(Object key, BSONObject value, Context context)
			throws IOException, InterruptedException {
		context.write(new Text((String)value.get("state")), new IntWritable(1));
	}
}
