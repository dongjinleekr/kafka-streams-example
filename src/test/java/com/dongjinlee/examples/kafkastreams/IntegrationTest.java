package com.dongjinlee.examples.kafkastreams;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.MockTime;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.integration.utils.EmbeddedKafkaCluster;
import org.apache.kafka.streams.integration.utils.IntegrationTestUtils;
import org.apache.kafka.test.TestUtils;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import static com.dongjinlee.examples.kafkastreams.assertj.Assertions.assertThat;

/**
 * Integration test using {@link EmbeddedKafkaCluster}
 */
public class IntegrationTest {
  @ClassRule
  public final static EmbeddedKafkaCluster CLUSTER = new EmbeddedKafkaCluster(1);

  private final static String APPLICATION_ID = "test-app-id";
  private final static String INPUT_TOPIC = "inputTopic";
  private final static String OUTPUT_TOPIC = "outputTopic";

  @BeforeClass
  public static void startKafkaCluster() throws Exception {
    CLUSTER.createTopic(INPUT_TOPIC);
    CLUSTER.createTopic(OUTPUT_TOPIC);
  }

  private Properties producerProperty() {
    Properties producerConfig = new Properties();
    producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, CLUSTER.bootstrapServers());
    producerConfig.put(ProducerConfig.ACKS_CONFIG, "all");
    producerConfig.put(ProducerConfig.RETRIES_CONFIG, 0);
    producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Serdes.String().serializer().getClass());
    producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Serdes.String().serializer().getClass());
    return producerConfig;
  }

  private Properties streamsProperty(String appId, String bootstrap) {
    Properties streamsProperty = WordCountDemo.properties(appId, bootstrap);
    streamsProperty.put(StreamsConfig.STATE_DIR_CONFIG, TestUtils.tempDirectory().getAbsolutePath());
    return streamsProperty;
  }

  private Properties consumerProperty() {
    Properties consumerConfig = new Properties();
    consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, CLUSTER.bootstrapServers());
    consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "test-consumer");
    consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, Serdes.String().deserializer().getClass());
    consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, Serdes.Long().deserializer().getClass());
    return consumerConfig;
  }

  @Test
  public void test() throws Exception {
    Properties props = streamsProperty(APPLICATION_ID, CLUSTER.bootstrapServers());
    Topology topology = WordCountDemo.topology(INPUT_TOPIC, OUTPUT_TOPIC);
    List<String> inputValues = Arrays.asList("A", "A", "B", "C", "B");
    List<KeyValue<String, Long>> expected = Arrays.asList(
        new KeyValue<>("a", 1L),
        new KeyValue<>("a", 2L),
        new KeyValue<>("b", 1L),
        new KeyValue<>("c", 1L),
        new KeyValue<>("b", 2L));

    // Create and run KafkaStreams instance
    final KafkaStreams streams = new KafkaStreams(topology, props);
    streams.start();

    // Feed input with produce config
    IntegrationTestUtils.produceValuesSynchronously(INPUT_TOPIC, inputValues, producerProperty(), new MockTime());

    // Retrieve output with consumer config
    List<KeyValue<String, Long>> actualWordCounts = IntegrationTestUtils.
        waitUntilMinKeyValueRecordsReceived(consumerProperty(), OUTPUT_TOPIC, expected.size());
    // Stop KafkaStreams instance
    streams.close();

    // assertions
    assertThat(actualWordCounts.get(0)).hasSameKeyValue(expected.get(0));
    assertThat(actualWordCounts.get(1)).hasSameKeyValue(expected.get(1));
    assertThat(actualWordCounts.get(2)).hasSameKeyValue(expected.get(2));
  }
}
