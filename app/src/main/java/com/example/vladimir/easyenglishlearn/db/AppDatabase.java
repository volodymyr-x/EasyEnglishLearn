package com.example.vladimir.easyenglishlearn.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.vladimir.easyenglishlearn.model.Word;

@Database(entities = {Word.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract WordDao wordDao();
}
