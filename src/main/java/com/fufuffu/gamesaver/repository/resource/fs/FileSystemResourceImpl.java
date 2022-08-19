package com.fufuffu.gamesaver.repository.resource.fs;

import com.fufuffu.gamesaver.repository.resource.FileResource;

import java.io.*;

public class FileSystemResourceImpl implements FileResource {

    @Override
    public boolean createFile(String path, String content) {
        try {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, false))) {
                writer.write(content);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }

    @Override
    public boolean appendFile(String path, String newContent) {
        try {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
                writer.write(newContent);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
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

    @Override
    public boolean makeDirectory(String path) {
        File file = new File(path);
        return file.mkdirs();
    }
}
