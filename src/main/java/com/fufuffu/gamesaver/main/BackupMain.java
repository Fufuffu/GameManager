package com.fufuffu.gamesaver.main;

import com.fufuffu.gamesaver.interactor.ConfigBasePathResolverImpl;
import com.fufuffu.gamesaver.interactor.ConfigFileSaverImpl;
import com.fufuffu.gamesaver.interactor.UserSelectedGameGetterImpl;
import com.fufuffu.gamesaver.pipeline.GameSaverBackupPipeline;
import com.fufuffu.gamesaver.repository.DriveConfigRepositoryImpl;
import com.fufuffu.gamesaver.repository.SupportedGameRepositoryImpl;
import com.fufuffu.gamesaver.repository.resource.drive.GoogleDriveFileResourceImpl;
import com.fufuffu.gamesaver.repository.resource.fs.FileSystemResourceImpl;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class BackupMain {
    public static void main(String[] args) throws GeneralSecurityException, IOException {
        // Instantiate resources
        GoogleDriveFileResourceImpl googleDriveFileResource = new GoogleDriveFileResourceImpl();
        FileSystemResourceImpl fileSystemResource = new FileSystemResourceImpl();

        // Instantiate repositories
        SupportedGameRepositoryImpl supportedGameRepository = new SupportedGameRepositoryImpl(fileSystemResource);
        DriveConfigRepositoryImpl driveConfigRepository = new DriveConfigRepositoryImpl(googleDriveFileResource);

        // Instantiate interactors
        UserSelectedGameGetterImpl userSelectedGameGetter = new UserSelectedGameGetterImpl(supportedGameRepository);
        ConfigBasePathResolverImpl configBasePathResolver = new ConfigBasePathResolverImpl(supportedGameRepository);

        ConfigFileSaverImpl configFileSaver = new ConfigFileSaverImpl(userSelectedGameGetter,
                configBasePathResolver, supportedGameRepository, driveConfigRepository);


        // Instantiate and run pipeline
        GameSaverBackupPipeline pipeline = new GameSaverBackupPipeline(configFileSaver);
        pipeline.run();
    }
}
