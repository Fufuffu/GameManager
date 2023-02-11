package com.fufuffu.gamesaver.entities.game.config;

/**
 * Holds all possible base paths for a config file
 * GAME_FOLDER -> Game installation directory
 * STEAM_USERDATA -> Steam installation directory, followed by /userdata/accountID/
 * DOCUMENTS -> Documents folder in Windows
 * APPDATA_LOCAL -> App data local folder in Windows
 */
public enum GamePathEnum {
    GAME_FOLDER("GameFolder"),
    STEAM_USERDATA("SteamUserdata"),
    DOCUMENTS("Documents"),
    APPDATA_LOCAL("AppDataLocal");

    private final String text;

    GamePathEnum(final String text) {
        this.text = text;
    }

    public static GamePathEnum fromString(String text) {
        for (GamePathEnum b : GamePathEnum.values()) {
            if (b.text.equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }

    @Override
    public String toString() {
        return text;
    }
}