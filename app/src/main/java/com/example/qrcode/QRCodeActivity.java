package com.example.qrcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log; // Importando Log para depuração
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRCodeActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private ImageView imageViewQRCode;
    private TextView textViewQRInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        // Inicializando o DatabaseHelper usando o método getInstance
        db = DatabaseHelper.getInstance(this);
        imageViewQRCode = findViewById(R.id.imageViewQRCode);
        textViewQRInfo = findViewById(R.id.textViewQRInfo);

        // Supondo que o RGM do usuário foi passado pela Intent
        String userRGM = getIntent().getStringExtra("USER_RGM");
        Log.d("QRCodeActivity", "RGM recebido: " + userRGM); // Log para depuração
        generateQRCode(userRGM);
    }

    private void generateQRCode(String rgm) {
        if (rgm == null || rgm.isEmpty()) {
            textViewQRInfo.setText("RGM não fornecido.");
            return;
        }

        // Normaliza o RGM
        rgm = rgm.trim();

        // Verifica se o RGM existe
        if (!db.rgmExists(rgm)) {
            textViewQRInfo.setText("Aluno não encontrado.");
            return;
        }

        // Obtém as informações do aluno
        String name = db.getStudentNameByRGM(rgm);
        String email = db.getStudentEmailByRGM(rgm);

        // Formata as informações para o QR Code
        String qrContent = "Nome: " + name + "\nE-mail: " + email + "\nRGM: " + rgm;

        // Gera o QR Code
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            BitMatrix bitMatrix = barcodeEncoder.encode(qrContent, BarcodeFormat.QR_CODE, 250, 250);
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageViewQRCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            textViewQRInfo.setText("Erro ao gerar QR Code: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            textViewQRInfo.setText("Erro inesperado ao gerar QR Code: " + e.getMessage());
            e.printStackTrace();
        }
    }
}