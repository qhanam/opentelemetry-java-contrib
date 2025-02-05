package io.opentelemetry.contrib.metrics.logsbased;

import io.opentelemetry.api.metrics.MeterBuilder;
import io.opentelemetry.api.metrics.MeterProvider;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.IOException;
import java.util.logging.Logger;

public class LogsBasedMeterProvider implements MeterProvider, Closeable {
    private static final Logger LOGGER = Logger.getLogger(MeterProvider.class.getName());

    @Nullable
    private SdkMeterProvider sdkMeterProvider;

    public LogsBasedMeterProvider (SdkMeterProvider sdkMeterProvider) {
      this.sdkMeterProvider = sdkMeterProvider;
    }

    @Override
    public MeterBuilder meterBuilder(String instrumentationScopeName) {

        if (instrumentationScopeName == null || instrumentationScopeName.isEmpty()) {
            LOGGER.fine("Meter requested without instrumentation scope name.");
            instrumentationScopeName = "unknown";
        }

      if (this.sdkMeterProvider == null) {
        this.sdkMeterProvider = SdkMeterProvider.builder().build();
      }

      return new LogsBasedMeterBuilder(sdkMeterProvider.meterBuilder(instrumentationScopeName));
    }

    @Override
    public void close() throws IOException {
      if (sdkMeterProvider != null) {
        sdkMeterProvider.close();
      }
    }
}
