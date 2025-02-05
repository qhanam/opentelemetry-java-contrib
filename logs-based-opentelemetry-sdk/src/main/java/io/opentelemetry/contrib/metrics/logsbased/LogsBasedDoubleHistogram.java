package io.opentelemetry.contrib.metrics.logsbased;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.metrics.DoubleHistogramBuilder;
import io.opentelemetry.api.metrics.LongHistogramBuilder;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;

public class LogsBasedDoubleHistogram implements DoubleHistogram {
    private final DoubleHistogram doubleHistogram;
    private final String name;

    public LogsBasedDoubleHistogram(String name, DoubleHistogram doubleHistogram) {
        this.doubleHistogram = doubleHistogram;
        this.name = name;
    }

    @Override
    public void record(double v) {
        Context currentContext = Context.current();
        Span getUserSpan = Span.fromContext(currentContext);
        getUserSpan.setAttribute("metric." + this.name, v);
        this.doubleHistogram.record(v);
    }

    @Override
    public void record(double v, Attributes attributes) {
        Context currentContext = Context.current();
        Span getUserSpan = Span.fromContext(currentContext);
        getUserSpan.setAttribute("metric." + this.name, v);
        this.doubleHistogram.record(v, attributes);
    }

    @Override
    public void record(double v, Attributes attributes, Context context) {
        Span getUserSpan = Span.fromContext(context);
        getUserSpan.setAttribute("metric." + this.name, v);
        this.doubleHistogram.record(v, attributes, context);
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
