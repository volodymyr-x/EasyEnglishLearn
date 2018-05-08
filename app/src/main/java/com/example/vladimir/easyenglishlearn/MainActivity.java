package com.example.vladimir.easyenglishlearn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.vladimir.easyenglishlearn.preferences.PreferencesActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnStart, btnSPref, btnAbout;
    public static float fontSize;

    private OnClickListener btnStartListener = v ->
            startActivity(new Intent(this, CategoryActivity.class));

    private OnClickListener btnSPrefListener = v ->
            startActivity(new Intent(this, PreferencesActivity.class));

    private OnClickListener btnAboutListener = v -> {};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btnStart = findViewById(R.id.ma_btn_start);
        btnStart.setOnClickListener(btnStartListener);
        btnSPref = findViewById(R.id.ma_btn_shar_pref);
        btnSPref.setOnClickListener(btnSPrefListener);
        btnAbout = findViewById(R.id.ma_btn_about);
        btnAbout.setOnClickListener(btnAboutListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        fontSize = Float.parseFloat(prefs.getString(getString(R.string.pr_size_list), "20"));
        btnStart.setTextSize(fontSize);
        btnSPref.setTextSize(fontSize);
        btnAbout.setTextSize(fontSize);
    }
}

