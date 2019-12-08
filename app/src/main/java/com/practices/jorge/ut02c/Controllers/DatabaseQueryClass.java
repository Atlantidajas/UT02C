package com.practices.jorge.ut02c.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.practices.jorge.ut02c.Models.CreateUser.User;
import com.practices.jorge.ut02c.Util.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseQueryClass {

    private Context context;

    public DatabaseQueryClass(Context context){
        this.context = context;
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public long insertStudent( User user){

        long id = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance( context );
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_USERS_NAME, user.getName());
        contentValues.put(Config.COLUMN_USERS_PHONE, user.getPhoneNumber());
        contentValues.put(Config.COLUMN_USERS_EMAIL, user.getEmail());

        try {
            id = sqLiteDatabase.insertOrThrow( Config.TABLE_USERS, null, contentValues);
        } catch (SQLiteException e){
            Logger.d("Error: " + e.getMessage());
            Toast.makeText( context, "Fallo en la operación: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return id;
    }

    public List<User> getAllStudent(){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(Config.TABLE_USERS, null, null, null, null, null, null, null);


            if(cursor!=null)
                if(cursor.moveToFirst()){
                    List<User> userList = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_USERS_ID));
                        String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_USERS_NAME));
                        String email = cursor.getString(cursor.getColumnIndex(Config.COLUMN_USERS_EMAIL));
                        String phone = cursor.getString(cursor.getColumnIndex(Config.COLUMN_USERS_PHONE));

                        userList.add(new User(id, name, email, phone));
                    }   while (cursor.moveToNext());

                    return userList;
                }
        } catch (Exception e){
            Logger.d("Error: " + e.getMessage());
            Toast.makeText(context, "Error en la operación", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }

    public User getStudentByRegNum(long idUser){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        User user = null;
        try {

            cursor = sqLiteDatabase.query(Config.TABLE_USERS, null,
                    Config.COLUMN_USERS_ID + " = ? ", new String[]{String.valueOf( idUser )},
                    null, null, null);


            if(cursor.moveToFirst()){
                int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_USERS_ID));
                String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_USERS_NAME));
                String phone = cursor.getString(cursor.getColumnIndex(Config.COLUMN_USERS_PHONE));
                String email = cursor.getString(cursor.getColumnIndex(Config.COLUMN_USERS_EMAIL));

                user = new User(id, name, phone, email);
            }
        } catch (Exception e){
            Logger.d("Error: " + e.getMessage());
            Toast.makeText(context, "Error en la operación", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return user;
    }

    public long updateStudentInfo(User user){

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_USERS_NAME, user.getName());
        contentValues.put(Config.COLUMN_USERS_PHONE, user.getPhoneNumber());
        contentValues.put(Config.COLUMN_USERS_EMAIL, user.getEmail());

        try {
            rowCount = sqLiteDatabase.update(Config.TABLE_USERS, contentValues,
                    Config.COLUMN_USERS_ID + " = ? ",
                    new String[] { String.valueOf(user.getId())});
        } catch (SQLiteException e){
            Logger.d("Error: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public long deleteStudentByRegNum( long idUsers ) {
        long deletedRowCount = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            deletedRowCount = sqLiteDatabase.delete(Config.TABLE_USERS,
                                    Config.COLUMN_USERS_ID + " = ? ",
                                    new String[]{ String.valueOf( idUsers )});
        } catch (SQLiteException e){
            Logger.d("Error: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deletedRowCount;
    }

    public boolean deleteAllStudents(){
        boolean deleteStatus = false;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            sqLiteDatabase.delete(Config.TABLE_USERS, null, null);

            long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Config.TABLE_USERS);

            if(count==0)
                deleteStatus = true;

        } catch (SQLiteException e){
            Logger.d("Error: " + e.getMessage());
            Toast.makeText( context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deleteStatus;
    }

}