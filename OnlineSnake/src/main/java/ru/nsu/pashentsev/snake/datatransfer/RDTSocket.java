package ru.nsu.pashentsev.snake.datatransfer;

import ru.nsu.pashentsev.snake.messages.MessageOwner;
import ru.nsu.pashentsev.snake.messages.messages.Message;

import java.net.InetAddress;

public interface RDTSocket {
    Message send(Message message, NetNode receiver);
    void sendNonBlocking(Message message, NetNode receiver);
    void sendWithoutConfirm(Message message, NetNode receiver);
    MessageOwner receive();

    InetAddress getAddress();
    void removePendingMessage(long messageSequence);

    int getLocalPort();

    void start();
    void stop();
}
