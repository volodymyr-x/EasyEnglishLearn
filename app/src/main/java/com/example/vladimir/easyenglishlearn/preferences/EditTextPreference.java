package com.example.vladimir.easyenglishlearn.preferences;

import android.content.Context;
import android.graphics.Color;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by BOBAH on 29.04.2015.
 */
public class EditTextPreference extends DialogPreference {

    public EditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        EditText mEditText = new EditText(context, attrs);
        //mEditText.setId(com.android.internal.R.id.edit);
        mEditText.setEnabled(true);
        mEditText.setText("qwerty");
        mEditText.setTextColor(Color.BLUE);
    }
}
