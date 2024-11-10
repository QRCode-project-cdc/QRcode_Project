package com.example.qrcode;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_RGM = "rgm";
    private static final String COLUMN_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_RGM + " TEXT, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Método para adicionar um usuário
    public boolean addUser (String name, String email, String rgm, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_RGM, rgm);
        values.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1; // Retorna true se a inserção foi bem-sucedida
    }

    // Método para verificar se o usuário existe
    public boolean checkUser (String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists; // Retorna true se o usuário foi encontrado
    }

    // Método para buscar o nome do usuário pelo e-mail
    public String getUserName(String email) {
        if (email == null) {
            return null; // Retorna null se o e-mail for nulo
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_NAME + " FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=?", new String[]{email});
        String name = null;
        if (cursor.moveToFirst()) {
            name = cursor.getString(0); // Obtém o nome da primeira coluna
        }
        cursor.close();
        db.close();
        return name; // Retorna o nome do usuário ou null se não encontrado
    }

    // Método para verificar se o e-mail já existe
    public boolean emailExists(String email) {
        if (email == null) {
            return false; // Retorna false se o e-mail for nulo
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists; // Retorna true se o e-mail já existe
    }

    // Método para verificar se o usuário existe pelo e-mail
    public boolean userExists(String email) {
        if (email == null) {
            return false; // Retorna false se o e-mail for nulo
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=?", new String[]{email });
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists; // Retorna true se o usuário foi encontrado
    }

    // Método para atualizar os dados do usuário
    public boolean updateUser (String currentEmail, String newName, String newEmail, String newPassword) {
        if (currentEmail == null || newName == null || newEmail == null || newPassword == null) {
            return false; // Retorna false se algum campo for nulo
        }

        SQLiteDatabase db = this.getWritableDatabase();

        // Verifica se o novo e-mail já existe (exceto para o e-mail atual)
        if (!currentEmail.equals(newEmail) && emailExists(newEmail)) {
            return false; // Retorna false se o novo e-mail já existe
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, newName);
        values.put(COLUMN_EMAIL, newEmail);
        values.put(COLUMN_PASSWORD, newPassword);

        // Atualiza o usuário com base no e-mail atual
        int rowsAffected = db.update(TABLE_USERS, values, COLUMN_EMAIL + "=?", new String[]{currentEmail});
        db.close();
        return rowsAffected > 0; // Retorna true se a atualização foi bem-sucedida
    }
}