package ru.nsu.pashentsev.snake.client.controller.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class UserEvent {
    private final @Getter UserEventType type;
}
