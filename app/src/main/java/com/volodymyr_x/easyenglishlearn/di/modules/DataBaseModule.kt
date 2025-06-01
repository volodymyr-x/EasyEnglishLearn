package com.volodymyr_x.easyenglishlearn.di.modules

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.volodymyr_x.easyenglishlearn.Constants
import com.volodymyr_x.easyenglishlearn.data.db.AppDatabase
import com.volodymyr_x.easyenglishlearn.data.db.WordDao
import com.volodymyr_x.easyenglishlearn.model.Word
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        createDatabaseBuilder(context).build()

    private fun createDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> =
        Room.databaseBuilder(context, AppDatabase::class.java, Constants.DATABASE_NAME)
            .addCallback(DatabaseCallback(context))

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): WordDao = appDatabase.wordDao()

    class DatabaseCallback(
        val context: Context
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val jsonString = context.assets.open("db_initial_data.json")
                .bufferedReader()
                .use { it.readText() }

            val items = Json.decodeFromString<List<Word>>(jsonString)

            db.beginTransaction()
            try {
                for (item in items) {
                    val values = ContentValues().apply {
                        put("lexeme", item.lexeme)
                        put("translation", item.translation)
                        put("category", item.category)
                    }
                    db.insert("Word", CONFLICT_IGNORE, values)
                }
                db.setTransactionSuccessful()
            } finally {
                db.endTransaction()
            }
        }

    }
}
