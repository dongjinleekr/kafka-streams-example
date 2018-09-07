package com.dongjinlee.examples.kafkastreams;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.test.ConsumerRecordFactory;
import org.apache.kafka.test.StreamsTestUtils;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TopologyTest {

  private final static String APPLICATION_ID = "test-app-id";
  private final static String INPUT_TOPIC = "inputTopic";
  private final static String OUTPUT_TOPIC = "outputTopic";

  private final ConsumerRecordFactory<String, String> recordFactory =
      new ConsumerRecordFactory<>(Serdes.String().serializer(), Serdes.String().serializer());
  private final Properties props = StreamsTestUtils.getStreamsConfig(APPLICATION_ID, "localhost:9092",
      Serdes.String().getClass().getName(), Serdes.String().getClass().getName(), new Properties());

  public static void compareKeyAndValue(ProducerRecord<String, Long> lhs, ProducerRecord<String, Long> rhs) {
    assertThat(lhs.key(), equalTo(rhs.key()));
    assertThat(lhs.value(), equalTo(rhs.value()));
  }

  @Test
  public void test() {
    // Create TopologyTestDriver instance
    TopologyTestDriver driver = new TopologyTestDriver(
        WordCountDemo.topology(INPUT_TOPIC, OUTPUT_TOPIC), props);

    List<String> inputValues = Arrays.asList("A", "A", "B", "C", "B");
    List<ProducerRecord<String, Long>> expectedList = Arrays.asList(
        new ProducerRecord<>(OUTPUT_TOPIC, "a", 1L),
        new ProducerRecord<>(OUTPUT_TOPIC, "a", 2L),
        new ProducerRecord<>(OUTPUT_TOPIC, "b", 1L),
        new ProducerRecord<>(OUTPUT_TOPIC, "c", 1L),
        new ProducerRecord<>(OUTPUT_TOPIC, "b", 2L));

    // Feed Input
    for (String inputValue : inputValues) {
      driver.pipeInput(recordFactory.create(INPUT_TOPIC, null, inputValue));
    }

    // Retrieve Output
    for (ProducerRecord<String, Long> expected : expectedList) {
      ProducerRecord<String, Long> actual = driver.
          readOutput(OUTPUT_TOPIC, Serdes.String().deserializer(), Serdes.Long().deserializer());
      compareKeyAndValue(actual, expected);
    }
  }
}
