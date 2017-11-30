package client.app;

import net.NetHandler;

import java.io.IOException;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CompletableFuture;

public class NetDelegate implements NetHandler.Delegate<Message> {

    private NetHandler<Message> netHandler;
    private GameState gameState;
    private final int port;
    private static final int DEFAULT_PORT = 3333;
    private static final int PORT_UPPER_LIMIT = 3343;
    private String uniqueName;

    NetDelegate(GameState gameState) throws IOException {
        this.gameState = gameState;

        int port = DEFAULT_PORT;
        int lastPort = PORT_UPPER_LIMIT;
        boolean success = false;
        while (!success && port < lastPort){
            try {
                netHandler = new NetHandler<>(port, this);
                success = true;
            }catch (IOException ignored){
                port++;
            }
        }
        if (!success) {
            throw new IOException("Could not find a port to use");
        }
        this.port = port;
        uniqueName = netHandler.getUniqueName();
        System.out.println("Me: " + uniqueName);
        System.out.println("Listening on port " + port);
    }


    public void onNewMessage(Message message, String sender) {
        gameState.incomingMessage(message, sender);
    }

    public void peerNotResponding(String uniqueName) {

    }

    @Override
    public void onNewConnection(String uniqueName) {
        if (!uniqueName.equals(this.uniqueName)){
            System.out.println("Player joined the game: " + uniqueName);
            gameState.newConnectedPlayer(uniqueName);
        }
    }

    public void connectTo(String host, int port, CompletionHandler<Void, Void> completionHandler){
        CompletableFuture.runAsync(()->{
            try {
                netHandler.connectTo(host, port);
                completionHandler.completed(null,null);
            } catch (IOException e) {
                completionHandler.failed(e, null);
            }
        });
    }

    public void send(Message message) {
        try {
            netHandler.sendMessage(message);
        } catch (IOException e) {
            System.err.println("Failed to send location");
        }
    }
}
