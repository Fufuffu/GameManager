package com.fufuffu.gamesaver.repository.resource.drive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface GoogleDriveFileResource {
    String createFile(String fileName, String parentFolder, java.io.File fileContent) throws IOException;

    boolean deleteFile(String filePath) throws IOException;

    String getFileIdIfExists(String parentFolder, String fileName) throws IOException;

    ByteArrayOutputStream getFileContent(String filePath) throws IOException;

    String makeDirectoryIfNotExists(String dirName, String parent) throws IOException;
}
