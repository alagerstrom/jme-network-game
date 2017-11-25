package client.app;

import net.NetHandler;

import java.io.IOException;

public class NetDelegate implements NetHandler.Delegate<Message> {

    private NetHandler<Message> netHandler;
    private Application application;
    private final int port;
    private static final int DEFAULT_PORT = 3333;
    private static final int PORT_UPPER_LIMIT = 3343;
    private String uniqueName;

    public NetDelegate(Application application) throws IOException {
        this.application = application;

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
        connect();
    }


    public void onNewMessage(Message message, String sender) {
        application.incomingMessage(message, sender);
    }

    public void peerNotResponding(String uniqueName) {

    }

    @Override
    public void onNewConnection(String uniqueName) {
        if (!uniqueName.equals(this.uniqueName)){
            System.out.println("Player joined the game: " + uniqueName);
            application.newConnectedPlayer(uniqueName);
        }
    }

    private void connect(){

        String[] hosts = {"localhost", "192.168.0.14"};
        for (int i = 0; i < hosts.length; i++){
            String host = hosts[i];
            for (int port = DEFAULT_PORT; port < PORT_UPPER_LIMIT; port++){
                if (port == this.port)
                    continue;
                try {
                    netHandler.connectTo(host, port);
                    System.out.println("Connected to " + host + ":" + port);
                } catch (IOException ignored) {

                }
            }
        }


    }

    public void send(Message message) {
        try {
            netHandler.sendMessage(message);
        } catch (IOException e) {
            System.err.println("Failed to send location");
        }
    }
}
