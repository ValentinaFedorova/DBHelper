package com.example.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    final static String DB_NAME = "films.db";
    final static String TABLE_NAME = "films";
    final static String CREATE = "CREATE TABLE "+TABLE_NAME+ "( `_id` INTEGER PRIMARY KEY AUTOINCREMENT, `title` TEXT NOT NULL, `genre` TEXT NOT NULL, `rating` INTEGER NOT NULL, `year` INTEGER NOT NULL )";
    // при изменении структуры БД меняется и версия
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // выполняется, если базы данных нет
        db.execSQL(CREATE);
        db.execSQL("INSERT INTO films VALUES (1, 'Onward', 'cartoon', 9, 2020 )");
        db.execSQL("INSERT INTO films VALUES (2, 'Knives out', 'detective', 8, 2019 )");
        db.execSQL("INSERT INTO films VALUES (3, 'The Godfather', 'criminal', 10, 1972 )");
        db.execSQL("INSERT INTO films VALUES (4, 'La La Land', 'drama', 5, 2016 )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // выполняется, если версия базы данных отличается
        db.execSQL("DROP DATABASE "+DB_NAME);
        this.onCreate(db);
    }

}
