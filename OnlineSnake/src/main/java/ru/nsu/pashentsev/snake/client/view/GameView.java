package ru.nsu.pashentsev.snake.client.view;

import me.zelenskih.fit.snakes.SnakesProto.GameConfig;
import org.jetbrains.annotations.NotNull;
import ru.nsu.pashentsev.snake.datatransfer.NetNode;
import ru.nsu.pashentsev.snake.gamehandler.GameState;
import ru.nsu.pashentsev.snake.multicastreceiver.GameInfo;

import java.util.Collection;

public interface GameView {
    void setConfig(@NotNull GameConfig gameConfig);
    void setMyPlayer(NetNode self);
    void updateCurrentGame(GameState state);
    void updateGameList(@NotNull Collection<GameInfo> gameInfos);
}
