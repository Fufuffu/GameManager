package com.fufuffu.gamesaver.repository.resource.fs;

import java.io.*;
import java.nio.file.Path;

public class FileSystemResourceImpl implements FileSystemResource {

    @Override
    public String readFile(Path path) throws IOException {
        try {
            StringBuilder result = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(path.toString()))) {
                String line = reader.readLine();
                while (line != null) {
                    result.append(line);
                    line = reader.readLine();
                }

                return result.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void writeByteStream(ByteArrayOutputStream outputStream, Path path) throws IOException {
        try (OutputStream outputFile = new FileOutputStream(path.toString())) {
            outputStream.writeTo(outputFile);
        }
    }
}
