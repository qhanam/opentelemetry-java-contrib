package io.opentelemetry.contrib.metrics.logsbased;

public final class LogsBasedMeterProviderBuilder {
    LogsBasedMeterProviderBuilder() {
    }

    public LogsBasedMeterProvider build() {
        return new LogsBasedMeterProvider();
    }
}
