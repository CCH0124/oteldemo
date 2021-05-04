package com.otel.example.oteldemo.utils;

import com.otel.example.oteldemo.model.Note;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.extension.annotations.WithSpan;

public class OtelTool {
    private static final Tracer tracer = GlobalOpenTelemetry.getTracer(OtelTool.class.getSimpleName());

    @WithSpan
    public static void requestValueSpan(Note note) {
        Span span = Span.current();
        span.setAttribute("id", note.getId());
        span.addEvent("Get.note.from.id", Attributes.of(AttributeKey.stringKey("content"), note.getContent(),
                AttributeKey.stringKey("title"), note.getTitle()));
        parent();
    }

    private static void parent() {
        Span parentSpan = tracer.spanBuilder("parent").startSpan();
        try {
            parentSpan.addEvent("parent", Attributes.of(AttributeKey.stringKey("parent"), "parent"));
            child(parentSpan);
        } finally {
            parentSpan.end();
        }
    }

    private static void child(Span span) {
        Span childSpan = tracer.spanBuilder("child").setParent(Context.current().with(span)).startSpan();
        try (Scope scope = childSpan.makeCurrent()) {
            childSpan.setAttribute("name", "child");
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            childSpan.end();
        }
    }
}
