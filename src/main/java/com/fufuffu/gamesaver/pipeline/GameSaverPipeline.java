package com.fufuffu.gamesaver.pipeline;

public class GameSaverPipeline {

    public GameSaverPipeline() {

    }

    public void run() {
        // Get current application data (fetch general config.json)
        // If backup:
        //      Get list of games to save
        //      Foreach:
        //          Fetch files, save them into game directory
        // If restore:
        //      Get list of games to restore
        //      Foreach:
        //          Read everything from folder, save according to config, each file in it's path
    }
}
