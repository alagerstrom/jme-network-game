package net;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection<T> extends Thread {

    private final Socket socket;
    private final NetHandler netHandler;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;


    Connection(Socket socket, NetHandler netHandler) throws IOException {
        this.netHandler = netHandler;
        this.socket = socket;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        start();
    }

    @Override
    public void run() {
        while (true){
            try{
                NetMessage netMessage = (NetMessage) inputStream.readObject();
                netHandler.handleIncomingMessage(netMessage);
            }catch (EOFException e){
                netHandler.removeConnection(Connection.this);
                break;
            }catch (Exception e){
                System.err.println(e.getMessage());
            }
        }
    }

    public synchronized void send(NetMessage netMessage) throws IOException {
        outputStream.writeObject(netMessage);
    }
}
