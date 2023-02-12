package com.fufuffu.gamesaver.entities;

import com.fufuffu.gamesaver.entities.game.config.GamePathEnum;

import java.nio.file.Path;

public class ResolvedBasePath {
    private final GamePathEnum pathEnum;
    private final Path fileSystemPath;

    public ResolvedBasePath(GamePathEnum pathEnum, Path fileSystemPath) {
        this.pathEnum = pathEnum;
        this.fileSystemPath = fileSystemPath;
    }

    public GamePathEnum getPathEnum() {
        return pathEnum;
    }

    public Path getFileSystemPath() {
        return fileSystemPath;
    }
}
