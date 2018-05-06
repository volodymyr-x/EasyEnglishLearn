package com.example.vladimir.easyenglishlearn.fragments;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;

import com.example.vladimir.easyenglishlearn.CategoryActivity;
import com.example.vladimir.easyenglishlearn.R;
import com.example.vladimir.easyenglishlearn.db.DatabaseHelper;

public class RemoveCategoryFragment extends DialogFragment implements OnClickListener {

    private final String LOG_TAG = RemoveCategoryFragment.class.getSimpleName();
    public static final String DIALOG_REMOVE_CATEGORY = "DIALOG_REMOVE_CATEGORY";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        adb.setView(layoutInflater.inflate(R.layout.remove_category, null))
                .setPositiveButton("Да", this)
                .setNegativeButton("Нет", this);
        return adb.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        Button posiButton = ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE);
        posiButton.setBackgroundResource(R.drawable.states_button);
        Button negButton = ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_NEGATIVE);
        negButton.setBackgroundResource(R.drawable.states_button);

        TextView textView = getDialog().findViewById(R.id.rcf_tv_remove_category);

        float fontSize = ((CategoryActivity) getActivity()).getFontSize();
        posiButton.setTextSize(fontSize);
        negButton.setTextSize(fontSize);
        textView.setTextSize(fontSize);
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                CategoryActivity activity = (CategoryActivity) this.getActivity();
                String nameOfCategory = activity.getCursorAdapter().getCursor()
                        .getString(activity.getCursorAdapter().getCursor().getColumnIndex(DatabaseHelper.COLUMN_CATEGORY));

                String selection = "name_of_category == ?";
                String[] selectionArgs = new String[] { nameOfCategory };
                Cursor cursor = activity.getDbHelper().sqLiteDB.query(DatabaseHelper.DATABASE_TABLE_WORDS, null, selection, selectionArgs, null, null, null);

                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            activity.getDbHelper().delRecWords(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORDS_ID)));

                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                } else
                    Log.d(LOG_TAG, "Cursor is null");

                activity.getDbHelper().delRec(activity.getContextMenuAdapter().id);
                activity.getSupportLoaderManager().getLoader(0).forceLoad();
                break;
            case Dialog.BUTTON_NEGATIVE:
                break;
            default:
                break;
        }
    }

}
