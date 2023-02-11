package com.fufuffu.gamesaver.interactor;

import com.fufuffu.gamesaver.entities.game.GameList;
import com.fufuffu.gamesaver.entities.game.GameListRelation;
import com.fufuffu.gamesaver.repository.SupportedGameRepository;

import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UserSelectedGameGetterImpl implements UserSelectedGameGetter {

    private final SupportedGameRepository supportedGameRepository;

    public UserSelectedGameGetterImpl(SupportedGameRepository supportedGameRepository) {
        this.supportedGameRepository = supportedGameRepository;
    }

    @Override
    public GameListRelation get() throws IOException {
        GameList gamesAvailable = supportedGameRepository.readGameList();

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
}
