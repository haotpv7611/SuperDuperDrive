package com.fpt.haotpv.SuperDuperDrive.mapper;

import com.fpt.haotpv.SuperDuperDrive.entity.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    // check duplicate note to a single user
    @Select("SELECT * FROM NOTES WHERE notetitle = #{noteTitle} AND notedescription = #{noteDescription} AND userid = ${userId}")
    Note findNoteByTitleAndDescriptionAndUserId(String noteTitle, String noteDescription, Integer userId);

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteId}")
    Note findById(Integer noteId);

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Note> findAllByUserId(Integer userId);

    @Insert("INSERT INTO NOTES(notetitle, notedescription, userid) " +
            "VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insert(Note note);

    @Update("UPDATE NOTES SET notetitle = #{noteTitle}, notedescription = #{noteDescription} WHERE noteid = #{noteId}")
    int update(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
    int delete(Integer noteId);
}
