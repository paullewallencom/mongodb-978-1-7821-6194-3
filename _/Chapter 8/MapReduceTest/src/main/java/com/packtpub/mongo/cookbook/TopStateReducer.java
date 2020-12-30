/**
 *
 */
package com.packtpub.mongo.cookbook;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import com.mongodb.hadoop.io.BSONWritable;

/**
 * @author Amol
 *
 */
public class TopStateReducer extends Reducer<Text, IntWritable, Text, BSONWritable> {

	/**
	 *
	 */
	@Override
	protected void reduce(Text text, Iterable<IntWritable> values, Context context)
			throws IOException, InterruptedException {
		int sum = 0;
		for(IntWritable value : values) {
			sum += value.get();
		}
		BSONObject object = new BasicBSONObject();
		object.put("count", sum);
		context.write(text, new BSONWritable(object));
	}
}
