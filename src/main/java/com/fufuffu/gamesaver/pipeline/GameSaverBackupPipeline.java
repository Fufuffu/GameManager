package com.fufuffu.gamesaver.pipeline;

import com.fufuffu.gamesaver.interactor.ConfigFileSaver;

import java.io.IOException;

public class GameSaverBackupPipeline implements GameSaverPipeline {

    private final ConfigFileSaver configFileSaver;

    public GameSaverBackupPipeline(ConfigFileSaver configFileSaver) {
        this.configFileSaver = configFileSaver;
    }

    @Override
    public void run() throws IOException {
        configFileSaver.save();
    }
}
