package ro.pub.cs.systems.eim.practicaltest02v1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PracticalTest02v1MainActivity extends AppCompatActivity {
    private EditText portEditText;
    private EditText userInputEditText;
    private Button requestButton;
    private TextView resultTextView;
    private ServerThread serverThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02v1_main);

        portEditText = findViewById(R.id.port);
        userInputEditText = findViewById(R.id.userInput);
        requestButton = findViewById(R.id.request);
        resultTextView = findViewById(R.id.result);

        requestButton.setOnClickListener(view -> {
            if (serverThread == null)
            {
                serverThread = new ServerThread(Integer.parseInt(portEditText.getText().toString()));
                serverThread.start();
            }

            ClientThread client = new ClientThread(
                    "localhost",
                    Integer.parseInt(portEditText.getText().toString()),
                    userInputEditText.getText().toString(),
                    resultTextView);
            client.start();
        });
    }

    @Override
    protected void onDestroy() {
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}