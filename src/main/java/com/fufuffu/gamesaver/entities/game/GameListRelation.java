package com.fufuffu.gamesaver.entities.game;

/**
 * Represents a supported game. Contains its display name and the file containing further configuration
 */
public class GameListRelation {
    private String nameToShow;
    private String nameInFileSystem;
    private String fileWithConfig;

    public GameListRelation() {
    }

    public GameListRelation(String nameToShow, String nameInFileSystem, String fileWithConfig) {
        this.nameToShow = nameToShow;
        this.nameInFileSystem = nameInFileSystem;
        this.fileWithConfig = fileWithConfig;
    }

    public String getNameToShow() {
        return nameToShow;
    }

    public void setNameToShow(String nameToShow) {
        this.nameToShow = nameToShow;
    }

    public String getNameInFileSystem() {
        return nameInFileSystem;
    }

    public void setNameInFileSystem(String nameInFileSystem) {
        this.nameInFileSystem = nameInFileSystem;
    }

    public String getFileWithConfig() {
        return fileWithConfig;
    }

    public void setFileWithConfig(String fileWithConfig) {
        this.fileWithConfig = fileWithConfig;
    }
}
