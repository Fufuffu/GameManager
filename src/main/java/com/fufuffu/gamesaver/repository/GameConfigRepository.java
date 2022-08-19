package com.fufuffu.gamesaver.repository;

import com.fufuffu.gamesaver.entities.GameConfig;

public interface GameConfigRepository {
    void saveGame(GameConfig config);
    GameConfig getGame(String gameName);
}
