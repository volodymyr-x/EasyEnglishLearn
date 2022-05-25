package com.vladimir_x.easyenglishlearn

import android.app.Application
import com.vladimir_x.easyenglishlearn.data.db.AppDatabase
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.concurrent.Executors

class App : Application() {
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room
            .databaseBuilder(this, AppDatabase::class.java, Constants.DATABASE_NAME)
            .addCallback(rdc)
            .build()
    }

    var rdc: RoomDatabase.Callback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Executors.newSingleThreadScheduledExecutor().execute {
                db.execSQL(
                    "INSERT INTO word (lexeme, translation, category) " +
                            "VALUES('milk', 'молоко', 'Еда и Напитки'), " +
                            "('apple', 'яблоко', 'Еда и Напитки')," +
                            "('bread', 'хлеб', 'Еда и Напитки')," +
                            "('butter', 'масло', 'Еда и Напитки')," +
                            "('football', 'футбол', 'Спорт')," +
                            "('tennis', 'тенис', 'Спорт')," +
                            "('basketball', 'баскетбол', 'Спорт')," +
                            "('snooker', 'снукер', 'Спорт')," +
                            "('money', 'деньги', 'Поход в магазин')," +
                            "('seller', 'продавец', 'Поход в магазин')," +
                            "('queue', 'очередь', 'Поход в магазин');"
                )
            }
        }
    }

    companion object {
        lateinit var instance: App
    }
}