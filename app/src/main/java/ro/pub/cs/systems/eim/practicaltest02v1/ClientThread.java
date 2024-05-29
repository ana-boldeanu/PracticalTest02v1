package ro.pub.cs.systems.eim.practicaltest02v1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

public class ClientThread extends Thread{

    private final String address;
    private final int port;
    private final String userInput;
    private final TextView result;

    public ClientThread(String address, int port, String userInput, TextView result) {
        this.address = address;
        this.port = port;
        this.userInput = userInput;
        this.result = result;
    }

    @Override
    public void run() {
        try
        {
            Log.i("Client","[CLIENT] Creating socket for address " + address + ", port " + port);
            Socket socket = new Socket(address, port);

            Log.i("Client","[CLIENT] Sending user input: " + userInput);
            PrintWriter printWriter = Utilities.getWriter(socket);
            printWriter.println(userInput);
            printWriter.flush();

            BufferedReader bufferedReader = Utilities.getReader(socket);
            StringBuilder results = new StringBuilder();

            int resultsCounter = Utilities.NUM_RESULTS;
            while (resultsCounter > 0)
            {
                String result = bufferedReader.readLine();
                Log.i("Client","[CLIENT] Received result: " + result);

                results.append(result).append("\n");
                resultsCounter--;
            }

            result.post(() -> result.setText(results.toString()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}