package com.example.qrcode;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "users.db";
    public static final int DATABASE_VERSION = 5; // Aumente a versão para forçar a recriação do +de dados
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
    public static final String TABLE_EVENTS = "events"; // Tornado público
    public static final String COLUMN_EVENT_ID = "event_id";
    public static final String COLUMN_EVENT_TITLE = "title";
    public static final String COLUMN_EVENT_DESCRIPTION = "description";
    public static final String COLUMN_EVENT_TEACHER_ID = "teacher_id";

    public static final String TABLE_SCANNED_DATA = "scanned_data"; // Tornado público
    public static final String COLUMN_SCANNED_ID = "id";
    public static final String COLUMN_SCANNED_DATA = "data";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public static final String TABLE_REGISTRATIONS = "registrations"; // Tabela de inscrições
    public static final String COLUMN_REGISTRATION_ID = "id";
    public static final String COLUMN_REGISTRATION_STUDENT_RGM = "studentRgm";
    public static final String COLUMN_REGISTRATION_EVENT_ID = "event_id";

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
        String createStudentsTable = "CREATE TABLE IF NOT EXISTS " + TABLE_STUDENTS + " (" +
                COLUMN_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STUDENT_NAME + " TEXT, " +
                COLUMN_STUDENT_EMAIL + " TEXT UNIQUE, " +
                COLUMN_STUDENT_RGM + " TEXT UNIQUE, " +
                COLUMN_STUDENT_PASSWORD + " TEXT)";
        db.execSQL(createStudentsTable);

        // Criação da tabela de professores
        String createTeachersTable = "CREATE TABLE IF NOT EXISTS " + TABLE_TEACHERS + " (" +
                COLUMN_TEACHER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TEACHER_NAME + " TEXT, " +
                COLUMN_TEACHER_EMAIL + " TEXT UNIQUE, " +
                COLUMN_TEACHER_PASSWORD + " TEXT)";
        db.execSQL(createTeachersTable);

        // Criação da tabela de eventos
        String createEventsTable = "CREATE TABLE IF NOT EXISTS " + TABLE_EVENTS + " (" +
                COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EVENT_TITLE + " TEXT, " +
                COLUMN_EVENT_DESCRIPTION + " TEXT, " +
                COLUMN_EVENT_TEACHER_ID + " INTEGER)";
        db.execSQL(createEventsTable);

        // Criação da tabela de dados escaneados
        String createScannedDataTable = "CREATE TABLE IF NOT EXISTS " + TABLE_SCANNED_DATA + " (" +
                COLUMN_SCANNED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SCANNED_DATA + " TEXT, " +
                COLUMN_TIMESTAMP + " TEXT)";
        db.execSQL(createScannedDataTable);

        // Criação da tabela de inscrições
        String createRegistrationsTable = "CREATE TABLE IF NOT EXISTS " + TABLE_REGISTRATIONS + " (" +
                COLUMN_REGISTRATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_REGISTRATION_STUDENT_RGM + " INTEGER, " +
                COLUMN_REGISTRATION_EVENT_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_REGISTRATION_STUDENT_RGM + ") REFERENCES " + TABLE_STUDENTS + "(" + COLUMN_STUDENT_ID + "), " +
                "FOREIGN KEY(" + COLUMN_REGISTRATION_EVENT_ID + ") REFERENCES " + TABLE_EVENTS + "(" + COLUMN_EVENT_ID + "))";
        db.execSQL(createRegistrationsTable);
    }

    // Método para obter o ID do aluno pelo RGM
    public int getStudentIdByRGM(String rgm) {
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT " + COLUMN_STUDENT_ID + " FROM " + TABLE_STUDENTS + " WHERE " + COLUMN_STUDENT_RGM + "=?", new String[]{rgm})) {
            if (cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_ID));
            }
        }
        return -1; // Retorna -1 se não encontrar
    }

    // Método para o professor inserir evento
    public void addEvent(String title, String description) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_EVENT_TITLE, title);
            values.put(COLUMN_EVENT_DESCRIPTION, description);

            db.insertOrThrow(TABLE_EVENTS, null, values);
            Log.d("DatabaseHelper", "Evento adicionado: " + title);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao adicionar evento: " + e.getMessage());
        }
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTRATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEACHERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCANNED_DATA);
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

    // Método para verificar se um aluno existe pelo R GM e senha
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

    // Método para adicionar dados escaneados
    public void addScannedData(String data) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SCANNED_DATA, data);
        values.put(COLUMN_TIMESTAMP, getCurrentTimeInBrasilia());

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.insertOrThrow(TABLE_SCANNED_DATA, null, values);
            Log.d("DatabaseHelper", "Dados escaneados adicionados: " + data);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao adicionar dados escaneados: " + e.getMessage());
        }
    }

    // Método para obter a hora atual em Brasília
    private String getCurrentTimeInBrasilia() {
        java.util.Date date = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("America/Sao_Paulo"));
        return sdf.format(date);
    }

    // Método para adicionar uma inscrição
    public boolean addRegistration(String studentRgm, int eventId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_REGISTRATION_STUDENT_RGM, studentRgm); // Alterado para usar studentRgm
        values.put(COLUMN_REGISTRATION_EVENT_ID, eventId);

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            long result = db.insert(TABLE_REGISTRATIONS, null, values);
            return result != -1; // Retorna true se a inserção foi bem-sucedida
        }
    }

    // Método para obter eventos registrados
    public Cursor getRegisteredEvents(String studentRgm) { // Alterado para aceitar studentRgm
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT e.* FROM " + TABLE_EVENTS + " e " +
                "JOIN " + TABLE_REGISTRATIONS + " r ON e." + COLUMN_EVENT_ID + " = r." + COLUMN_REGISTRATION_EVENT_ID +
                " WHERE r." + COLUMN_REGISTRATION_STUDENT_RGM + " = ?"; // Alterado para usar COLUMN_REGISTRATION_STUDENT_RGM
        Log.d("DatabaseHelper", "Consulta SQL: " + query + " | studentRgm: " + studentRgm);
        return db.rawQuery(query, new String[]{studentRgm}); // Alterado para passar studentRgm
    }

    // Método para verificar se um aluno já está inscrito em um evento
    public boolean isStudentRegistered(String studentRgm, int eventId) {
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_REGISTRATIONS + " WHERE " + COLUMN_REGISTRATION_STUDENT_RGM + "=? AND " + COLUMN_REGISTRATION_EVENT_ID + "=?", new String[]{studentRgm, String.valueOf(eventId)})) {
            return cursor.getCount() > 0; // Retorna true se o aluno já estiver inscrito
        }
    }

    // Método para remover a inscrição de um aluno em um evento
    public boolean removeRegistration(String studentRGM, int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Define a condição para a exclusão
        String whereClause = COLUMN_REGISTRATION_STUDENT_RGM + " = ? AND " + COLUMN_REGISTRATION_EVENT_ID + " = ?";
        String[] whereArgs = new String[]{studentRGM, String.valueOf(eventId)};

        // Executa a exclusão e verifica se foi bem-sucedida
        int rowsAffected = db.delete(TABLE_REGISTRATIONS, whereClause, whereArgs);
        db.close();
        return rowsAffected > 0; // Retorna true se pelo menos uma linha foi afetada
    }

    // Método para registrar a presença do aluno em um evento
    public boolean registerAttendance(String studentRgm, int eventId, String timestamp) {
        ContentValues values = new ContentValues();
        values.put("studentRgm", studentRgm);
        values.put("eventId", eventId);


        try (SQLiteDatabase db = this.getWritableDatabase()) {
            long result = db.insert("attendance", null, values); // ou use o nome real da tabela
            return result != -1; // Retorna true se a inserção foi bem-sucedida
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao registrar presença: " + e.getMessage());
            return false;
        }
    }
    public List<String> getScannedStudents(int eventId) {
        List<String> scannedStudents = new ArrayList<>();
        String query = "SELECT student_rgm FROM registrations WHERE event_id = ?";

        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(eventId)})) {

            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range")
                    String studentRgm = cursor.getString(cursor.getColumnIndex("student_rgm"));
                    scannedStudents.add(studentRgm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao obter alunos escaneados: " + e.getMessage());
        }

        return scannedStudents;
    }
    public boolean deleteEvent(int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete("events", "event_id = ?", new String[]{String.valueOf(eventId)});
        db.close(); // Sempre feche o banco de dados após operações de escrita/leitura
        return rowsDeleted > 0; // Retorna true se pelo menos uma linha foi excluída
    }
}