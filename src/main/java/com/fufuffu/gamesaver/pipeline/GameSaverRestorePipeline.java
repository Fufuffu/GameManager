package com.fufuffu.gamesaver.pipeline;

import com.fufuffu.gamesaver.entities.ResolvedBasePath;
import com.fufuffu.gamesaver.entities.game.GameListRelation;
import com.fufuffu.gamesaver.entities.game.config.ConfigFilePath;
import com.fufuffu.gamesaver.entities.game.config.GameConfigFiles;
import com.fufuffu.gamesaver.repository.resource.drive.GoogleDriveFileResourceImpl;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GameSaverRestorePipeline implements GameSaverPipeline {

    private final GoogleDriveFileResourceImpl driveFileResource;

    public GameSaverRestorePipeline(GoogleDriveFileResourceImpl driveFileResource) {
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

            Path fileToSave = Paths.get(basePath.getFileSystemPath().toString(), configFile.getRelativeConfigPath());

            String fileId = null;
            try {
                fileId = driveFileResource.getFileIdIfExists(gameFolderId, configFile.getRelativeConfigPath());
            } catch (IOException e) {
                // Do nothing, expected when file not found
            }

            if (fileId == null) {
                continue;
            }

            ByteArrayOutputStream outputStream = driveFileResource.getFileContent(fileId);
            try (OutputStream outputFile = new FileOutputStream(fileToSave.toString())) {
                outputStream.writeTo(outputFile);
            }
        }
    }
}
