package main;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ocz on 6/6/16.
 */
public class Proxy {
    private final int bufLen = 1024;
    private String ipAsServer;
    private int portAsServer;
    private String peerIP;
    private int peerPort;

    public Proxy(String ipAsServer, int portAsServer, String peerIP, int peerPort) throws IOException {
        this.ipAsServer = ipAsServer;
        this.portAsServer = portAsServer;
        this.peerIP = peerIP;
        this.peerPort = peerPort;
    }

    public void run() throws IOException {
        ServerSocket serverSocket = new ServerSocket(portAsServer, 128, Inet4Address.getByName(ipAsServer));
        Socket socketWithClient;
        while (true) {
            socketWithClient = serverSocket.accept();
            new Thread(new ClientListener(socketWithClient)).start();
        }
    }

    private class ClientListener implements Runnable {
        Socket socketWithClient;
        Socket socketWithPeer;
        public ClientListener(Socket socketWithClient) throws IOException {
            this.socketWithClient = socketWithClient;
            this.socketWithPeer = new Socket(peerIP, peerPort);
        }

        @Override
        public void run() {
            byte[] b = new byte[bufLen];
            int bytesRead;
            try {
                bytesRead=socketWithClient.getInputStream().read(b);
                socketWithPeer.getOutputStream().write(b, 0, bytesRead);
                socketWithPeer.getOutputStream().flush();

                while ((bytesRead = socketWithPeer.getInputStream().read(b)) != -1) {
                    socketWithClient.getOutputStream().write(b, 0, bytesRead);
                }
                socketWithClient.getOutputStream().flush();
            } catch (IOException e) {
                // hide the exception
            }
        }
    }
}
