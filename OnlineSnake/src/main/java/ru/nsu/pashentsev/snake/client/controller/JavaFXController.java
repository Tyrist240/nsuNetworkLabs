package ru.nsu.pashentsev.snake.client.controller;

import lombok.RequiredArgsConstructor;
import me.zelenskih.fit.snakes.SnakesProto.GameConfig;
import ru.nsu.pashentsev.snake.client.controller.events.JoinToGameEvent;
import ru.nsu.pashentsev.snake.client.controller.events.UserEvent;
import ru.nsu.pashentsev.snake.client.view.GameView;
import ru.nsu.pashentsev.snake.client.network.NetworkHandler;
import ru.nsu.pashentsev.snake.client.controller.events.MoveEvent;

@RequiredArgsConstructor
public final class JavaFXController implements GameController {
    private final GameConfig playerConfig;
    private final String playerName;
    private final NetworkHandler gameNetwork;
    private final GameView view;

    @Override
    public void fireEvent(UserEvent userEvent) {
        switch (userEvent.getType()) {
            case NEW_GAME -> {
                this.view.setConfig(this.playerConfig);
                this.gameNetwork.startNewGame();
            }
            case JOIN_GAME -> {
                JoinToGameEvent joinEvent = (JoinToGameEvent) userEvent;
                this.view.setConfig(joinEvent.getConfig());
                this.gameNetwork.joinToGame(joinEvent.getMasterNode(), this.playerName);
            }
            case MOVE -> {
                MoveEvent moveEvent = (MoveEvent) userEvent;
                this.gameNetwork.handleMove(moveEvent.getDirection());
            }
            case EXIT -> this.gameNetwork.exit();
        }
    }
}
