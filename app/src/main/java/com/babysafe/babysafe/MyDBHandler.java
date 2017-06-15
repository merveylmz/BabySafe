package com.babysafe.babysafe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import model.Tokens;
import model.Users;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "users.db";

    /*public static final String TABLE_CITY = "CREATE TABLE TABLE_CITY (Id INTEGER, CityName VARCHAR(55),FindName VARCHAR(55), Country VARCHAR(55), Zoom INTEGER)";
    public static final String TABLE_CLOUDS = "CREATE TABLE TABLE_CLOUDS (Al INTEGER )";*/

    String CREATE_TABLE_USER = "CREATE TABLE " +
            Users.TABLE_USERS + "("
            + Users.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Users.COLUMN_NAME + " TEXT,"
            + Users.COLUMN_SURNAME + " TEXT,"
            + Users.COLUMN_EMAIL + " TEXT,"
            + Users.COLUMN_PASSWORD + " TEXT,"
            + Users.COLUMN_TOKEN + " TEXT" + ")";

    String  CREATE_TABLE_TOKEN = "CREATE TABLE " +
            Tokens.TABLE_TOKENS + "("
            + Tokens.COLUMN_ID + " INTEGER,"
            + Tokens.COLUMN_TOKEN + " TEXT" + ")";


    public MyDBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_TOKEN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE " + Users.TABLE_USERS);
        db.execSQL("DROP TABLE " + Tokens.TABLE_TOKENS);
        onCreate(db);
    }

    /*--------------------------------------- VERİ EKLEME -----------------------------------------*/

    public Users addUser(Users users) {

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues valuesUsers = new ContentValues();
            valuesUsers.put(Users.COLUMN_NAME, users.getUserName());
            valuesUsers.put(Users.COLUMN_SURNAME, users.getUserSurname());
            valuesUsers.put(Users.COLUMN_EMAIL, users.getUserEmail());
            valuesUsers.put(Users.COLUMN_PASSWORD, users.getUserPassword());
            valuesUsers.put(Users.COLUMN_TOKEN, users.getUserToken());
            db.insert(Users.TABLE_USERS, null, valuesUsers);
            db.close();
            return  users;

        }catch (Exception e){
            e.printStackTrace();
        }
        return  users;
    }
    public void addToken(Integer id, String token){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuesTokens = new ContentValues();

        valuesTokens.put(Tokens.COLUMN_ID, id);
        valuesTokens.put(Tokens.COLUMN_TOKEN, token);

        db.insert(Tokens.TABLE_TOKENS, null, valuesTokens);

        db.close();
    }

    /*----------------------------------------- SORGU ---------------------------------------------*/

    public Users findUser(String email, String password) {

        Users users = new Users();
        try {
            String queryUsers = "select id, name, surname, email, password, token from users where email='" + email + "' and '" + password + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(queryUsers, null);

            if (cursor.moveToNext()) {

                users.setId(cursor.getInt(0));
                users.setUserName(cursor.getString(1));
                users.setUserSurname(cursor.getString(2));
                users.setUserEmail(cursor.getString(3));
                users.setUserPassword(cursor.getString(4));
                users.setUserToken(cursor.getString(5));

                cursor.close();
            }else {
                return null;
            }
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return users;
    }
    public ArrayList<String> findToken(Integer id) {

        ArrayList<String> tokenList = new ArrayList<String>();
        try {
            String queryTokens = "select token from tokens where id='" + id + "'";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(queryTokens, null);

            while (cursor.moveToNext()){
                tokenList.add(Integer.toString(cursor.getInt(0)));

            }
            cursor.close();

            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return tokenList;
    }

    public String getTokenFind() {
        String token = "";
        try {

            String queryTokens = "SELECT token FROM tokens";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(queryTokens, null);

            while (cursor.moveToNext()){
                token = cursor.getString(0);
            }
            cursor.close();

            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    /*----------------------------------------- SİLME ---------------------------------------------*/

    public void deleteToken(String token){

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String queryTokens = "delete  from tokens where token='" + token + "'";
            db.delete(Tokens.TABLE_TOKENS, Tokens.COLUMN_TOKEN + " = ?",
                    new String[] { token });
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void deleteToken() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String queryTokens = "delete  from tokens'";
            db.delete(Tokens.TABLE_TOKENS, null, null);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}