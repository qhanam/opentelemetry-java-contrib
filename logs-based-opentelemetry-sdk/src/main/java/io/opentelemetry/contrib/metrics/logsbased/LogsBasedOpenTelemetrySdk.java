package io.opentelemetry.contrib.metrics.logsbased;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.metrics.MeterProvider;
import io.opentelemetry.api.trace.TracerProvider;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.trace.SdkTracerProvider;

public class LogsBasedOpenTelemetrySdk implements OpenTelemetry {

    private final OpenTelemetrySdk openTelemetrySdk;

    private final MeterProvider meterProvider;

    LogsBasedOpenTelemetrySdk(SdkTracerProvider tracerProvider, SdkMeterProvider meterProvider, SdkLoggerProvider loggerProvider, ContextPropagators propagators) {
        this.meterProvider = new LogsBasedMeterProvider();
        this.openTelemetrySdk = OpenTelemetrySdk
                .builder()
                .setTracerProvider(tracerProvider)
                .setLoggerProvider(loggerProvider)
                .setPropagators(propagators)
                .build();
    }

    public static LogsBasedOpenTelemetrySdkBuilder builder() {
        return new LogsBasedOpenTelemetrySdkBuilder();
    }

    @Override
    public MeterProvider getMeterProvider() {
        return this.meterProvider;
    }

    @Override
    public TracerProvider getTracerProvider() {
        return openTelemetrySdk.getTracerProvider();
    }

    @Override
    public ContextPropagators getPropagators() {
        return openTelemetrySdk.getPropagators();
    }
}
