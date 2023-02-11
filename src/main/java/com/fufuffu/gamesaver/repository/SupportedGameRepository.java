package com.fufuffu.gamesaver.repository;

import com.fufuffu.gamesaver.entities.game.GameList;
import com.fufuffu.gamesaver.entities.game.config.GameConfigFiles;

import java.io.IOException;

public interface SupportedGameRepository {
    GameList readGameList() throws IOException;

    GameConfigFiles readGameConfigFiles(String gameConfigFileName) throws IOException;
}
