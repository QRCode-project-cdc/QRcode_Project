package com.example.qrcode;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

public class CreateEvent extends AppCompatActivity {

    private EditText edtNomeEvento, edtDescEvento;
    private Button buttonCadEvento;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        edtNomeEvento = findViewById(R.id.edtNomeEvento);
        edtDescEvento = findViewById(R.id.edtDescEvento);
        buttonCadEvento = findViewById(R.id.buttonCadEvento);

        // Inicializa o DatabaseHelper
        databaseHelper = DatabaseHelper.getInstance(this);

        buttonCadEvento.setOnClickListener(v -> {
            String nomeEvento = edtNomeEvento.getText().toString();
            String descEvento = edtDescEvento.getText().toString();

            if (!nomeEvento.isEmpty() && !descEvento.isEmpty()) {
                databaseHelper.addEvent(nomeEvento, descEvento);
                Toast.makeText(CreateEvent.this, "Evento criado com sucesso!", Toast.LENGTH_SHORT).show();

                // Limpar campos
                edtNomeEvento.setText("");
                edtDescEvento.setText("");

                finish();
            } else {
                Toast.makeText(CreateEvent.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            }
        });

        // Chama a função para verificar as colunas após a criação do banco de dados
        verificarColunas();
    }

    // Função de Diagnóstico: Verificar as colunas da tabela
    public void verificarColunas() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("PRAGMA table_info(" + DatabaseHelper.TABLE_EVENTS + ");", null);

        if (cursor != null) {
            // Verifica as colunas e imprime o nome de cada uma no Log
            while (cursor.moveToNext()) {
                String columnName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                Log.d("Tabela Eventos", "Coluna encontrada: " + columnName);  // Usando Log para depuração
            }
            cursor.close();
        }
        db.close();
    }
}