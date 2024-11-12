package com.example.qrcode;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 2; // Aumente a versão para forçar a recriação do banco de dados
    private static DatabaseHelper instance;

    // Tabelas e colunas para Alunos
    private static final String TABLE_STUDENTS = "students";
    private static final String COLUMN_STUDENT_ID = "id";
    private static final String COLUMN_STUDENT_NAME = "name";
    private static final String COLUMN_STUDENT_EMAIL = "email";
    private static final String COLUMN_STUDENT_RGM = "rgm";
    private static final String COLUMN_STUDENT_PASSWORD = "password";

    // Tabelas e colunas para Professores
    private static final String TABLE_TEACHERS = "teachers";
    private static final String COLUMN_TEACHER_ID = "id";
    private static final String COLUMN_TEACHER_NAME = "name";
    private static final String COLUMN_TEACHER_EMAIL = "email";
    private static final String COLUMN_TEACHER_PASSWORD = "password";

    // Tabela e colunas para Eventos
    private static final String TABLE_EVENTS = "events";
    private static final String COLUMN_EVENT_ID = "event_id";
    private static final String COLUMN_EVENT_TITLE = "title";
    private static final String COLUMN_EVENT_DESCRIPTION = "description";
    private static final String COLUMN_EVENT_DATE = "date";
    private static final String COLUMN_EVENT_TEACHER_ID = "teacher_id";

    // Construtor privado para evitar instâncias externas
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Método para obter a instância singleton
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criação da tabela de alunos
        String createStudentsTable = "CREATE TABLE " + TABLE_STUDENTS + " (" +
                COLUMN_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STUDENT_NAME + " TEXT, " +
                COLUMN_STUDENT_EMAIL + " TEXT UNIQUE, " +
                COLUMN_STUDENT_RGM + " TEXT UNIQUE, " +
                COLUMN_STUDENT_PASSWORD + " TEXT)";
        db.execSQL(createStudentsTable);

        // Criação da tabela de professores
        String createTeachersTable = "CREATE TABLE " + TABLE_TEACHERS + " (" +
                COLUMN_TEACHER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TEACHER_NAME + " TEXT, " +
                COLUMN_TEACHER_EMAIL + " TEXT UNIQUE, " +
                COLUMN_TEACHER_PASSWORD + " TEXT)";
        db.execSQL(createTeachersTable);

        // Criação da tabela de eventos (já estava aqui, mas para garantir)
        String createEventsTable = "CREATE TABLE " + TABLE_EVENTS + " (" +
                COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EVENT_TITLE + " TEXT, " +
                COLUMN_EVENT_DESCRIPTION + " TEXT, " +
                COLUMN_EVENT_DATE + " TEXT, " +
                COLUMN_EVENT_TEACHER_ID + " INTEGER)";
        db.execSQL(createEventsTable);
    }


    // Método para o professor inserir evento
    public void addEvent(String title, String date, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("date", date);
        values.put("description", description);
        values.put("isAvailableForStudents", true);  // Evento visível para alunos

        db.insert("events", null, values);
        db.close();
    }

    // Método para obter todos os eventos
    public Cursor getAllEvents() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_EVENTS, null);
    }

    public boolean emailExists(String email) {
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursorStudents = db.rawQuery("SELECT * FROM " + TABLE_STUDENTS + " WHERE " + COLUMN_STUDENT_EMAIL + " = ?", new String[]{email});
             Cursor cursorTeachers = db.rawQuery("SELECT * FROM " + TABLE_TEACHERS + " WHERE " + COLUMN_TEACHER_EMAIL + " = ?", new String[]{email})) {

            return cursorStudents.getCount() > 0 || cursorTeachers.getCount() > 0;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Verifica se a versão foi atualizada e faz a migração necessária
        if (oldVersion < 2) { // Verifica se a versão antiga é menor que 2
            // Adiciona a tabela de eventos se não existir
            String createEventsTable = "CREATE TABLE IF NOT EXISTS " + TABLE_EVENTS + " (" +
                    COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EVENT_TITLE + " TEXT, " +
                    COLUMN_EVENT_DESCRIPTION + " TEXT, " +
                    COLUMN_EVENT_DATE + " TEXT, " +
                    COLUMN_EVENT_TEACHER_ID + " INTEGER)";
            db.execSQL(createEventsTable);
        }

        // Se necessário, adicione outras verificações de versão e migração aqui

        // Chamando o onCreate para garantir que o banco de dados esteja consistente
        onCreate(db);
    }


    // Método para adicionar um aluno
    public boolean addStudent(String name, String email, String rgm, String password) {
        if (rgmExists(rgm)) {
            return false; // Retorna false se o RGM já existe
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_NAME, name);
        values.put(COLUMN_STUDENT_EMAIL, email);
        values.put(COLUMN_STUDENT_RGM, rgm);
        values.put(COLUMN_STUDENT_PASSWORD, password);

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            long result = db.insert(TABLE_STUDENTS, null, values);
            return result != -1; // Retorna true se a inserção foi bem-sucedida
        }
    }

    // Método para adicionar um professor
    public boolean addTeacher(String name, String email, String password) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEACHER_NAME, name);
        values.put(COLUMN_TEACHER_EMAIL, email);
        values.put(COLUMN_TEACHER_PASSWORD, password);

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            long result = db.insert(TABLE_TEACHERS, null, values);
            return result != -1; // Retorna true se a inserção foi bem-sucedida
        }
    }

    // Método para verificar se um aluno existe pelo RGM e senha
    public boolean checkStudent(String rgm, String password) {
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STUDENTS + " WHERE " + COLUMN_STUDENT_RGM + "=? AND " + COLUMN_STUDENT_PASSWORD + "=?", new String[]{rgm, password})) {
            return cursor.getCount() > 0;
        }
    }

    // Método para verificar se um professor existe pelo e-mail e senha
    public boolean checkTeacher(String email, String password) {
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TEACHERS + " WHERE " + COLUMN_TEACHER_EMAIL + "=? AND " + COLUMN_TEACHER_PASSWORD + "=?", new String[]{email, password})) {
            return cursor.getCount() > 0;
        }
    }

    // Método para verificar se o RGM já existe
    public boolean rgmExists(String rgm) {
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STUDENTS + " WHERE " + COLUMN_STUDENT_RGM + "=?", new String[]{rgm})) {
            return cursor.getCount() > 0;
        }
    }

    // Método para atualizar as informações do aluno
    public boolean updateStudent(String currentRGM, String name, String rgm, String password) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_NAME, name);
        values.put(COLUMN_STUDENT_RGM, rgm);
        values.put(COLUMN_STUDENT_PASSWORD, password);

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            int rowsAffected = db.update(TABLE_STUDENTS, values, COLUMN_STUDENT_RGM + "=?", new String[]{currentRGM});
            return rowsAffected > 0; // Retorna true se pelo menos uma linha foi atualizada
        }
    }

    // Método para verificar se um aluno existe pelo e-mail
    public boolean studentEmailExists(String email) {
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STUDENTS + " WHERE " + COLUMN_STUDENT_EMAIL + "=?", new String[]{email})) {
            return cursor.getCount() > 0;
        }
    }

    // Método para obter o nome do aluno pelo e-mail
    public String getStudentName(String email) {
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT " + COLUMN_STUDENT_NAME + " FROM " + TABLE_STUDENTS + " WHERE " + COLUMN_STUDENT_EMAIL + "=?", new String[]{email})) {
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_NAME));
            }
        }
        return null;
    }

    // Método para obter o RGM do aluno pelo e-mail
    public String getStudentRGM(String email) {
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT " + COLUMN_STUDENT_RGM + " FROM " + TABLE_STUDENTS + " WHERE " + COLUMN_STUDENT_EMAIL + "=?", new String[]{email})) {
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_RGM));
            }
        }
        return null;
    }

    // Método para obter o e-mail do aluno pelo RGM
    public String getStudentEmailByRGM(String rgm) {
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT " + COLUMN_STUDENT_EMAIL + " FROM " + TABLE_STUDENTS + " WHERE " + COLUMN_STUDENT_RGM + "=?", new String[]{rgm})) {
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_EMAIL));
            }
        }
        return null; // Retorna null se não encontrar
    }

    // Método para obter o nome do aluno pelo RGM
    public String getStudentNameByRGM(String rgm) {
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT " + COLUMN_STUDENT_NAME + " FROM " + TABLE_STUDENTS + " WHERE " + COLUMN_STUDENT_RGM + "=?", new String[]{rgm})) {
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_NAME));
            }
        }
        return null; // Retorna null se não encontrar
    }
}