package io.opentelemetry.contrib.metrics.logsbased;

import static io.opentelemetry.api.common.AttributeKey.stringKey;

import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.metrics.DoubleHistogramBuilder;
import io.opentelemetry.api.metrics.LongHistogramBuilder;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import javax.annotation.Nullable;
import java.lang.reflect.Field;

public class LogsBasedDoubleHistogram implements DoubleHistogram {
  private final DoubleHistogram doubleHistogram;
  private final String name;

  public LogsBasedDoubleHistogram(String name, DoubleHistogram doubleHistogram) {
    this.doubleHistogram = doubleHistogram;
    this.name = name;
  }

  @Override
  public void record(double v) {
    Context context = Context.current();
    Span getUserSpan = Span.fromContext(context);
    getUserSpan.setAttribute("metric." + this.name, v);
    this.doubleHistogram.record(v, getOperationAttribute(context));
  }

  @Override
  public void record(double v, Attributes attributes) {
    Context context = Context.current();
    Span getUserSpan = Span.fromContext(context);
    getUserSpan.setAttribute("metric." + this.name, v);
    attributes = attributes.toBuilder().putAll(getOperationAttribute(context)).build();
    this.doubleHistogram.record(v, attributes);
  }

  @Override
  public void record(double v, Attributes attributes, Context context) {
    Span getUserSpan = Span.fromContext(context);
    getUserSpan.setAttribute("metric." + this.name, v);
    attributes = attributes.toBuilder().putAll(getOperationAttribute(context)).build();
    this.doubleHistogram.record(v, attributes, context);
  }

  private static Attributes getOperationAttribute(Context context) {
    String operation = readOperationFromBaggage(context);
    if (operation != null) {
      return Attributes.of(stringKey("aws.local.operation"), operation);
    }
    String spanName = reflectSpanName(Span.fromContext(context));
    if(spanName != null) {
      return Attributes.of(stringKey("aws.local.operation"), spanName);
    }
    return Attributes.empty();
  }

  @Nullable
  private static String readOperationFromBaggage(Context context) {
    return Baggage.fromContext(context).getEntryValue("aws.local.operation");
  }

  @Nullable
  private static String reflectSpanName(Span span) {
    try {
      Field agentSpanField = span.getClass().getDeclaredField("agentSpan");
      agentSpanField.setAccessible(true);
      Object applicationSpan = agentSpanField.get(span);
      Field applicationSpanField =
          agentSpanField.get(span).getClass().getDeclaredField("name");
      applicationSpanField.setAccessible(true);
      return (String) applicationSpanField.get(applicationSpan);
    } catch (Exception e) {
      return null;
    }
  }

  static final class EventDoubleHistogramBuilder implements DoubleHistogramBuilder {
    private final DoubleHistogramBuilder doubleHistogramBuilder;
    private final String name;

    public EventDoubleHistogramBuilder(String name, DoubleHistogramBuilder doubleHistogramBuilder) {
      this.name = name;
      this.doubleHistogramBuilder = doubleHistogramBuilder;
    }

    @Override
    public DoubleHistogramBuilder setDescription(String s) {
      this.doubleHistogramBuilder.setDescription(s);
      return this;
    }

    @Override
    public DoubleHistogramBuilder setUnit(String s) {
      this.doubleHistogramBuilder.setUnit(s);
      return this;
    }

    @Override
    public LongHistogramBuilder ofLongs() {
      return this.doubleHistogramBuilder.ofLongs();
    }

    @Override
    public DoubleHistogram build() {
      return new LogsBasedDoubleHistogram(this.name, this.doubleHistogramBuilder.build());
    }
  }
}
