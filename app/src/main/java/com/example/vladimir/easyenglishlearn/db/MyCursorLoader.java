package com.example.vladimir.easyenglishlearn.db;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

public class MyCursorLoader extends CursorLoader {

    private DatabaseHelper db_helper;

    public MyCursorLoader (Context context, DatabaseHelper db_helper) {
        super(context);
        this.db_helper = db_helper;
    }

    @Override
    public Cursor loadInBackground() {
        return db_helper.getAllData();
    }
}
