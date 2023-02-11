package com.fufuffu.gamesaver.repository;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fufuffu.gamesaver.entities.game.GameList;
import com.fufuffu.gamesaver.entities.game.config.GameConfigFiles;
import com.fufuffu.gamesaver.entities.json.JsonUtil;
import com.fufuffu.gamesaver.repository.resource.fs.FileSystemResource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Optional;

public class SupportedGameRepositoryImpl implements SupportedGameRepository {
    private static final String SUPPORTED_GAMES_FOLDER = "gameConfigs";
    private static final String GAME_CONFIG_FOLDER = "games";

    private static final String SUPPORTED_GAMES_FILE = "supportedGames.json";

    private final FileSystemResource fileSystemResource;

    public SupportedGameRepositoryImpl(FileSystemResource fileSystemResource) {
        this.fileSystemResource = fileSystemResource;

        JsonUtil.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        JsonUtil.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    @Override
    public GameList readGameList() throws NoSuchElementException, IOException {
        Path gameListPath = Paths.get(SUPPORTED_GAMES_FOLDER, SUPPORTED_GAMES_FILE);
        String gameListString = fileSystemResource.readFile(gameListPath);

        return convertToJson(gameListString, GameList.class);
    }

    @Override
    public GameConfigFiles readGameConfigFiles(String gameConfigFileName) throws IOException {
        Path configJsonPath = Paths.get(SUPPORTED_GAMES_FOLDER, GAME_CONFIG_FOLDER, gameConfigFileName);
        String gameConfigFileString = fileSystemResource.readFile(configJsonPath);

        return convertToJson(gameConfigFileString, GameConfigFiles.class);
    }

    private <T> T convertToJson(String path, Class<T> cls) {
        Optional<T> parsed = JsonUtil.fromJson(path, cls);
        if (parsed.isPresent()) {
            return parsed.get();
        } else {
            throw new NoSuchElementException(path + " JSON was malformed");
        }
    }
}
