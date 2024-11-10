package com.example.qrcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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

        db = new DatabaseHelper(this);
        imageViewQRCode = findViewById(R.id.imageViewQRCode);
        textViewQRInfo = findViewById(R.id.textViewQRInfo);

        // Supondo que o email do usuário foi passado pela Intent
        String userEmail = getIntent().getStringExtra("USER_EMAIL");
        generateQRCode(userEmail);
    }

    private void generateQRCode(String email) {
        if (email == null || !db.userExists(email)) {
            textViewQRInfo.setText("Usuário não encontrado.");
            return;
        }

        // Obtém as informações do usuário
        String name = db.getUserName(email);
        String rgm = db.getUserRGM(email); // Adicione este método ao DatabaseHelper se ainda não existir.

        // Formata as informações para o QR Code
        String qrContent = "Nome: " + name + "\nEmail: " + email + "\nRGM: " + rgm;

        // Gera o QR Code
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            BitMatrix bitMatrix = barcodeEncoder.encode(qrContent, BarcodeFormat.QR_CODE, 250, 250);
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageViewQRCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            textViewQRInfo.setText("Erro ao gerar QR Code");
            e.printStackTrace();
        }
    }
}
