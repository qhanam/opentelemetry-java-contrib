package io.opentelemetry.contrib.metrics.logsbased;

import io.opentelemetry.api.metrics.MeterBuilder;
import io.opentelemetry.api.metrics.MeterProvider;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.sdk.metrics.Aggregation;
import io.opentelemetry.sdk.metrics.InstrumentSelector;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.View;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.logging.Logger;

public class LogsBasedMeterProvider implements MeterProvider, Closeable {
    private static final Logger LOGGER = Logger.getLogger(MeterProvider.class.getName());

    private static final SdkMeterProvider sdkMeterProvider =
            SdkMeterProvider.builder()
                    .registerView(
                            // Target histograms matching and use defaults (maxBuckets: 160 maxScale: 20)
                            InstrumentSelector.builder().setName("*ExponentialHistogram*").build(),
                            View.builder()
                                    .setAggregation(Aggregation.base2ExponentialBucketHistogram())
                                    .build())
                    .registerMetricReader(
                            PeriodicMetricReader.builder(OtlpGrpcMetricExporter.builder().build())
                                    // Default is 60000ms (60 seconds). Set to 30 seconds for demonstrative purposes
                                    // only.
                                    .setInterval(Duration.ofSeconds(30))
                                    .build())
                    .build();

    @Override
    public MeterBuilder meterBuilder(String instrumentationScopeName) {

        if (instrumentationScopeName == null || instrumentationScopeName.isEmpty()) {
            LOGGER.fine("Meter requested without instrumentation scope name.");
            instrumentationScopeName = "unknown";
        }

        return new LogsBasedMeterBuilder(sdkMeterProvider.meterBuilder(instrumentationScopeName));
    }

    @Override
    public void close() throws IOException {
        sdkMeterProvider.close();
    }
}
