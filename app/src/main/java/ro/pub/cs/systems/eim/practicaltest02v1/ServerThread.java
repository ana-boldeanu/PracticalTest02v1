package ro.pub.cs.systems.eim.practicaltest02v1;

import android.util.Log;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {
    private ServerSocket serverSocket = null;

    public ServerThread(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            Log.e("Server", "[SERVER] Exception occurred: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try
        {
            while (!Thread.currentThread().isInterrupted())
            {
                Log.i("Server", "[SERVER] Waiting for a client invocation.");
                Socket socket = serverSocket.accept();
                Log.i("Server", "[SERVER] A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort());
                CommunicationThread communicationThread = new CommunicationThread(socket);
                communicationThread.start();
            }
        } catch (IOException e) {
            Log.e("Server", "[SERVER] Exception occurred: " + e.getMessage());
        }
    }

    public void stopThread() {
        interrupt();
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                Log.e("Server", "[SERVER THREAD] An exception has occurred: " + e.getMessage());
            }
        }
    }
}