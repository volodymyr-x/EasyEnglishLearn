package com.example.vladimir.easyenglishlearn;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
    Button btnStartTheme, btnSharPref, btnAbout;
    static int color;
    static float fSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btnStartTheme = (Button) findViewById(R.id.btnStartTema);
        btnStartTheme.setOnClickListener(this);
        btnSharPref = (Button) findViewById(R.id.btnSharPref);
        btnSharPref.setOnClickListener(this);
        btnAbout = (Button) findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //fSize = Float.parseFloat(prefs.getString(getString(R.string.pr_size), "20"));
        fSize = Float.parseFloat(prefs.getString("list", "20"));
        btnStartTheme.setTextSize(fSize);
        btnSharPref.setTextSize(fSize);
        btnAbout.setTextSize(fSize);

        color = Color.BLACK;
        if (prefs.getBoolean(getString(R.string.pr_color_red), false)) {
            color += Color.RED;
        }
        if (prefs.getBoolean(getString(R.string.pr_color_green), false)) {
            color += Color.GREEN;
        }
        if (prefs.getBoolean(getString(R.string.pr_color_blue), false)) {
            color += Color.BLUE;
        }
        btnStartTheme.setTextColor(color);
        btnSharPref.setTextColor(color);
        btnAbout.setTextColor(color);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btnStartTema:
                intent = new Intent(this, CategoryActivity.class);
                startActivity(intent);

                break;
            case R.id.btnSharPref:
                intent.setClass(this, PreferencesActivity.class);
                startActivity(intent);

                break;
            case R.id.btnAbout:

                break;

            default:
                break;
        }

    }

}

