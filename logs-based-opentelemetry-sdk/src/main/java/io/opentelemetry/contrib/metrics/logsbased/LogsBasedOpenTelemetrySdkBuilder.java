package io.opentelemetry.contrib.metrics.logsbased;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.OpenTelemetrySdkBuilder;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import javax.annotation.Nullable;

public class LogsBasedOpenTelemetrySdkBuilder {
  private ContextPropagators propagators = ContextPropagators.noop();
  @Nullable
  private SdkTracerProvider tracerProvider;
  @Nullable
  private SdkMeterProvider meterProvider;
  @Nullable
  private SdkLoggerProvider loggerProvider;

  private final OpenTelemetrySdkBuilder openTelemetrySdkBuilder = OpenTelemetrySdk
      .builder();

  LogsBasedOpenTelemetrySdkBuilder() {
  }

  @com.google.errorprone.annotations.CanIgnoreReturnValue
  public LogsBasedOpenTelemetrySdkBuilder setTracerProvider(SdkTracerProvider tracerProvider) {
    this.tracerProvider = tracerProvider;
    return this;
  }

  @com.google.errorprone.annotations.CanIgnoreReturnValue
  public LogsBasedOpenTelemetrySdkBuilder setMeterProvider(SdkMeterProvider meterProvider) {
    this.meterProvider = meterProvider;
    return this;
  }

  @com.google.errorprone.annotations.CanIgnoreReturnValue
  public LogsBasedOpenTelemetrySdkBuilder setLoggerProvider(SdkLoggerProvider loggerProvider) {
    this.loggerProvider = loggerProvider;
    return this;
  }

  @com.google.errorprone.annotations.CanIgnoreReturnValue
  public LogsBasedOpenTelemetrySdkBuilder setPropagators(ContextPropagators propagators) {
    this.propagators = propagators;
    return this;
  }

  public LogsBasedOpenTelemetrySdk buildAndRegisterGlobal() {
    LogsBasedOpenTelemetrySdk adot = this.build();
    GlobalOpenTelemetry.set(adot);
    return adot;
  }

  public LogsBasedOpenTelemetrySdk build() {
    SdkTracerProvider tracerProvider = this.tracerProvider;
    if (tracerProvider == null) {
      tracerProvider = SdkTracerProvider.builder().build();
    }

    SdkMeterProvider meterProvider = this.meterProvider;
    if (meterProvider == null) {
      meterProvider = SdkMeterProvider.builder().build();
    }

    SdkLoggerProvider loggerProvider = this.loggerProvider;
    if (loggerProvider == null) {
      loggerProvider = SdkLoggerProvider.builder().build();
    }

    OpenTelemetrySdk openTelemetrySdk = openTelemetrySdkBuilder.setTracerProvider(tracerProvider)
      .setTracerProvider(tracerProvider)
      .setLoggerProvider(loggerProvider)
      .setPropagators(propagators)
      .build();

    return new LogsBasedOpenTelemetrySdk(openTelemetrySdk, meterProvider);
  }
}
