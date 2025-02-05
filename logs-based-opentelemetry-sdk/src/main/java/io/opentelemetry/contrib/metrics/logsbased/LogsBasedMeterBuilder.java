package io.opentelemetry.contrib.metrics.logsbased;

import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.MeterBuilder;

class LogsBasedMeterBuilder implements MeterBuilder {
    private final MeterBuilder meterBuilder;

    LogsBasedMeterBuilder(MeterBuilder meterBuilder) {
        this.meterBuilder = meterBuilder;
    }

    @Override
    public MeterBuilder setSchemaUrl(String schemaUrl) {
        meterBuilder.setSchemaUrl(schemaUrl);
        return this;
    }

    @Override
    public MeterBuilder setInstrumentationVersion(String instrumentationScopeVersion) {
        meterBuilder.setInstrumentationVersion(instrumentationScopeVersion);
        return this;
    }

    @Override
    public Meter build() {
        return new LogsBasedMeter(this.meterBuilder.build());
    }
}
