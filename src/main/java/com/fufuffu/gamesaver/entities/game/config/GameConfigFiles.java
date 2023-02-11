package com.fufuffu.gamesaver.entities.game.config;

import java.util.List;

/**
 * Holds a list of all config files for a given game
 */
public class GameConfigFiles {
    private List<ConfigFilePath> files;

    public GameConfigFiles() {
    }

    public GameConfigFiles(List<ConfigFilePath> files) {
        this.files = files;
    }

    public List<ConfigFilePath> getFiles() {
        return files;
    }

    public void setFiles(List<ConfigFilePath> files) {
        this.files = files;
    }
}
