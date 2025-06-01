package com.volodymyr_x.easyenglishlearn.data.db

import androidx.room.Database
import com.volodymyr_x.easyenglishlearn.model.Word
import androidx.room.RoomDatabase

@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
}
