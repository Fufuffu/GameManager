package com.fufuffu.gamesaver.repository.resource.fs;

import java.io.IOException;
import java.nio.file.Path;

public interface FileSystemResource {
    String readFile(Path path) throws IOException;
}
