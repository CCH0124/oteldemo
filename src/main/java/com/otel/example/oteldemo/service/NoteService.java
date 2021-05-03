package com.otel.example.oteldemo.service;

import java.util.List;

import com.otel.example.oteldemo.model.Note;

public interface NoteService {
    List<Note> getAllNote();
    void saveNote(Note note);
    Note getNoteById(Long id);
    void deleteNoteById(Long id);
}
