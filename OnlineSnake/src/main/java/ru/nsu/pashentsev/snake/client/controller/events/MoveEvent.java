package ru.nsu.pashentsev.snake.client.controller.events;

import lombok.Getter;
import me.zelenskih.fit.snakes.SnakesProto.Direction;

public final class MoveEvent extends UserEvent {
    private final @Getter Direction direction;

    public MoveEvent(Direction direction) {
        super(UserEventType.MOVE);
        this.direction = direction;
    }
}
