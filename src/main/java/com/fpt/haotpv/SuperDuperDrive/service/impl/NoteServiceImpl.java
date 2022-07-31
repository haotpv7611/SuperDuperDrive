package com.fpt.haotpv.SuperDuperDrive.service.impl;

import com.fpt.haotpv.SuperDuperDrive.entity.Note;
import com.fpt.haotpv.SuperDuperDrive.mapper.NoteMapper;
import com.fpt.haotpv.SuperDuperDrive.service.NoteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteMapper mapper;

    public NoteServiceImpl(NoteMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Note getNoteById(Integer id) {

        return this.mapper.findById(id);
    }

    @Override
    public List<Note> getAllNotesByUser(Integer userId) {

        return mapper.findAllByUserId(userId);
    }

    @Override
    public int createNote(Note note) {

        return this.mapper.insert(note);
    }

    @Override
    public int updateNote(Note note) {

        return this.mapper.update(note);
    }

    @Override
    public int deleteNote(Integer noteId) {

        return this.mapper.delete(noteId);
    }
}
