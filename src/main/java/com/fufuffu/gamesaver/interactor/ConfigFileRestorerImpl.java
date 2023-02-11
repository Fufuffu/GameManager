package com.fufuffu.gamesaver.interactor;

import com.fufuffu.gamesaver.entities.ResolvedBasePath;
import com.fufuffu.gamesaver.entities.game.GameListRelation;
import com.fufuffu.gamesaver.entities.game.config.ConfigFilePath;
import com.fufuffu.gamesaver.entities.game.config.GameConfigFiles;
import com.fufuffu.gamesaver.repository.DriveConfigRepository;
import com.fufuffu.gamesaver.repository.SupportedGameRepository;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ConfigFileRestorerImpl implements ConfigFileRestorer {

    private final UserSelectedGameGetter userSelectedGameGetter;
    private final ConfigBasePathResolver configBasePathResolver;

    private final SupportedGameRepository supportedGameRepository;
    private final DriveConfigRepository driveConfigRepository;

    public ConfigFileRestorerImpl(UserSelectedGameGetter userSelectedGameGetter, ConfigBasePathResolver configBasePathResolver, SupportedGameRepository supportedGameRepository, DriveConfigRepository driveConfigRepository) {
        this.userSelectedGameGetter = userSelectedGameGetter;
        this.configBasePathResolver = configBasePathResolver;

        this.supportedGameRepository = supportedGameRepository;
        this.driveConfigRepository = driveConfigRepository;
    }

    @Override
    public void restore() throws IOException {
        GameListRelation game = userSelectedGameGetter.get();
        GameConfigFiles gameConfigFiles = supportedGameRepository.readGameConfigFiles(game.getFileWithConfig());
        List<ResolvedBasePath> resolvedPaths = configBasePathResolver.resolve(gameConfigFiles);

        for (ConfigFilePath configFile : gameConfigFiles.getFiles()) {
            ResolvedBasePath basePath = resolvedPaths.stream()
                    .filter(resolvedBasePath -> resolvedBasePath.getPathEnum() == configFile.getBasePath())
                    .findFirst()
                    .get();

            Path fileToSave = Paths.get(basePath.getFileSystemPath().toString(), configFile.getRelativeConfigPath());

            String fileId = null;
            try {
                fileId = driveConfigRepository.getFileIdIfExists(configFile.getRelativeConfigPath(), game.getNameInFileSystem());
            } catch (IOException e) {
                // Do nothing, expected when file not found
            }

            if (fileId == null) {
                System.out.println("Ignoring file: " + configFile.getRelativeConfigPath() + " since it was not found");
                continue;
            }

            ByteArrayOutputStream outputStream = driveConfigRepository.getFileContent(fileId, game.getNameInFileSystem());
            try (OutputStream outputFile = new FileOutputStream(fileToSave.toString())) {
                outputStream.writeTo(outputFile);
            }
            System.out.printf("Restored file with name: %s , fileIdInDrive: %s %n", configFile.getRelativeConfigPath(), fileId);
        }
    }
}
