package com.dongjinlee.examples.kafkastreams.assertj;

import org.apache.kafka.streams.KeyValue;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Objects;

/**
 * AssertJ class for {@link KeyValue}
 */
public class KeyValueAssert<K, V> extends AbstractAssert<KeyValueAssert<K, V>, KeyValue<K, V>> {
  protected KeyValueAssert(final KeyValue<K, V> actual) {
    super(actual, KeyValueAssert.class);
  }

  protected KeyValue<K, V> getActual() {
    return actual;
  }

  /**
   * Verifies that the actual {@link KeyValue} has same key and value with given {@link KeyValue} instance.
   *
   * @param record {@link KeyValue} instance to compare.
   * @return this {@link KeyValueAssert} for assertions chaining.
   */
  public KeyValueAssert<K, V> hasSameKeyValue(KeyValue<K, V> record) {
    return hasSameKeyValue(record.key, record.value);
  }

  /**
   * Verifies that the actual {@link KeyValue} has same key and value with given key and value.
   *
   * @param key   key to compare.
   * @param value value to compare.
   * @return this {@link KeyValueAssert} for assertions chaining.
   */
  public KeyValueAssert<K, V> hasSameKeyValue(K key, V value) {
    Objects.instance().assertNotNull(info, actual);
    Objects.instance().assertEqual(info, actual.key, key);
    Objects.instance().assertEqual(info, actual.value, value);

    return myself;
  }
}
