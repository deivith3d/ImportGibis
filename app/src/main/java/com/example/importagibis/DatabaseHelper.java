package com.example.importagibis;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gibiteca.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_GIBIS = "gibis";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITULO = "titulo";
    public static final String COLUMN_NUMERO = "numero";
    public static final String COLUMN_SERIE = "serie";
    public static final String COLUMN_EDITORA = "editora";
    public static final String COLUMN_IMAGEM = "imagem";
    public static final String COLUMN_ANO = "ano";
    public static final String COLUMN_ADQUIRIDO = "adquirido";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_GIBIS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITULO + " TEXT, " +
                    COLUMN_NUMERO + " INTEGER, " +
                    COLUMN_SERIE + " TEXT, " +
                    COLUMN_EDITORA + " TEXT, " +
                    COLUMN_IMAGEM + " TEXT, " +
                    COLUMN_ANO + " TEXT, " +
                    COLUMN_ADQUIRIDO + " INTEGER" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GIBIS);
        onCreate(db);
    }
}
