package com.dongjinlee.examples.kafkastreams.assertj;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.KeyValue;

/**
 * Provides entry point for all Kafka assert classes.
 */
public class Assertions {
  public static <K, V> ProducerRecordAssert<K, V> assertThat(final ProducerRecord<K, V> actual) {
    return new ProducerRecordAssert<>(actual);
  }

  public static <K, V> KeyValueAssert<K, V> assertThat(final KeyValue<K, V> actual) {
    return new KeyValueAssert<>(actual);
  }

  /**
   * Empty constructor: instantiation disallowed.
   */
  protected Assertions() {
  }
}
