package ru.nsu.pashentsev.snake.messages.messages;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.zelenskih.fit.snakes.SnakesProto;

import java.io.Serializable;

@RequiredArgsConstructor
public abstract class Message implements Serializable {
    private final @Getter MessageType type;
    private final @Getter long messageSequence;
    private final @Getter int senderID;
    private final @Getter int receiverID;

    public abstract SnakesProto.GameMessage getGameMessage();
}
