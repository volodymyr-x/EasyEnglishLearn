package com.example.vladimir.easyenglishlearn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.vladimir.easyenglishlearn.preferences.PreferencesActivity;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private Button btnStart, btnSPref, btnAbout;
    public static float fontSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btnStart = findViewById(R.id.ma_btn_start);
        btnStart.setOnClickListener(this);
        btnSPref = findViewById(R.id.ma_btn_shar_pref);
        btnSPref.setOnClickListener(this);
        btnAbout = findViewById(R.id.ma_btn_about);
        btnAbout.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ma_btn_start:
                intent = new Intent(this, CategoryActivity.class);
                startActivity(intent);

                break;
            case R.id.ma_btn_shar_pref:
                intent.setClass(this, PreferencesActivity.class);
                startActivity(intent);

                break;
            case R.id.ma_btn_about:

                break;
            default:
                break;
        }
    }
}

