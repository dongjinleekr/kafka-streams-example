name := "kafka-streams-examples"

organization := "com.dongjinlee"

version := "0.1-SNAPSHOT"

description := "A KafkaStreams example project."

val assertjVersion = "3.11.1"
val junitVersion = "4.12"
val kafkaVersion = "1.1.0"

// see: https://github.com/sbt/sbt/issues/3618
val workaround = {
  sys.props += "packaging.type" -> "jar"
  ()
}

libraryDependencies ++= Seq(
  // compile dependencies: required for kafka streams applications
  "org.apache.kafka" % "kafka-clients" % kafkaVersion,
  "org.apache.kafka" % "kafka-streams" % kafkaVersion,
  // test dependencies
  // common testing libraries
  "junit" % "junit" % junitVersion % "test",
  "org.assertj" % "assertj-core" % assertjVersion % "test",
  // kafka streams common testing libraries
  ("org.apache.kafka" % "kafka-clients" % kafkaVersion classifier "test") % "test",
  ("org.apache.kafka" % "kafka-streams" % kafkaVersion classifier "test") % "test",
  // additional testing library for TopologyTestDriver functionality
  "org.apache.kafka" % "kafka-streams-test-utils" % kafkaVersion % "test",
  // additional testing library for EmbeddedKafkaCluster functionality
  "org.apache.kafka" % "kafka_2.12" % kafkaVersion % "test",
  ("org.apache.kafka" % "kafka_2.12" % kafkaVersion classifier "test") % "test",
)

javacOptions ++= Seq("-source", "1.8")
