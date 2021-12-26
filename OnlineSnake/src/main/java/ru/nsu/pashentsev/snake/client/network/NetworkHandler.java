package ru.nsu.pashentsev.snake.client.network;

import me.zelenskih.fit.snakes.SnakesProto.Direction;
import ru.nsu.pashentsev.snake.datatransfer.NetNode;
import ru.nsu.pashentsev.snake.multicastreceiver.GameInfo;

import java.util.Set;

public interface NetworkHandler {
    void startNewGame();

    void joinToGame(NetNode gameOwner, String playerName);

    void handleMove(Direction direction);

    void exit();

    void updateActiveGames(Set<GameInfo> gameInfos);
}
