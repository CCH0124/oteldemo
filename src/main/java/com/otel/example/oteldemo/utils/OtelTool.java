package com.otel.example.oteldemo.utils;

import com.otel.example.oteldemo.model.Note;
import com.otel.example.oteldemo.service.NoteServiceImp;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.extension.annotations.WithSpan;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;

public class OtelTool {
    private final static Tracer tracer = GlobalOpenTelemetry.getTracer(NoteServiceImp.class.getSimpleName());

    @WithSpan
    public static void requestValueSpan(Note note) {
        Span span = Span.current();
        span.setAttribute("id", note.getId());
        span.addEvent("Get.note.from.id", Attributes.of(AttributeKey.stringKey("content"), note.getContent(),AttributeKey.stringKey("title"), note.getTitle()));
    }

    private static Attributes atttributes(String key, String value) {
        return Attributes.of(AttributeKey.stringKey(key), value);
    }
}
