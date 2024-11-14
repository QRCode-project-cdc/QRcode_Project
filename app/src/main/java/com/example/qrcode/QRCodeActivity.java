package com.example.qrcode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONObject;

public class QRCodeActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private ImageView imageViewQRCode;
    private TextView textViewQRInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        db = DatabaseHelper.getInstance(this);
        imageViewQRCode = findViewById(R.id.imageViewQRCode);
        textViewQRInfo = findViewById(R.id.textViewQRInfo);

        String userRGM = getIntent().getStringExtra("USER_RGM");
        int eventId = getIntent().getIntExtra("EVENT_ID", -1);
        String eventTitle = getIntent().getStringExtra("EVENT_TITLE");

        Log.d("QRCodeActivity", "RGM recebido: " + userRGM);
        generateQRCode(userRGM, eventId, eventTitle);
    }

    private void generateQRCode(String rgm, int eventId, String eventTitle) {
        if (rgm == null || rgm.isEmpty()) {
            textViewQRInfo.setText("RGM não fornecido.");
            return;
        }

        rgm = rgm.trim();

        if (!db.rgmExists(rgm)) {
            textViewQRInfo.setText("Aluno não encontrado.");
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("RGM", rgm);
            jsonObject.put("EventoID", eventId);
        } catch (Exception e) {
            textViewQRInfo.setText("Erro ao criar JSON: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            BitMatrix bitMatrix = barcodeEncoder.encode(jsonObject.toString(), BarcodeFormat.QR_CODE, 250, 250);
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