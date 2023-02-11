package com.fufuffu.gamesaver.interactor;

import com.fufuffu.gamesaver.entities.ResolvedBasePath;
import com.fufuffu.gamesaver.entities.game.GameListRelation;
import com.fufuffu.gamesaver.entities.game.config.ConfigFilePath;
import com.fufuffu.gamesaver.entities.game.config.GameConfigFiles;
import com.fufuffu.gamesaver.repository.DriveConfigRepository;
import com.fufuffu.gamesaver.repository.SupportedGameRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ConfigFileSaverImpl implements ConfigFileSaver {

    private final UserSelectedGameGetter userSelectedGameGetter;
    private final ConfigBasePathResolver configBasePathResolver;

    private final SupportedGameRepository supportedGameRepository;
    private final DriveConfigRepository driveConfigRepository;

    public ConfigFileSaverImpl(UserSelectedGameGetter userSelectedGameGetter, ConfigBasePathResolver configBasePathResolver, SupportedGameRepository supportedGameRepository, DriveConfigRepository driveConfigRepository) {
        this.userSelectedGameGetter = userSelectedGameGetter;
        this.configBasePathResolver = configBasePathResolver;

        this.supportedGameRepository = supportedGameRepository;
        this.driveConfigRepository = driveConfigRepository;
    }

    @Override
    public void save() throws IOException {
        GameListRelation game = userSelectedGameGetter.get();
        GameConfigFiles gameConfigFiles = supportedGameRepository.readGameConfigFiles(game.getFileWithConfig());
        List<ResolvedBasePath> resolvedPaths = configBasePathResolver.resolve(gameConfigFiles);

        for (ConfigFilePath configFile : gameConfigFiles.getFiles()) {
            ResolvedBasePath basePath = resolvedPaths.stream()
                    .filter(resolvedBasePath -> resolvedBasePath.getPathEnum() == configFile.getBasePath())
                    .findFirst()
                    .get();

            Path fileToRead = Paths.get(basePath.getFileSystemPath().toString(), configFile.getRelativeConfigPath());

            // Discard file if it doesn't exist
            File file = new File(fileToRead.toString());
            if (!file.isFile()) {
                System.out.println("Ignoring file: " + configFile.getRelativeConfigPath() + " since it was not found");
                continue;
            }

            String createdFileId = driveConfigRepository.createFile(configFile.getRelativeConfigPath(), fileToRead, game.getNameInFileSystem());
            System.out.printf("Created file with name: %s , fileIdInDrive: %s %n", configFile.getRelativeConfigPath(), createdFileId);
        }
    }
}
