package com.example.vladimir.easyenglishlearn.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper {

    private static final String DATABASE_NAME = "myDB2";
    private static final int DB_VERSION = 1;
    private static final String DATABASE_TABLE = "category";
    private static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORY = "name_of_category";
    private static final String DATABASE_CREATE_SCRIPT = "CREATE TABLE IF NOT EXISTS "
            + DATABASE_TABLE + " (" + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_CATEGORY
            + " text not null" + ");";
    public static final String DATABASE_TABLE_WORDS = "words";
    public static final String COLUMN_WORDS_ID = "_id";
    private static final String COLUMN_NAME_OF_CATEGORY = "name_of_category";
    public static final String COLUMN_LEXEME = "lexeme";
    public static final String COLUMN_TRANSLATION = "perevod";
    private static final String DATABASE_WORDS_CREATE_SCRIPT = "CREATE TABLE IF NOT EXISTS "
            + DATABASE_TABLE_WORDS + " (" + COLUMN_WORDS_ID + " integer primary key autoincrement, " + COLUMN_NAME_OF_CATEGORY
            + " text, " + COLUMN_LEXEME + " text, " + COLUMN_TRANSLATION + " text" + ");";

    private final Context context;
    private DBHelper dbHelper;
    public SQLiteDatabase sqLiteDB;

    public DatabaseHelper(Context context) {
        this.context = context;
    }

    // открыть подключение
    public void open() {
        dbHelper = new DBHelper(context, DATABASE_NAME, null, DB_VERSION);
        sqLiteDB = dbHelper.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        if (dbHelper !=null) dbHelper.close();
    }

    // получить все данные из таблицы DB_TABLE
    public Cursor getAllData() {
        return sqLiteDB.query(DATABASE_TABLE, null, null, null, null, null, null);
    }
    // получить нужные данные из таблицы DATABASE_TABLE_WORDS
    public Cursor getWords(String nameOfCategory) {
        String selection = "name_of_category == ?";
        String[] selectionArgs = new String[] { nameOfCategory };
        return sqLiteDB.query(DATABASE_TABLE_WORDS, null, selection, selectionArgs, null, null, null);
    }

    // добавить запись в DB_TABLE
    public void addRec(String category) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CATEGORY, category);
        sqLiteDB.insert(DATABASE_TABLE, null, cv);
    }
    // добавить запись в DATABASE_TABLE_WORDS
    public void addRecWords(String category, String lexeme, String translation) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME_OF_CATEGORY, category);
        cv.put(COLUMN_LEXEME, lexeme);
        cv.put(COLUMN_TRANSLATION, translation);
        sqLiteDB.insert(DATABASE_TABLE_WORDS, null, cv);
    }

    // удалить запись из DB_TABLE
    public void delRec(long id) {
        sqLiteDB.delete(DATABASE_TABLE, COLUMN_ID + " = " + id, null);
    }
    // удалить запись из DB_TABLE_WORDS
    public void delRecWords(long id) {
        sqLiteDB.delete(DATABASE_TABLE_WORDS, COLUMN_WORDS_ID + " = " + id, null);
    }

    //редактировать запись из DB_TABLE
    public void updRec (String newNameOfCategory, long id) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CATEGORY, newNameOfCategory);
        sqLiteDB.update(DATABASE_TABLE, cv, COLUMN_WORDS_ID + " = " + id, null);
    }

    // редактировать запись из DB_TABLE_WORDS
    public void updRecWords (String lexeme, String translation, long id){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LEXEME, lexeme);
        cv.put(COLUMN_TRANSLATION, translation);
        sqLiteDB.update(DATABASE_TABLE_WORDS, cv, COLUMN_WORDS_ID + " = " + id, null);
    }

    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {

        DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // создаем и заполняем БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_SCRIPT);

            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_CATEGORY, "Еда и Напитки");
            db.insert(DATABASE_TABLE, null, contentValues);
            contentValues.put(COLUMN_CATEGORY, "Спорт");
            db.insert(DATABASE_TABLE, null, contentValues);
            contentValues.put(COLUMN_CATEGORY, "Туризм");
            db.insert(DATABASE_TABLE, null, contentValues);
            contentValues.put(COLUMN_CATEGORY, "Отдых");
            db.insert(DATABASE_TABLE, null, contentValues);
            contentValues.put(COLUMN_CATEGORY, "Поход в магазин");
            db.insert(DATABASE_TABLE, null, contentValues);

            db.execSQL(DATABASE_WORDS_CREATE_SCRIPT);

            ContentValues contentValues1 = new ContentValues();
            contentValues1.put(COLUMN_NAME_OF_CATEGORY, "Еда и Напитки");
            contentValues1.put(COLUMN_LEXEME, "milk");
            contentValues1.put(COLUMN_TRANSLATION, "молоко");
            db.insert(DATABASE_TABLE_WORDS,null, contentValues1);
            contentValues1.put(COLUMN_NAME_OF_CATEGORY, "Еда и Напитки");
            contentValues1.put(COLUMN_LEXEME, "apple");
            contentValues1.put(COLUMN_TRANSLATION, "яблоко");
            db.insert(DATABASE_TABLE_WORDS,null, contentValues1);
            contentValues1.put(COLUMN_NAME_OF_CATEGORY, "Еда и Напитки");
            contentValues1.put(COLUMN_LEXEME, "bread");
            contentValues1.put(COLUMN_TRANSLATION, "хлеб");
            db.insert(DATABASE_TABLE_WORDS,null, contentValues1);
            contentValues1.put(COLUMN_NAME_OF_CATEGORY, "Еда и Напитки");
            contentValues1.put(COLUMN_LEXEME, "butter");
            contentValues1.put(COLUMN_TRANSLATION, "масло");
            db.insert(DATABASE_TABLE_WORDS,null, contentValues1);

            contentValues1.put(COLUMN_NAME_OF_CATEGORY, "Поход в магазин");
            contentValues1.put(COLUMN_LEXEME, "money");
            contentValues1.put(COLUMN_TRANSLATION, "деньги");
            db.insert(DATABASE_TABLE_WORDS,null, contentValues1);
            contentValues1.put(COLUMN_NAME_OF_CATEGORY, "Поход в магазин");
            contentValues1.put(COLUMN_LEXEME, "queue");
            contentValues1.put(COLUMN_TRANSLATION, "очередь");
            db.insert(DATABASE_TABLE_WORDS,null, contentValues1);
            contentValues1.put(COLUMN_NAME_OF_CATEGORY, "Поход в магазин");
            contentValues1.put(COLUMN_LEXEME, "shop assistant");
            contentValues1.put(COLUMN_TRANSLATION, "продавец");
            db.insert(DATABASE_TABLE_WORDS,null, contentValues1);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}

