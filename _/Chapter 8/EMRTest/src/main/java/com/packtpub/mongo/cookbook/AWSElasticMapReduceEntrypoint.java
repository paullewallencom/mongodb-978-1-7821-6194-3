/**
 *
 */
package com.packtpub.mongo.cookbook;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceClient;
import com.amazonaws.services.elasticmapreduce.model.ActionOnFailure;
import com.amazonaws.services.elasticmapreduce.model.HadoopJarStepConfig;
import com.amazonaws.services.elasticmapreduce.model.JobFlowInstancesConfig;
import com.amazonaws.services.elasticmapreduce.model.RunJobFlowRequest;
import com.amazonaws.services.elasticmapreduce.model.RunJobFlowResult;
import com.amazonaws.services.elasticmapreduce.model.StepConfig;

/**
 * @author Amol
 *
 */
public class AWSElasticMapReduceEntrypoint {

	// Change the Bucket to your own here
	private static final String INPUT_S3_BUCKET = "<Your Input bucket>";
	private static final String OUTPUT_S3_BUCKET = "<Your Output bucket>";
	private static final String LOG_S3_URI = "<Your Logs bucket>";

//	Sample for the above constants is given below.
//	private static final String INPUT_S3_BUCKET = "s3://com.packtpub.mongo.cookbook.emr-in/";
//	private static final String OUTPUT_S3_BUCKET = "s3://com.packtpub.mongo.cookbook.emr-out/";
//	private static final String LOG_S3_URI = "s3://com.packtpub.mongo.cookbook.emr-logs/";

	// Provide your own AWS Access and Secret key here
	private static final String AWS_ACCESS_KEY = "<Your Access Key>";
	private static final String AWS_SECRET_KEY = "<Your Secret key>";

	// ------------------Following values can be left unchanged--------------------------------
	private static final String EMR_JOB_FLOW_NAME = "Mongo Hadoop EMR Test";
	private static final String INSTANCE_TYPE = "m1.medium";
	private static final String HADOOP_VERSION = "2.4.0";
	private static final String JAR_PATH = INPUT_S3_BUCKET + "mongo-hadoop-emr-assembly.jar";
	private static final String INPUT_FILE = INPUT_S3_BUCKET + "postalCodes.bson";

	public static void main(String[] args) {

		System.out.println("Creating a new job flow");


		StepConfig jarStep = new StepConfig()
				.withName("Jar Execution Step")
				.withHadoopJarStep(
						new HadoopJarStepConfig(JAR_PATH)
								.withMainClass(
										"com.packtpub.mongo.cookbook.TopStateMapReduceEntrypoint")
								.withArgs(
										"-Dmongo.job.input.format=com.mongodb.hadoop.BSONFileInputFormat",
										"-Dmongo.job.mapper=com.packtpub.mongo.cookbook.TopStatesMapper",
										"-Dmongo.job.reducer=com.packtpub.mongo.cookbook.TopStateReducer",
										"-Dmongo.job.output=org.apache.hadoop.io.Text",
										"-Dmongo.job.output.value=org.apache.hadoop.io.IntWritable",
										"-Dmongo.job.output.value=org.apache.hadoop.io.IntWritable",
										"-Dmongo.job.output.format=com.mongodb.hadoop.BSONFileOutputFormat",
										"-Dmapred.input.dir=" + INPUT_FILE,
										"-Dmapred.output.dir="
												+ OUTPUT_S3_BUCKET))
				.withActionOnFailure(ActionOnFailure.TERMINATE_JOB_FLOW);

		RunJobFlowRequest request = new RunJobFlowRequest()
				.withName(EMR_JOB_FLOW_NAME)
				.withSteps(jarStep)
				.withInstances(
						new JobFlowInstancesConfig()
								.withMasterInstanceType(INSTANCE_TYPE)
								.withHadoopVersion(HADOOP_VERSION)
								.withTerminationProtected(false)
								.withInstanceCount(1)
								.withEc2KeyName("EC2 Test Key Pair"))
				.withLogUri(LOG_S3_URI);

		AmazonElasticMapReduceClient client = new AmazonElasticMapReduceClient(
				new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY));
		RunJobFlowResult result = client.runJobFlow(request);
		System.out.println("Job Flow Id is " + result.getJobFlowId());
	}
}
