package com.vladimir_x.easyenglishlearn.di.modules

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.data.db.AppDatabase
import com.vladimir_x.easyenglishlearn.data.db.WordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executors
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
            .addCallback(rdc)

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): WordDao = appDatabase.wordDao()

    private var rdc: RoomDatabase.Callback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Executors.newSingleThreadScheduledExecutor().execute {
                db.execSQL(
                    "INSERT INTO word (lexeme, translation, category) " +
                            "VALUES('milk', 'молоко', 'Їжа та напої'), " +
                            "('apple', 'яблуко', 'Їжа та напої')," +
                            "('bread', 'хліб', 'Їжа та напої')," +
                            "('butter', 'масло', 'Їжа та напої')," +
                            "('football', 'футбол', 'Спорт')," +
                            "('tennis', 'теніс', 'Спорт')," +
                            "('basketball', 'баскетбол', 'Спорт')," +
                            "('snooker', 'снукер', 'Спорт')," +
                            "('money', 'гроші', 'В магазині')," +
                            "('seller', 'продавець', 'В магазині')," +
                            "('queue', 'черга', 'В магазині');"
                )
            }
        }
    }
}
