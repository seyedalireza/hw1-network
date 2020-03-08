package ir.sarh.server;

import ir.sarh.server.thread.Server;

import java.io.IOException;

public class Main {

    private static final int SERVER_PORT = 8123;

    public static void main(String[] args) throws IOException {
        Server server = new Server(SERVER_PORT);
        server.start();
    }
}
