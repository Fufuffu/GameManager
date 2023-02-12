package com.fufuffu.gamesaver.repository;

import com.fufuffu.gamesaver.entities.game.GameList;
import com.fufuffu.gamesaver.entities.game.config.GameConfigFiles;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public interface SupportedGameRepository {
    GameList readGameList() throws IOException;

    GameConfigFiles readGameConfigFiles(String gameConfigFileName) throws IOException;

    void writeConfigFile(ByteArrayOutputStream fileStream, Path filePath) throws IOException;
}
