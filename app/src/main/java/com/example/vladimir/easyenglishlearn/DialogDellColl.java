package com.example.vladimir.easyenglishlearn;

/**
 * Created by BOBAH on 26.03.2015.
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;

public class DialogDellColl extends DialogFragment implements OnClickListener {

    Cursor cursor = null;
    final String LOG_TAG = "myLogs";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        adb.setView(layoutInflater.inflate(R.layout.dialog_del_coll, null))
                .setPositiveButton("Да", this)
                .setNegativeButton("Нет", this);
                //.setMessage("Удалить коллекцию?");
        return adb.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        Button posiButton = ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE);
        posiButton.setBackgroundResource(R.drawable.states_button);
        Button negButton = ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_NEGATIVE);
        negButton.setBackgroundResource(R.drawable.states_button);

        TextView textView = (TextView) getDialog().findViewById(R.id.tvDialDellColl);

        posiButton.setTextSize(((CategoryActivity) getActivity()).getfSize());
        negButton.setTextSize(((CategoryActivity) getActivity()).getfSize());
        textView.setTextSize(((CategoryActivity) getActivity()).getfSize());

        posiButton.setTextColor(((CategoryActivity) getActivity()).getColor());
        negButton.setTextColor(((CategoryActivity) getActivity()).getColor());
        textView.setTextColor(((CategoryActivity) getActivity()).getColor());
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                CategoryActivity activity = (CategoryActivity) this.getActivity();
                String nameOfCategory = activity.scAdapter.getCursor()
                        .getString(activity.scAdapter.getCursor().getColumnIndex(DatabaseHelper.COLUMN_CATEGORY));

                String selection = "name_of_category == ?";
                String[] selectionArgs = new String[] { nameOfCategory };
                cursor = activity.db.mDB.query(DatabaseHelper.DATABASE_TABLE_WORDS, null, selection, selectionArgs, null, null, null);

                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            activity.db.delRecWords(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORDS_ID)));

                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                } else
                    Log.d(LOG_TAG, "Cursor is null");

                activity.db.delRec(activity.acmi.id);
                activity.getSupportLoaderManager().getLoader(0).forceLoad();
                break;
            case Dialog.BUTTON_NEGATIVE:

                break;

            default:
                break;
        }
    }

}
