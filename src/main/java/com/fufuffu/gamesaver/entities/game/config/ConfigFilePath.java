package com.fufuffu.gamesaver.entities.game.config;

/**
 * Represents a config file to be saved in the given basePath
 */
public class ConfigFilePath {
    private String basePath;
    private String relativeConfigPath;

    public ConfigFilePath() {
    }

    public ConfigFilePath(String basePath, String relativeConfigPath) {
        this.basePath = basePath;
        this.relativeConfigPath = relativeConfigPath;
    }

    public GamePathEnum getBasePath() throws IllegalArgumentException {
        return GamePathEnum.fromString(basePath);
    }

    public String getRelativeConfigPath() {
        return relativeConfigPath;
    }
}
