package com.fpt.haotpv.SuperDuperDrive.service;

import com.fpt.haotpv.SuperDuperDrive.entity.File;

import java.util.List;

public interface FileService {

    Boolean isFileNameAvailable(String fileName, Integer userId);

    File getFileById(Integer id);

    List<File> getAllFilesByUser(Integer userId);

    int uploadFile(File file);

    int deleteFile(Integer fileId);
}
