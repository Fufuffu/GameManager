package com.fufuffu.gamesaver.repository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public interface DriveConfigRepository {
    String createFile(String fileId, Path fileContentPath, String gameNameInFs) throws IOException;

    String getFileIdIfExists(String fileId, String gameNameInFs) throws IOException;

    ByteArrayOutputStream getFileContent(String fileId, String gameNameInFs) throws IOException;
}
