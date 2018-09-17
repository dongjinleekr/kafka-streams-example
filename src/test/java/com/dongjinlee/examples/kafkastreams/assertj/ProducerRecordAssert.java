package com.dongjinlee.examples.kafkastreams.assertj;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Objects;

/**
 * AssertJ class for {@link ProducerRecord}
 */
public class ProducerRecordAssert<K, V> extends AbstractAssert<ProducerRecordAssert<K, V>, ProducerRecord<K, V>> {
  protected ProducerRecordAssert(final ProducerRecord<K, V> actual) {
    super(actual, ProducerRecordAssert.class);
  }

  protected ProducerRecord<K, V> getActual() {
    return actual;
  }

  /**
   * Verifies that the actual {@link ProducerRecord} has same key and value with given {@link ProducerRecord} instance.
   *
   * @param record {@link ProducerRecord} instance to compare.
   * @return this {@link ProducerRecordAssert} for assertions chaining.
   */
  public ProducerRecordAssert<K, V> hasSameKeyValue(ProducerRecord<K, V> record) {
    return hasSameKeyValue(record.key(), record.value());
  }

  /**
   * Verifies that the actual {@link ProducerRecord} has same key and value with given key and value.
   *
   * @param key   key to compare.
   * @param value value to compare.
   * @return this {@link ProducerRecordAssert} for assertions chaining.
   */
  public ProducerRecordAssert<K, V> hasSameKeyValue(K key, V value) {
    Objects.instance().assertNotNull(info, actual);
    Objects.instance().assertEqual(info, actual.key(), key);
    Objects.instance().assertEqual(info, actual.value(), value);

    return myself;
  }
}
