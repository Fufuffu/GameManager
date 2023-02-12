package com.fufuffu.gamesaver.repository;

import com.fufuffu.gamesaver.repository.resource.drive.GoogleDriveFileResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class DriveConfigRepositoryImpl implements DriveConfigRepository {

    private final String GAME_SAVER_BASE_FOLDER = "GameSaverData";

    private final GoogleDriveFileResource googleDriveFileResource;

    private String baseFolderId = null;
    private String gameFolderId = null;

    public DriveConfigRepositoryImpl(GoogleDriveFileResource googleDriveFileResource) {
        this.googleDriveFileResource = googleDriveFileResource;
    }

    @Override
    public String createFile(String fileId, Path fileContentPath, String gameNameInFs) throws IOException {
        initFolders(gameNameInFs);

        return googleDriveFileResource.createFile(fileId, gameFolderId, fileContentPath.toFile());
    }

    @Override
    public String getFileIdIfExists(String fileId, String gameNameInFs) throws IOException {
        initFolders(gameNameInFs);

        return googleDriveFileResource.getFileIdIfExists(gameFolderId, fileId);
    }

    @Override
    public ByteArrayOutputStream getFileContent(String fileId, String gameNameInFs) throws IOException {
        initFolders(gameNameInFs);

        return googleDriveFileResource.getFileContent(fileId);
    }


    private void initFolders(String gameNameInFs) throws IOException {
        if (baseFolderId == null) {
            baseFolderId = googleDriveFileResource.makeDirectoryIfNotExists(GAME_SAVER_BASE_FOLDER, "root");
        }

        if (gameFolderId == null) {
            gameFolderId = googleDriveFileResource.makeDirectoryIfNotExists(gameNameInFs, baseFolderId);
        }
    }
}
