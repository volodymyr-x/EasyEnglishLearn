package com.example.vladimir.easyenglishlearn.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

public class ToastUtil {

    private Context context;

    public ToastUtil(Context context) {
        this.context = context;
    }

    public void showMessage(@StringRes int id, int errorsCount) {
        String message = context.getString(id);
        if (errorsCount >= 0) {
            message += " " + errorsCount;
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

    }

    public void showMessage(@StringRes int id) {
        showMessage(id, -1);

    }
}
