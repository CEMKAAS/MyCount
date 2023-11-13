package com.zaroslikov.mycountjava2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zaroslikov.mycountjava2.db.MyDataBaseHelper;

public class MainActivity extends AppCompatActivity {

    private MyDataBaseHelper myDB;

    private TextView count;


    private ImageButton plus, minus;
    private int countPush = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        count = findViewById(R.id.count);
        myDB = new MyDataBaseHelper(this);

        plus = findViewById(R.id.button_plus);
        minus = findViewById(R.id.button_minus);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countPush++;
                String s = String.valueOf(countPush);
                count.setText(s);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countPush--;
                String s = String.valueOf(countPush);
                count.setText(s);
            }
        });
    }
}