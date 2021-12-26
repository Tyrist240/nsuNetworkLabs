package ru.nsu.pashentsev.snake.client.controller.events;

public final class ExitEvent extends UserEvent {
    public ExitEvent() {
        super(UserEventType.EXIT);
    }
}
