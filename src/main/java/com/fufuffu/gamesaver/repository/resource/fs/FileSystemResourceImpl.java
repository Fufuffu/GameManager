package com.fufuffu.gamesaver.repository.resource.fs;

import java.io.*;
import java.nio.file.Paths;

public class FileSystemResourceImpl {

    public FileSystemResourceImpl() {
    }

    public boolean createFile(String fileName, String parentFolder, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Paths.get(parentFolder, fileName).toString(), false))) {
            writer.write(content);
        }
        return true;
    }

    public boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }

    public String getFileContent(String path) {
        try {
            StringBuilder result = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                while (reader.readLine() != null) {
                    result.append(reader.readLine());
                }
                return result.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String makeDirectoryIfNotExists(String dirName, String parent) throws IOException {
        File file = new File(Paths.get(parent, dirName).toString());
        if (file.mkdirs()) {
            return dirName;
        }

        throw new IOException("Could not create a folder: " + dirName);
    }
}
