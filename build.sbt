name := "spark-test"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "1.6.1"

// http://mvnrepository.com/artifact/org.apache.spark/spark-mllib_2.11
libraryDependencies += "org.apache.spark" % "spark-mllib_2.11" % "1.6.1"

libraryDependencies += "org.apache.spark" % "spark-graphx_2.11" % "1.6.1"

libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "1.6.1"

libraryDependencies += "org.apache.spark" % "spark-hive_2.11" % "1.6.1"

libraryDependencies += "com.datastax.spark" % "spark-cassandra-connector_2.11" % "1.5.0"

libraryDependencies += "org.apache.hbase" % "hbase-server" % "1.1.2"

libraryDependencies += "org.apache.hbase" % "hbase-client" % "1.1.2"

libraryDependencies += "org.apache.hbase" % "hbase-common" % "1.1.2"

libraryDependencies += "com.github.scopt" % "scopt_2.11" % "3.3.0"

libraryDependencies += "org.apache.cassandra" % "cassandra-all" % "1.2.11"

libraryDependencies += "org.apache.cassandra" % "cassandra-clientutil" % "3.0.2"

libraryDependencies += "org.apache.kafka" % "kafka_2.11" % "0.8.2.1"

libraryDependencies += "org.apache.spark" % "spark-streaming-kafka_2.11" % "1.6.1"

libraryDependencies += "org.apache.spark" % "spark-streaming-mqtt_2.10" % "1.6.1"

libraryDependencies += "org.apache.spark" % "spark-streaming-flume_2.10" % "1.6.1"

libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "1.6.1"

libraryDependencies += "org.eclipse.paho" % "org.eclipse.paho.client.mqttv3" % "1.0.2"

libraryDependencies += "org.apache.spark" % "spark-streaming-twitter_2.11" % "1.6.1"

libraryDependencies += "com.twitter" % "algebird-core_2.11" % "0.12.0"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.3.11"

libraryDependencies += "com.typesafe.akka" % "akka-zeromq_2.11" % "2.3.11"

libraryDependencies += "org.apache.spark" % "spark-streaming-zeromq_2.11" % "1.6.1"

