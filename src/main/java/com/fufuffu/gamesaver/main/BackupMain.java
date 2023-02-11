package com.fufuffu.gamesaver.main;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fufuffu.gamesaver.entities.ResolvedBasePath;
import com.fufuffu.gamesaver.entities.game.GameList;
import com.fufuffu.gamesaver.entities.game.GameListRelation;
import com.fufuffu.gamesaver.entities.game.config.ConfigFilePath;
import com.fufuffu.gamesaver.entities.game.config.GameConfigFiles;
import com.fufuffu.gamesaver.entities.game.config.GamePathEnum;
import com.fufuffu.gamesaver.entities.json.JsonUtil;
import com.fufuffu.gamesaver.pipeline.GameSaverBackupPipeline;
import com.fufuffu.gamesaver.repository.resource.drive.GoogleDriveFileResourceImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;

public class BackupMain {

    private static final String SUPPORTED_GAMES_FOLDER = "gameConfigs";
    private static final String GAME_CONFIG_FOLDER = "games";

    private static final String SUPPORTED_GAMES_FILE = "supportedGames.json";

    public static void main(String[] args) throws GeneralSecurityException, IOException {
        GoogleDriveFileResourceImpl driveResource = new GoogleDriveFileResourceImpl();
        GameSaverBackupPipeline pipeline = new GameSaverBackupPipeline(driveResource);

        GameListRelation game = askForGame();

        Path configJsonPath = Paths.get(SUPPORTED_GAMES_FOLDER, GAME_CONFIG_FOLDER, game.getFileWithConfig());
        GameConfigFiles configFiles = readJsonFile(configJsonPath, GameConfigFiles.class);

        List<GamePathEnum> neededPaths = configFiles.getFiles().stream()
                .map(ConfigFilePath::getBasePath)
                .distinct()
                .collect(Collectors.toList());

        ArrayList<ResolvedBasePath> resolvedBasePaths = new ArrayList<>();
        for (GamePathEnum gamePath : neededPaths) {
            resolvedBasePaths.add(new ResolvedBasePath(gamePath, askForPath(gamePath)));
        }

        pipeline.run(game, configFiles, resolvedBasePaths);
    }

    private static Path askForPath(GamePathEnum gamePath) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter the path for your " + gamePath + " : ");
            String stringPath = scanner.nextLine();

            File possibleFile = new File(stringPath);

            if (possibleFile.isDirectory()) {
                return Paths.get(stringPath);
            }

            System.out.println("Please enter a valid path, it was either a file or not a valid path");
        }
    }

    private static GameListRelation askForGame() throws IOException {
        GameList gamesAvailable = readJsonFile(Paths.get(SUPPORTED_GAMES_FOLDER, SUPPORTED_GAMES_FILE), GameList.class);

        String gameNames = gamesAvailable.getGames().stream().map(GameListRelation::getNameToShow).collect(Collectors.toList()).toString();

        Scanner scanner = new Scanner(System.in);
        Optional<GameListRelation> selectedGame = Optional.empty();
        while (!selectedGame.isPresent()) {
            System.out.println("Enter the game you desire to backup, options: " + gameNames + ": ");
            String gameToBackup = scanner.nextLine();
            Optional<GameListRelation> attemptGame = gamesAvailable.getGames()
                    .stream()
                    .filter(gameListRelation -> gameListRelation.getNameToShow().equals(gameToBackup))
                    .findFirst();

            if (attemptGame.isPresent()) {
                selectedGame = attemptGame;
            } else {
                System.out.println("Incorrect game name");
            }
        }

        return selectedGame.get();
    }

    private static <T> T readJsonFile(Path path, Class<T> cls) throws IOException, NoSuchElementException {
        JsonUtil.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,true);
        JsonUtil.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        try {
            StringBuilder result = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(path.toString()))) {
                String line = reader.readLine();
                while (line != null) {
                    result.append(line);
                    line = reader.readLine();
                }

                Optional<T> parsed = JsonUtil.fromJson(result.toString(), cls);

                if (parsed.isPresent()) {
                    return parsed.get();
                } else {
                    throw new NoSuchElementException(path + " JSON was malformed");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
