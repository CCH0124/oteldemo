package com.otel.example.oteldemo.controller;

import java.util.List;

import javax.validation.Valid;

import com.otel.example.oteldemo.model.Note;
import com.otel.example.oteldemo.service.NoteService;
import com.otel.example.oteldemo.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/")
public class NoteController {
    @Autowired
    NoteService noteService;

    @GetMapping("notes")
    public List<Note> getAllNotes() {
        return this.noteService.getAllNote();
    }

    @PostMapping("notes")
    public void createNote(@Valid @RequestBody Note note) {
        this.noteService.saveNote(note);
    }

    @GetMapping("notes/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable(value = "id") Long noteId) {
        try {
            Note note = this.noteService.getNoteById(noteId);
            if (note == null) {
                return new ResponseEntity<Note>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<Note>(note, HttpStatus.OK);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<Note>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("notes/{id}")
    public ResponseEntity<?> updateNote(@PathVariable(value = "id") Long noteId) {
        try {
            Note note = this.noteService.getNoteById(noteId);
            if (note == null) {
                return new ResponseEntity<Note>(HttpStatus.NOT_FOUND);
            }
            this.noteService.saveNote(note);
            return new ResponseEntity<Note>(HttpStatus.OK);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<Note>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("notes/{id}")
    public void deleteNote(@PathVariable(value = "id") Long noteId) {
        this.noteService.deleteNoteById(noteId);
    }
}
