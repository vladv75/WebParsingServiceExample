package ru.allfound.webparsingserviceexample;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final String LOG = "MainActivity";

    public final static String PARAM_URI = "uri";
    public final static String PARAM_PINTENT = "pendingIntent";
    public final static String PARAM_RESULT = "result";

    EditText etUri;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUri = (EditText) findViewById(R.id.etURI);
        tvResult = (TextView) findViewById(R.id.tvResult);
    }

    public void onClickStart(View view) {

        Log.d(LOG, "START service");

        String uri = etUri.getText().toString();
        Intent intent_empty = new Intent();
        PendingIntent pendingIntent = createPendingResult(0, intent_empty, 0);
        Intent intent = new Intent(this, WebParsingService.class)
                .putExtra(PARAM_URI, uri)
                .putExtra(PARAM_PINTENT, pendingIntent);
        startService(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG, "onActivityResult");

        tvResult.setText(data.getStringExtra(PARAM_RESULT));
    }

    public void onClickStop(View view) {
        stopService(new Intent(this, WebParsingService.class));
        Log.d(LOG, "STOP service");
    }
}
