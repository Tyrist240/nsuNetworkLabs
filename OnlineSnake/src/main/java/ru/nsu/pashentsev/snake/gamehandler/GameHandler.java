package ru.nsu.pashentsev.snake.gamehandler;

import static me.zelenskih.fit.snakes.SnakesProto.Direction;
import org.jetbrains.annotations.NotNull;
import ru.nsu.pashentsev.snake.datatransfer.NetNode;

import java.util.Map;

public interface GameHandler {
    Player registerNewPlayer(@NotNull String playerName, NetNode netNode);

    void removePlayer(Player player);

    void moveAllSnakes(Map<Player, Direction> playersMoves);

    Snake getSnakeByPlayer(Player player);
}
