package com.example.handycart;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AssistanceActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("Assistance tab");
        setContentView(textview);
    }
}