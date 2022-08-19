package com.fufuffu.gamesaver.entities;

public class FilePath {
    private GameDirectory directory;
    private String relativePath;
    private String fileName;

    public FilePath(String relativePath, String fileName) {
        this.relativePath = relativePath;
        this.fileName = fileName;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public GameDirectory getDirectory() {
        return directory;
    }

    public void setDirectory(GameDirectory directory) {
        this.directory = directory;
    }
}
