package com.otel.example.oteldemo.repository;

import com.otel.example.oteldemo.model.Note;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
    
}