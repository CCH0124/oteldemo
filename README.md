1. [@Controller vs @RestController](https://javarevisited.blogspot.com/2017/08/difference-between-restcontroller-and-controller-annotations-spring-mvc-rest.html#axzz6sRNhPjm3)
2. [Tracing API](https://github.com/open-telemetry/opentelemetry-specification/blob/0.3/specification/api-tracing.md#set-attributes)

## Span 
Span 是組成 Tracer 的單位。
### SpanContext
為了要有完整 Tracer 的過程，每個 Span 要將 SpanContext 傳遞至子 Span。

SpanContext 透過 span_ID 告知子 span 誰是父，以及它屬於什麼 Tracer（trace_ID）。
### Attributes
由 `Key-value` 組成，提供 Span 詳細的資訊。可以查詢、分組或以其他方式分析 trace 和 span。
##### Status
可將其設定為 `OK`、`Cancelled` 或 `Permission Denied`。
##### SpanKind
SpanKind 在 Tracer 中提供有用訊息，此 Span 是否為遠端系統? SpanKind 值可以為 `CLIENT`、`SERVER`、`PRODUCER`、`CONSUMER` 和 `INTERNAL`。
### Events
包含名稱、時間戳和可選的 `Attributes` 集合等。其表示在 Span 工作負載內的特定時間發生所發生的事件。可能如下

```
events: 
t:3
name:log
message: "retrieved 400 records"
...
```
## 程式碼實現概念
### Annotation @WithSpan
每次調用帶註釋的方法時，它會在當前 Trace 中創建一個子 Span，並記錄所有引發的異常。

也可以透過 `exclude`，排除一些不要的場景。

### Accessing the tracer
如果要與 `Tracer` 進行交互，需要先獲取 `Tracer` 的實例。`Tracer` 以它們要檢測的組件命名。通常是一個 *library*、一個 *package*或一個 *class*。

```java
Tracer tracer = GlobalOpenTelemetry.getTracer(instrumentationName, instrumentationVersion);
```

### Span 屬性使用
- `Span` 中 `setAttribute` 是用於細分數據的索引。可能想要新增帳戶 ID 之類，以了解請求瓶頸和錯誤是否特定於某個帳戶或是否影響每個帳戶。
- `Span` 中 `addEvent` 可將細節做記錄，就是日誌的概念。這可自動查找與特定事務關聯的所有日誌，而不必進行大量的搜索和過濾。隨著服務的擴展這將是一個很好的追蹤過程

### Accessing the current span

`Span` 是在應用程式框架中被創建和管理。每個請求自動創建一個 `Tracer`，且應用程式程式碼已經被包裝在一個 `Span`，可用於新增特定於應用程式的*屬性*(setAttribute)和*事件*(addEvent)。

上述的前提是框架要被[支援](https://github.com/open-telemetry/opentelemetry-java-instrumentation/blob/main/docs/supported-libraries.md)。

```java
Span span = Span.current()
```

### Create own spans

當創建自己的 Span。這些範圍將自動成為當前範圍的子，同時新增至 `Tracer` 中。Span 管理包括三個步驟：開始 Span、設置為當前 Span 和結束 Span。當前如果存在 Span，OpenTelemetry 會將其創建為當前 Span 的子。會在 `Tracer` 上調用 `spanBuilder` 方法以觸發一個新的 `Tracer`。創建新的 Span 後，使用 `Scope` 建立一個新的代碼區塊，當中會包含子 Span。在該範圍內對 `Span.current()` 的任何調用都將回傳該子 Span，而不是請求的父 Span。都完成後，需呼叫 `end()` 方法來關閉 Span，否則會有洩漏問題。

### Error Handling

在 OpenTelemetry 中，異常記錄為事件。但是，為確保異常的格式正確，應使用 `span.recordException(error)` 方法而非 `addEvent`。

```java
childSpan.recordException(new RuntimeException("oops"));
childSpan.setStatus(StatusCode.ERROR);
```
在 OpenTelemetry 中，錯誤表示整體操作未完成。當異常被處理並不表示著整個操作都無法完成。為了示意操作失敗，需調用 `span.setStatus()` 並傳入錯誤代碼，該代碼可讓分析工具來自動觸發警報、測量錯誤率等。

## 實作

使用 `Annotation` 方式，此方式會建立一個 Span，而此 Span 會是觸發該 Span 的子 Span，我們這邊設置了屬性(setAttribute)和事件(event)這會以 Log 方式呈現。
```java
    @WithSpan
    public static void requestValueSpan(Note note) {
        Span span = Span.current();
        span.setAttribute("id", note.getId());
        span.addEvent("Get.note.from.id", Attributes.of(AttributeKey.stringKey("content"), note.getContent(),AttributeKey.stringKey("title"), note.getTitle()));
    }
```
![](image/custom-span-anno.png)

下面算是實作 `@WithSpan` 的方式，只不過需要定義一個 `Tracer`，在建立一個 Span(spanBuilder)。

```java
private final Tracer tracer = GlobalOpenTelemetry.getTracer(NoteServiceImp.class.getSimpleName());
```
```java
    @Override
    public void saveNote(Note note) {
        // TODO Auto-generated method stub
        Span span = tracer.spanBuilder("saveNote").startSpan();
        try (Scope scope = span.makeCurrent()) {
            this.noteRepository.save(note);
            span.setAttribute("CreateTime", note.getCreatedAt().toString());
            span.addEvent("Request.query", Attributes.of(AttributeKey.stringKey("content"), note.getContent(),AttributeKey.stringKey("title"), note.getTitle()));
        } catch (Throwable t) {
            span.setStatus(StatusCode.ERROR, "Change it to your error message");
        } finally {
            span.end(); // closing the scope does not end the span, this has to be done manually
        }
    }
```

![](image/custom-span.png)

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