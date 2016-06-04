package main;

import java.io.*;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ocz on 6/4/16.
 */
public class Bob {

    public Bob() throws IOException {
        ServerSocket serverSocket = new ServerSocket(9000, 50, Inet4Address.getByName("172.20.0.1"));
        Socket socketWithTransfer;
        while (true) {
            socketWithTransfer = serverSocket.accept();
            new Thread(new HandleARequest(socketWithTransfer)).start();
        }
    }

    private class HandleARequest implements Runnable {
        Socket socketWithTransfer;
        Socket socketWithServer;
        public HandleARequest(Socket socketWithTransfer) throws IOException {
            this.socketWithTransfer = socketWithTransfer;
            this.socketWithServer = new Socket("127.0.0.1", 8000);
        }

        @Override
        public void run() {
            byte[] b = new byte[10240];
            try {
                int bytesRead = socketWithTransfer.getInputStream().read(b);
                socketWithServer.getOutputStream().write(b, 0, bytesRead);
                socketWithServer.getOutputStream().flush();
                while ((bytesRead = socketWithServer.getInputStream().read(b)) != -1) {
                    socketWithTransfer.getOutputStream().write(b, 0, bytesRead);
                }
                socketWithTransfer.getOutputStream().close();
                System.out.println("Socket with server is closed");
                System.out.println("Socket with transfer is closed");
                socketWithTransfer.close();
                socketWithServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
