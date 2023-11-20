package com.zaroslikov.mycountjava2;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zaroslikov.mycountjava2.db.MyDataBaseHelper;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private MyDataBaseHelper myDB;
    private TextView count;
    private ImageButton plus, minus;
    private int countPush;
    private int stepPush;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        count = findViewById(R.id.count);
        myDB = new MyDataBaseHelper(this);

        plus = findViewById(R.id.button_plus);
        minus = findViewById(R.id.button_minus);

        Cursor cursor = myDB.lastReadProject();

        if (cursor == null && cursor.getCount() == 0){
            Calendar calendar = Calendar.getInstance();
            String time = (calendar.get(Calendar.DAY_OF_MONTH)) + "." + (calendar.get(Calendar.MONTH) + 1)+ "." + (calendar.get(Calendar.YEAR));
            myDB.insertToDb("Мой счет", 0,1,0, time);
            countPush = 0;
            stepPush = 1;
        }else{
            cursor.moveToNext();
            countPush = cursor.getInt(1);
            stepPush = cursor.getInt(2);

        }


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countPush += stepPush;

                String s = String.valueOf(countPush);
                count.setText(s);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countPush -= stepPush;
                String s = String.valueOf(countPush);
                count.setText(s);
            }
        });
    }
}