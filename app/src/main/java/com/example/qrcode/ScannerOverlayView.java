package com.example.qrcode;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ScannerOverlayView extends View {
    private Paint borderPaint;

    public ScannerOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Inicializar a pintura da borda
        borderPaint = new Paint();
        borderPaint.setColor(0xFFFFFFFF); // Cor da borda (branco)
        borderPaint.setStyle(Paint.Style.STROKE); // Apenas a borda
        borderPaint.setStrokeWidth(8); // Espessura da borda
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Desenhar um retângulo no centro da visualização
        int width = getWidth();
        int height = getHeight();
        int rectSize = 650; // Tamanho da área de escaneamento
        int left = (width - rectSize) / 2;
        int top = (height - rectSize) / 2;
        int right = left + rectSize;
        int bottom = top + rectSize;

        // Desenhar o fundo semi-transparente
        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(0x00000000); // Cor de fundo semi-transparente
        canvas.drawRect(0, 0, width, height, backgroundPaint); // Preencher toda a visualização

        // Limpar a área do retângulo (deixar transparente)
        canvas.drawRect(left, top, right, bottom, backgroundPaint); // Limpar a área do retângulo

        // Desenhar a borda
        canvas.drawRect(left, top, right, bottom, borderPaint); // Desenhar a borda
    }
}