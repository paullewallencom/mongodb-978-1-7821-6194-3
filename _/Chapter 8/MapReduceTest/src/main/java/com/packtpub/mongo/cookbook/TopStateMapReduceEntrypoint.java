/**
 *
 */
package com.packtpub.mongo.cookbook;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.ToolRunner;

import com.mongodb.hadoop.MongoConfig;
import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.io.BSONWritable;
import com.mongodb.hadoop.util.MongoTool;

/**
 * @author Amol
 *
 */
public class TopStateMapReduceEntrypoint extends MongoTool{


	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		MongoConfig config = new MongoConfig(conf);
		config.setInputFormat(MongoInputFormat.class);
		config.setMapperOutputKey(Text.class);
		config.setMapperOutputValue(IntWritable.class);
		config.setMapper(TopStatesMapper.class);

		config.setOutputFormat(MongoOutputFormat.class);
		config.setOutputKey(Text.class);
		config.setOutputValue(BSONWritable.class);
		config.setReducer(TopStateReducer.class);

		ToolRunner.run(conf, new TopStateMapReduceEntrypoint(), args);
	}
}
