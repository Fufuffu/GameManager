package com.fufuffu.gamesaver.repository.resource.fs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
}
