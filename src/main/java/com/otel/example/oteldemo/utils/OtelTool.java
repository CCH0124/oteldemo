package com.otel.example.oteldemo.utils;

import com.otel.example.oteldemo.model.Note;


import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.extension.annotations.WithSpan;

public class OtelTool {
    @WithSpan
    public static void requestValueSpan(Note note) {
        Span span = Span.current();
        span.setAttribute("id", note.getId());
        span.addEvent("Get.note.from.id", Attributes.of(AttributeKey.stringKey("content"), note.getContent(),AttributeKey.stringKey("title"), note.getTitle()));
    }
}
