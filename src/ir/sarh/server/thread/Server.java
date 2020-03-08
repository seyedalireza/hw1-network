package ir.sarh.server.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

    private ServerSocket serverSocket;

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        System.out.println("Server started successfully.");
        while (true) {
            try {
                Socket userSocket = serverSocket.accept();
                new UserControllerThread(userSocket).start();
                Thread.sleep(500);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
