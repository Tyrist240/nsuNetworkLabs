package server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static String PATH_TO_DIR = "." + File.separator + "uploads" + File.separator;

    private final int port;

    private final Timer timer;

    private final ExecutorService threadPool;

    private Server(String port) throws IOException {
        this.port = Integer.parseInt(port);
        this.threadPool = Executors.newCachedThreadPool();
        this.createDir();
        this.timer = new Timer();

        this.start();
    }

    public static void main(String[] args) throws IOException {
        new Server(args[0]);
    }

    private void createDir() {
        if (new File(PATH_TO_DIR).exists()) {
            new File(PATH_TO_DIR).delete();
        }

        new File(PATH_TO_DIR).mkdir();
    }

    private void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                CompletableFuture.supplyAsync(
                    new Handle(socket, this),
                    threadPool
                ).whenComplete((status, e) -> {
                    System.out.println(status.toString());
                    System.out.println();
                });
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public Timer getTimer() {
        return timer;
    }

}