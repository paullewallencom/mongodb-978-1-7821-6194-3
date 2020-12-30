#!/usr/bin/env bash
if [ -z "$HADOOP_HOME" ]
then
	echo 'HADOOP_HOME not set, setting to ~/hadoop-binaries/hadoop-2.4.0'
	export HADOOP_HOME=~/hadoop-binaries/hadoop-2.4.0
fi
if [ -f ./streaming/build/libs/mongo-hadoop-streaming-1.2.1-SNAPSHOT-hadoop_2.4.jar ]
then
	CONNECTOR_HOME=.
else
	if [ -z "$HADOOP_CONNECTOR_HOME" ]
	then
		echo 'Set the HADOOP_CONNECTOR_HOME to the root of the mongo-hadoop connector project'
		exit 1
	else
		CONNECTOR_HOME=$HADOOP_CONNECTOR_HOME
	fi
fi
echo 'Invoking the map reduce job'
$HADOOP_HOME/bin/hadoop jar \
$HADOOP_HOME/share/hadoop/tools/lib/hadoop-streaming* \
-libjars $CONNECTOR_HOME/streaming/build/libs/mongo-hadoop-streaming-1.2.1-SNAPSHOT-hadoop_2.4.jar \
-input /tmp/in \
-output /tmp/out \
-inputformat com.mongodb.hadoop.mapred.MongoInputFormat \
-outputformat com.mongodb.hadoop.mapred.MongoOutputFormat \
-io mongodb \
-jobconf mongo.input.uri=mongodb://127.0.0.1:27017/test.pincodes \
-jobconf mongo.output.uri=mongodb://127.0.0.1:27017/test.pyStreamTest \
-jobconf stream.io.identifier.resolver.class=com.mongodb.hadoop.streaming.io.MongoIdentifierResolver \
-mapper mapper.py \
-reducer reducer.py
     


