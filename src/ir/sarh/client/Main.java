package ir.sarh.client;

import ir.sarh.client.Thread.Client;

import java.io.IOException;

public class Main {

    private static final String SERVER_IP = "localhost";
    private static final int PORT = 8123;

    public static void main(String[] args) throws IOException {
        new Client(SERVER_IP, PORT).start();
    }
}
