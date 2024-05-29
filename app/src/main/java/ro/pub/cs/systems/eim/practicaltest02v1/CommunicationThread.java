package ro.pub.cs.systems.eim.practicaltest02v1;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class CommunicationThread extends Thread{
    private final Socket socket;

    public CommunicationThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e("Comm", "[COMMUNICATION THREAD] Socket is null!");
            return;
        }

        try
        {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            String userInput = bufferedReader.readLine();
            if (userInput == null || userInput.isEmpty()) {
                Log.i("Comm", "[COMMUNICATION] Error receiving parameters from client. Exiting.");
                return;
            }

            Log.i("Comm", "[COMMUNICATION] Getting the information from the webservice.");
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet("https://www.google.com/complete/search?client=chrome&q=" + userInput);
            HttpResponse httpGetResponse = httpClient.execute(httpGet);
            HttpEntity httpGetEntity = httpGetResponse.getEntity();

            if (httpGetEntity == null) {
                Log.i("Comm", "[COMMUNICATION] Error getting the information from the webservice. Exiting.");
                return;
            }
            String pageSourceCode = EntityUtils.toString(httpGetEntity);
            Log.i("Comm", "[COMMUNICATION] pageSourceCode: " + pageSourceCode);

            String[] parts = pageSourceCode.split("[\\[\\]\"]");
            StringBuilder results = new StringBuilder();

            int resultsCounter = Utilities.NUM_RESULTS;
            for (String part : parts) {
                if (!part.trim().isEmpty() && !part.equals(",")) {
                    results.append(part.trim()).append("\n");
                    resultsCounter--;
                }

                if (resultsCounter == 0) {
                    break;
                }
            }

            PrintWriter printWriter = Utilities.getWriter(socket);
            printWriter.println(results);
            printWriter.flush();

        } catch (Exception e) {
            Log.e("Comm", "[COMMUNICATION] Exception occurred: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                Log.e("Comm", "[COMMUNICATION] Exception occurred: " + e.getMessage());
            }
        }
    }
}