package ru.nsu.pashentsev.snake.server;

import ru.nsu.pashentsev.snake.gamehandler.GameState;

public interface ServerHandler {
    void update(GameState state);

    int getPort();

    void stop();
}
