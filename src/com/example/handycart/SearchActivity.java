package com.example.handycart;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SearchActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TextView textview = new TextView(this);
        textview.setText("Recherche de produit tab");
        setContentView(textview);
    }
}