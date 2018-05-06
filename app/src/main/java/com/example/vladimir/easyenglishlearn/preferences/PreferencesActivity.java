package com.example.vladimir.easyenglishlearn.preferences;

/**
 * Created by BOBAH on 26.03.2015.
 */
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.vladimir.easyenglishlearn.R;

public class PreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
