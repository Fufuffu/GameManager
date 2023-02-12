package com.fufuffu.gamesaver.interactor;

import com.fufuffu.gamesaver.entities.ResolvedBasePath;
import com.fufuffu.gamesaver.entities.game.config.ConfigFilePath;
import com.fufuffu.gamesaver.entities.game.config.GameConfigFiles;
import com.fufuffu.gamesaver.entities.game.config.GamePathEnum;
import com.fufuffu.gamesaver.repository.SupportedGameRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ConfigBasePathResolverImpl implements ConfigBasePathResolver {

    private final SupportedGameRepository supportedGameRepository;

    public ConfigBasePathResolverImpl(SupportedGameRepository supportedGameRepository) {
        this.supportedGameRepository = supportedGameRepository;
    }

    @Override
    public List<ResolvedBasePath> resolve(GameConfigFiles configFiles) {
        List<GamePathEnum> neededPaths = configFiles.getFiles().stream()
                .map(ConfigFilePath::getBasePath)
                .distinct()
                .collect(Collectors.toList());

        ArrayList<ResolvedBasePath> resolvedBasePaths = new ArrayList<>();
        for (GamePathEnum gamePath : neededPaths) {
            resolvedBasePaths.add(new ResolvedBasePath(gamePath, askForPath(gamePath)));
        }

        return resolvedBasePaths;
    }

    private Path askForPath(GamePathEnum gamePath) {
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
}
