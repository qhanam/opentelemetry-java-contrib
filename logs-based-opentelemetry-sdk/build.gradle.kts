plugins {
  id("otel.java-conventions")
  id("otel.publish-conventions")
}

description = "Logs-based OpenTelemetry SDK"
otelJava.moduleName.set("io.opentelemetry.contrib.metrics.logs-based-opentelemetry-sdk")

dependencies {
  api("io.opentelemetry:opentelemetry-api")
  api("io.opentelemetry:opentelemetry-sdk-metrics")
  api("io.opentelemetry:opentelemetry-api-incubator")

  implementation("io.opentelemetry:opentelemetry-exporter-otlp:1.43.0")

  compileOnly("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure")

  annotationProcessor("com.google.auto.service:auto-service")
  compileOnly("com.google.auto.service:auto-service-annotations")

  annotationProcessor("com.google.auto.value:auto-value")
  compileOnly("com.google.auto.value:auto-value-annotations")
}
