package com.example.project_8_1_diary2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    DatePicker dP;
    EditText edt;
    Button bt1;
    String fN;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("간단 일기장");

        dP = (DatePicker) findViewById(R.id.dP);
        edt = (EditText) findViewById(R.id.edt);
        bt1 = (Button) findViewById(R.id.bt1);

        Calendar cal = Calendar.getInstance();
        int cY = cal.get(Calendar.YEAR);
        int cM = cal.get(Calendar.MONTH);
        int cD = cal.get(Calendar.DAY_OF_MONTH);

        dP.init(cY, cM, cD, new DatePicker.OnDateChangedListener() {
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                fN = Integer.toString(year) + "_" + Integer.toString(monthOfYear + 1) + "_" + Integer.toString(dayOfMonth) + ".txt";
                String str = readDiary(fN);
                edt.setText(str);
                bt1.setEnabled(true);
            }
        });

        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    FileOutputStream outFs = openFileOutput(fN, Context.MODE_PRIVATE);
                    String str = edt.getText().toString();
                    outFs.write(str.getBytes());
                    outFs.close();
                    Toast.makeText(getApplicationContext(), fN + " 이 저장됨", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                }
            }
        });

        if (edt == null) {
            bt1.setText("새로저장");
            edt.setHint("일기 없음");
            bt1.setEnabled(false);
        } else {
            bt1.setText("수정하기");
        }
    }

    String readDiary(String fN) {
        String dStr = null;
        FileInputStream inFs;
        try {
            inFs = openFileInput(fN);
            byte[] txt = new byte[500];
            inFs.read(txt);
            inFs.close();
            dStr = (new String(txt)).trim();
            bt1.setText("수정하기");
        } catch (IOException e) {
            edt.setHint("일기 없음");
            bt1.setText("새로 저장");
        }
        return dStr;
    }

}