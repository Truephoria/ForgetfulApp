package com.example.trevor.forgetfultransfer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent displayMessage = getIntent();
        String message = displayMessage.getStringExtra(MainActivity.EXTRA_MESSAGE);

        TextView displayedText = findViewById(R.id.textView);
        displayedText.setText(message);

    }
}
