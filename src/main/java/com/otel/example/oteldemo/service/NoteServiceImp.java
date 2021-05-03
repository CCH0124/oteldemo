package com.otel.example.oteldemo.service;

import java.util.List;
import java.util.Optional;

import com.otel.example.oteldemo.model.Note;
import com.otel.example.oteldemo.repository.NoteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteServiceImp implements NoteService {
    @Autowired
    private NoteRepository noteRepository;

    @Override
    public List<Note> getAllNote() {
        // TODO Auto-generated method stub
        return this.noteRepository.findAll();
    }

    @Override
    public void saveNote(Note note) {
        // TODO Auto-generated method stub
        this.noteRepository.save(note);
    }

    @Override
    public Note getNoteById(Long id) {
        // TODO Auto-generated method stub
        Optional<Note> optional = this.noteRepository.findById(id);
        Note note = null;
        if (optional.isPresent()) {
            note = optional.get();
        }
        return note;
    }

    @Override
    public void deleteNoteById(Long id) {
        // TODO Auto-generated method stub
        this.noteRepository.deleteById(id);
    }
}
