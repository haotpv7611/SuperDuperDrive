package com.fpt.haotpv.SuperDuperDrive.service;

import com.fpt.haotpv.SuperDuperDrive.entity.Note;

import java.util.List;

public interface NoteService {

    Note getNoteById(Integer noteId);

    List<Note> getAllNotesByUser(Integer userId);

    int createNote(Note note);

    int updateNote(Note note);

    int deleteNote(Integer noteId);
}
