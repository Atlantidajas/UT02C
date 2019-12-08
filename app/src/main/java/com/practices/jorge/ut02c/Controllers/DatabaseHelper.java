package com.practices.jorge.ut02c.Controllers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.practices.jorge.ut02c.Util.Config;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper databaseHelper;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = Config.DATABASE_NAME;

    // Constructor
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public static synchronized DatabaseHelper getInstance(Context context){
        if( databaseHelper == null ){
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }

    @Override
    public void onCreate( SQLiteDatabase db ) {

        // Crear tablas SQL
        String CREATE_STUDENT_TABLE = "CREATE TABLE " + Config.TABLE_USERS + "("
                + Config.COLUMN_USERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_USERS_NAME + " TEXT NOT NULL, "
                + Config.COLUMN_USERS_PHONE + " TEXT, " // Puede ser nulo
                + Config.COLUMN_USERS_EMAIL + " TEXT " // Puede ser nulo
                + ")";

        Logger.d("Table create SQL: " + CREATE_STUDENT_TABLE);

        db.execSQL(CREATE_STUDENT_TABLE);

        Logger.d("DB created!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si existe la tabla
        db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_USERS);


        onCreate(db);
    }

}
