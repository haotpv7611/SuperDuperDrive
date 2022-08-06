package com.fpt.haotpv.SuperDuperDrive.mapper;

import com.fpt.haotpv.SuperDuperDrive.entity.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    // check duplicate fileName to a single user
    @Select("SELECT * FROM FILES WHERE filename = #{fileName} AND userid = ${userId}")
    File getFileByUsername(String fileName, Integer userId);

    @Select("SELECT * FROM FILES WHERE fileid = #{fileId}")
    File getFileById(Integer fileId);

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> getAllFilesByUserId(Integer userId);

    @Insert("INSERT INTO FILES(filename, contenttype, filesize, userid, filedata) " +
            "VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    @Delete("DELETE FROM FILES WHERE fileid = #{fileId}")
    int delete(Integer fileId);
}
