package io.opentelemetry.contrib.metrics.logsbased;

import io.opentelemetry.api.metrics.DoubleGaugeBuilder;
import io.opentelemetry.api.metrics.DoubleHistogramBuilder;
import io.opentelemetry.api.metrics.LongCounterBuilder;
import io.opentelemetry.api.metrics.LongUpDownCounterBuilder;
import io.opentelemetry.api.metrics.Meter;

public class LogsBasedMeter implements Meter {
    private final Meter meter;

    public LogsBasedMeter(Meter meter) {
        this.meter = meter;
    }

    @Override
    public LongCounterBuilder counterBuilder(String name) {
        return this.meter.counterBuilder(name);
    }

    @Override
    public LongUpDownCounterBuilder upDownCounterBuilder(String name) {
        return this.meter.upDownCounterBuilder(name);
    }

    @Override
    public DoubleHistogramBuilder histogramBuilder(String name) {
        return new LogsBasedDoubleHistogram.EventDoubleHistogramBuilder(name, this.meter.histogramBuilder(name));
    }

    @Override
    public DoubleGaugeBuilder gaugeBuilder(String name) {
        return this.meter.gaugeBuilder(name);
    }
}
