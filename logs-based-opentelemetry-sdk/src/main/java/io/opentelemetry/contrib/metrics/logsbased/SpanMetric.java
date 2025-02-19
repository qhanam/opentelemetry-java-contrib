package io.opentelemetry.contrib.metrics.logsbased;

import static io.opentelemetry.api.common.AttributeKey.stringKey;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.internal.InternalAttributeKeyImpl;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.ContextKey;

public class SpanMetric<T> {

  private final String name;
  private final String unit;
  private final AttributeKey<T> metricValueKey;

  private final AttributeKey<T> metricAttributenameKey;

  public static <T> SpanMetric<T> create(AttributeKey<T> name, String unit) {
    return new SpanMetric<>(name, unit);
  }

  private SpanMetric(AttributeKey<T> name, String unit) {
    this.name = name.getKey();
    this.unit = unit;
    this.metricValueKey =
        InternalAttributeKeyImpl.create("otel.metric.value", name.getType());
    this.metricAttributenameKey =
        InternalAttributeKeyImpl.create(
            "otel.metric." + this.name + "." + unit,
            name.getType());
  }

  public void record(T value) {
    Context.current().get(ContextKey.named("my-context-key"));
    Span span = Span.fromContext(Context.current());
    Attributes attributes = Attributes.builder()
        .put(stringKey("otel.metric.name"), this.name)
        .put(this.metricValueKey, value)
        .put(stringKey("otel.metric.unit"), this.unit)
        .build();
    span.addEvent("otel.metric", attributes);

    // Ideally we would use events only, but some providers don't support events.
    span.setAttribute(this.metricAttributenameKey, value);
  }

  public void record(T value, Attributes attributes) {
    Span span = Span.fromContext(Context.current());
    attributes = attributes.toBuilder()
        .put(stringKey("otel.metric.name"), this.name)
        .put(this.metricValueKey, value)
        .put(stringKey("otel.metric.unit"), this.unit)
        .build();
    span.addEvent("otel.metric", attributes);

    // Ideally we would use events only, but some providers don't support events.
    span.setAttribute(this.metricAttributenameKey, value);
  }
}
