package com.example.ysc_qr_;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.UnsupportedEncodingException;

public class VisitorActivity extends AppCompatActivity{

    private ImageView QRcode;
    private Button btn_re;
    private TextView name_txt;
    private String information=" ";

    public void CreateQR(){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(information, BarcodeFormat.QR_CODE,950,950);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            QRcode.setImageBitmap(bitmap);
        }catch (Exception e){}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visitor_activity);

        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String userName_qr = null;
        try {
            userName_qr = new String(intent.getStringExtra("userName").getBytes(), "ISO-8859-1");
            // QR코드 스캔시 한글이 인식이 안되서 별도로 진행하는 인코딩 작업입니다.
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        final int userMajor =  intent.getExtras().getInt("userMajor");

        name_txt = (TextView)findViewById(R.id.name_txt);
        btn_re = (Button)findViewById(R.id.re_btn);
        QRcode = (ImageView)findViewById(R.id.QR_img);
        information = userName_qr +" "+ userMajor;

        name_txt.setText(userName);

        CreateQR();

        btn_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"새로고침",Toast.LENGTH_SHORT).show();
                CreateQR();
            }
        });
    }
}
