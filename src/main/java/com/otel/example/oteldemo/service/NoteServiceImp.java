package com.otel.example.oteldemo.service;

import java.util.List;
import java.util.Optional;

import com.otel.example.oteldemo.model.Note;
import com.otel.example.oteldemo.repository.NoteRepository;
import com.otel.example.oteldemo.utils.OtelTool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;

@Service
public class NoteServiceImp implements NoteService {
    @Autowired
    private NoteRepository noteRepository;

    private final Tracer tracer = GlobalOpenTelemetry.getTracer(NoteServiceImp.class.getSimpleName());
    @Override
    public List<Note> getAllNote() {
        // TODO Auto-generated method stub
        return this.noteRepository.findAll();
    }

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

    @Override
    public Note getNoteById(Long id) {
        // TODO Auto-generated method stub
        Optional<Note> optional = this.noteRepository.findById(id);
        Note note = null;
        if (optional.isPresent()) {
            note = optional.get();
            OtelTool.requestValueSpan(note);
        }
        return note;
    }

    @Override
    public void deleteNoteById(Long id) {
        // TODO Auto-generated method stub
        this.noteRepository.deleteById(id);
    }
}
