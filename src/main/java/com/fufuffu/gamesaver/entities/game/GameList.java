package com.fufuffu.gamesaver.entities.game;

import java.util.List;

/**
 * Holds a list of all supported games with their display name and file containing its config files
 */
public class GameList {
    private List<GameListRelation> games;

    public GameList() {
    }

    public GameList(List<GameListRelation> games) {
        this.games = games;
    }

    public List<GameListRelation> getGames() {
        return games;
    }

    public void setGames(List<GameListRelation> games) {
        this.games = games;
    }
}
