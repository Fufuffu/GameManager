package com.fufuffu.gamesaver.repository.resource;

public interface FileResource {
    boolean createFile(String path, String content);
    boolean deleteFile(String path);
    boolean appendFile(String path, String newContent);
    String getFileContent(String path);

    boolean makeDirectory(String path);
}
