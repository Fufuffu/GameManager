package com.fufuffu.gamesaver.repository.resource.fs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public interface FileSystemResource {
    String readFile(Path path) throws IOException;

    void writeByteStream(ByteArrayOutputStream outputStream, Path path) throws IOException;
}
