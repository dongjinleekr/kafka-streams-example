name := "kafka-streams-examples"

organization := "com.dongjinlee"

version := "0.1-SNAPSHOT"

description := "A KafkaStreams example project."

val hamcrestVersion = "1.3"
val junitVersion = "4.12"
val kafkaVersion = "2.0.0"

// see: https://github.com/sbt/sbt/issues/3618
val workaround = {
  sys.props += "packaging.type" -> "jar"
  ()
}

libraryDependencies ++= Seq(
  // compile dependencies
  "org.apache.kafka" % "kafka-clients" % kafkaVersion,
  "org.apache.kafka" % "kafka-streams" % kafkaVersion,
  // test dependencies
  // test common
  "junit" % "junit" % junitVersion % "test",
  "org.hamcrest" % "hamcrest-all" % hamcrestVersion % "test",
  // TopologyTestDriver
  "org.apache.kafka" % "kafka-streams-test-utils" % kafkaVersion % "test",
  // EmbeddedKafkaCluster
  "org.apache.kafka" % "kafka_2.12" % kafkaVersion % "test",
  ("org.apache.kafka" % "kafka_2.12" % kafkaVersion classifier "test") % "test",
  ("org.apache.kafka" % "kafka-clients" % kafkaVersion classifier "test") % "test",
  ("org.apache.kafka" % "kafka-streams" % kafkaVersion classifier "test") % "test",
)

javacOptions ++= Seq("-source", "1.8")
