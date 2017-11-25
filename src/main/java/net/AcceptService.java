package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AcceptService extends Thread {

    private final ServerSocket serverSocket;
    private final NetHandler netHandler;

    public AcceptService(ServerSocket serverSocket, NetHandler netHandler) {
        this.netHandler = netHandler;
        this.serverSocket = serverSocket;
        this.setDaemon(true);
        start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                netHandler.addConnection(clientSocket);
            } catch (IOException ignored) {
            }
        }
    }
}
