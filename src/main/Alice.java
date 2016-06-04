package main;

import java.io.*;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by ocz on 6/4/16.
 */
public class Alice {

    public Alice() throws IOException {
        ServerSocket serverSocket = new ServerSocket(7000, 50, Inet4Address.getByName("127.0.0.1"));
        // listen on 127.0.0.1:7000
        Socket socketWithClient;
        while (true) {
            socketWithClient = serverSocket.accept();
            new Thread(new HandleAClient(socketWithClient)).start();
        }
    }


    private class HandleAClient implements Runnable {
        Socket socketWithClient;
        Socket socketWithTransfer;
        public HandleAClient(Socket socketWithClient) throws IOException {
            this.socketWithClient = socketWithClient;
            this.socketWithTransfer = new Socket("172.19.0.1", 9000);
        }

        @Override
        public void run() {
            byte[] b = new byte[10240];
            try {
                int bytesRead = socketWithClient.getInputStream().read(b);
                socketWithTransfer.getOutputStream().write(b, 0, bytesRead);
                socketWithTransfer.getOutputStream().flush();
                while ((bytesRead = socketWithTransfer.getInputStream().read(b)) != -1) {
                    socketWithClient.getOutputStream().write(b, 0, bytesRead);
                }
                System.out.println("Socket with transfer is closed");
                System.out.println("Socket with client is closed");
                socketWithClient.close();
                socketWithTransfer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
