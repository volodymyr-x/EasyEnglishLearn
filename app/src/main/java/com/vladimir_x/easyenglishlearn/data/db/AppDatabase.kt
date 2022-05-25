package com.vladimir_x.easyenglishlearn.data.db

import androidx.room.Database
import com.vladimir_x.easyenglishlearn.model.Word
import androidx.room.RoomDatabase

@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
}