package com.fufuffu.gamesaver.pipeline;

import com.fufuffu.gamesaver.entities.ResolvedBasePath;
import com.fufuffu.gamesaver.entities.game.GameListRelation;
import com.fufuffu.gamesaver.entities.game.config.GameConfigFiles;

import java.io.IOException;
import java.util.List;

public interface GameSaverPipeline {
    final String GAME_SAVER_BASE_FOLDER = "GameSaverData";

    void run(GameListRelation game, GameConfigFiles configFiles, List<ResolvedBasePath> resolvedPaths) throws IOException;
}
