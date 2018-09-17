kafka-streams-example
=====

This project provides how to run [unit|integration] test for Kafka Streams applications. It was written as an example project for Kafka User Meetup Seoul, September 2018.

- Unit test with `MockProcessorContext`: working.
- [Unit test with `TopologyTestDriver`](src/test/java/com/dongjinlee/examples/kafkastreams/TopologyTest.java)
- [Integration test with `EmbeddedKafkaCluster`](src/test/java/com/dongjinlee/examples/kafkastreams/IntegrationTest.java)

For slides, see [here](https://speakerdeck.com/dongjin/testing-kafka-streams-applications).
