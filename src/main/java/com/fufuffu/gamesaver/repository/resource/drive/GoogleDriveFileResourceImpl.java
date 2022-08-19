package com.fufuffu.gamesaver.repository.resource.drive;

import com.fufuffu.gamesaver.repository.resource.FileResource;

public class GoogleDriveFileResourceImpl implements FileResource {
    @Override
    public boolean createFile(String path, String content) {
        return false;
    }

    @Override
    public boolean deleteFile(String path) {
        return false;
    }

    @Override
    public boolean appendFile(String path, String newContent) {
        return false;
    }

    @Override
    public String getFileContent(String path) {
        return null;
    }

    @Override
    public boolean makeDirectory(String path) {
        return false;
    }
}
