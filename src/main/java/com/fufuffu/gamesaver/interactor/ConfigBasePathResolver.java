package com.fufuffu.gamesaver.interactor;

import com.fufuffu.gamesaver.entities.ResolvedBasePath;
import com.fufuffu.gamesaver.entities.game.config.GameConfigFiles;

import java.io.IOException;
import java.util.List;


public interface ConfigBasePathResolver {
    List<ResolvedBasePath> resolve(GameConfigFiles configFiles);
}
