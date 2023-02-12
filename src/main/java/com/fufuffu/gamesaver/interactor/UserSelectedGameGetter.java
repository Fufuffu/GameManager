package com.fufuffu.gamesaver.interactor;

import com.fufuffu.gamesaver.entities.game.GameListRelation;

import java.io.IOException;

public interface UserSelectedGameGetter {
    GameListRelation get() throws IOException;
}
