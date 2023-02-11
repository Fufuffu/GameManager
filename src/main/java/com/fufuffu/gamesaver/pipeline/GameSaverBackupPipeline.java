package com.fufuffu.gamesaver.pipeline;

import com.fufuffu.gamesaver.entities.ResolvedBasePath;
import com.fufuffu.gamesaver.entities.game.GameListRelation;
import com.fufuffu.gamesaver.entities.game.config.ConfigFilePath;
import com.fufuffu.gamesaver.entities.game.config.GameConfigFiles;
import com.fufuffu.gamesaver.repository.resource.drive.GoogleDriveFileResourceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GameSaverBackupPipeline implements GameSaverPipeline {

    private final GoogleDriveFileResourceImpl driveFileResource;

    public GameSaverBackupPipeline(GoogleDriveFileResourceImpl driveFileResource) {
        this.driveFileResource = driveFileResource;
    }

    @Override
    public void run(GameListRelation game, GameConfigFiles configFiles, List<ResolvedBasePath> resolvedPaths) throws IOException {
        String baseFolderId = driveFileResource.makeDirectoryIfNotExists(GAME_SAVER_BASE_FOLDER, "root");
        String gameFolderId = driveFileResource.makeDirectoryIfNotExists(game.getNameInFileSystem(), baseFolderId);

        for (ConfigFilePath configFile : configFiles.getFiles()) {
            ResolvedBasePath basePath = resolvedPaths.stream()
                    .filter(resolvedBasePath -> resolvedBasePath.getPathEnum() == configFile.getBasePath())
                    .findFirst()
                    .get();

            Path fileToRead = Paths.get(basePath.getFileSystemPath().toString(), configFile.getRelativeConfigPath());

            // Discard file if it doesn't exist
            File file = new File(fileToRead.toString());
            if (!file.isFile()) {
                continue;
            }

            String createdFileId = driveFileResource.createFile(configFile.getRelativeConfigPath(), gameFolderId, file);
            System.out.printf("Created file with name: %s , fileIdInDrive: %s %n", configFile.getRelativeConfigPath(), createdFileId);
        }
    }
}
