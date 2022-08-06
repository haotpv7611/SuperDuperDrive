package com.fpt.haotpv.SuperDuperDrive.service.impl;

import com.fpt.haotpv.SuperDuperDrive.entity.Credential;
import com.fpt.haotpv.SuperDuperDrive.entity.File;
import com.fpt.haotpv.SuperDuperDrive.mapper.FileMapper;
import com.fpt.haotpv.SuperDuperDrive.service.FileService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    private final FileMapper mapper;

    public FileServiceImpl(FileMapper userMapper) {
        this.mapper = userMapper;
    }

    @Override
    public Boolean isFileNameAvailable(String fileName,
                                       Integer userId) {

        return mapper.getFileByUsername(fileName, userId) == null;
    }

    @Override
    public File getFileById(Integer id) {

        return this.mapper.getFileById(id);
    }

    @Override
    public List<File> getAllFilesByUser(Integer userId) {

        return this.mapper.getAllFilesByUserId(userId);
    }

    @Override
    public int uploadFile(File file) {

        return this.mapper.insert(file);
    }

    @Override
    public int deleteFile(Integer fileId) {

        return this.mapper.delete(fileId);
    }
}
