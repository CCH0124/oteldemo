1. [@Controller vs @RestController](https://javarevisited.blogspot.com/2017/08/difference-between-restcontroller-and-controller-annotations-spring-mvc-rest.html#axzz6sRNhPjm3)
2. [Tracing API](https://github.com/open-telemetry/opentelemetry-specification/blob/0.3/specification/api-tracing.md#set-attributes)

## 設定 optntelemtry agent
在 lunch.json 中設定 `vmArgs`
```java
{
    // Use IntelliSense to learn about possible attributes.
    // Hover to view descriptions of existing attributes.
    // For more information, visit: https://go.microsoft.com/fwlink/?linkid=830387
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Launch Current File",
            "request": "launch",
            "mainClass": "${file}",
            "vmArgs": "-javaagent:opentelemetry-javaagent-all.jar -Dotel.resource.attributes=service.name=oteldemo -Dotel.exporter.otlp.endpoint=http://172.17.10.105:4317 -Dotel.exporter.otlp.traces.endpoint=http://172.17.10.105:4317 -Dotel.exporter.otlp.metrics.endpoint=http://172.17.10.105:4317"
        },
        {
            "type": "java",
            "name": "Launch OteldemoApplication",
            "request": "launch",
            "mainClass": "com.otel.example.oteldemo.OteldemoApplication",
            "projectName": "oteldemo"
        }
    ]
}
```