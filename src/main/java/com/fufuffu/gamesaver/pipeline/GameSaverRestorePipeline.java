package com.fufuffu.gamesaver.pipeline;

import com.fufuffu.gamesaver.interactor.ConfigFileRestorer;

import java.io.IOException;

public class GameSaverRestorePipeline implements GameSaverPipeline {

    private final ConfigFileRestorer configFileRestorer;

    public GameSaverRestorePipeline(ConfigFileRestorer configFileRestorer) {
        this.configFileRestorer = configFileRestorer;
    }

    @Override
    public void run() throws IOException {
        configFileRestorer.restore();
    }
}
