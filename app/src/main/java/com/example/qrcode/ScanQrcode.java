package com.example.qrcode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;
import com.google.zxing.ResultPoint;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScanQrcode extends AppCompatActivity {

    private BarcodeView barcodeView;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scan_qrcode);

        Button btnViewAttendance = findViewById(R.id.btnViewAttendance);
        btnViewAttendance.setOnClickListener(v -> {
            int currentEventId = 1; // Substituir pelo ID do evento real
            Intent intent = new Intent(ScanQrcode.this, AttendanceList.class);
            intent.putExtra("EVENT_ID", currentEventId);
            startActivity(intent);
        });

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        } else {
            Toast.makeText(this, "View 'main' não encontrada!", Toast.LENGTH_SHORT).show();
        }

        barcodeView = findViewById(R.id.barcode_view);
        if (barcodeView == null) {
            Log.e("ScanQrcode", "BarcodeView não encontrado!");
            Toast.makeText(this, "BarcodeView não encontrado!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        databaseHelper = DatabaseHelper.getInstance(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            initScanner();
        }
    }

    private void initScanner() {
        Log.d("ScanQrcode", "Iniciando o scanner de QR code...");
        barcodeView.decodeSingle(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                String scannedData = result.getText();
                Log.d("ScanQrcode", "QR Code escaneado: " + scannedData);
                Toast.makeText(ScanQrcode.this, "QR Code escaneado: " + scannedData, Toast.LENGTH_SHORT).show();

                // Process the scanned data as JSON
                processScannedData(scannedData);
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
                // Handle possible result points if needed
            }
        });
    }

    private void processScannedData(String scannedData) {
        try {
            JSONObject jsonObject = new JSONObject(scannedData);
            String rgm = jsonObject.getString("RGM");
            int eventId = jsonObject.getInt("EventoID");

            Log.d("ScanQrcode", "RGM: " + rgm + ", EventoID: " + eventId);

            // Adiciona os dados escaneados ao banco de dados
            databaseHelper.addScannedData(scannedData);

            // Registra a presença do aluno
            registerAttendance(rgm, eventId);

        } catch (Exception e) {
            Log.e("ScanQrcode", "Erro ao processar dados escaneados: " + e.getMessage());
            Toast.makeText(this, "Erro ao processar QR Code: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void registerAttendance(String rgm, int eventId) {
        String timestamp = getCurrentTime();
        if (databaseHelper.registerAttendance(rgm, eventId, timestamp)) {
            Toast.makeText (this, "Presença registrada para o aluno: " + rgm, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erro ao registrar presença.", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (barcodeView != null) {
            barcodeView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (barcodeView != null) {
            barcodeView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (barcodeView != null) {
            barcodeView.stopDecoding();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("ScanQrcode", "Permissão da câmera concedida");
                initScanner();
            } else {
                Toast.makeText(this, "A permissão da câmera é necessária para escanear QR Codes", Toast.LENGTH_SHORT).show();
            }
        }
    }
}