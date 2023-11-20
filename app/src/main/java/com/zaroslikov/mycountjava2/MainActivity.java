package com.zaroslikov.mycountjava2;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.zaroslikov.mycountjava2.db.MyDataBaseHelper;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private MyDataBaseHelper myDB;
    private TextView count;
    private ImageButton plus, minus;
    private int countPush;
    private int stepPush;
    private BottomSheetDialog bottomSheetDialog;
    private String appBarTitle;
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
            appBarTitle = "Мой счет";
            countPush = 0;
            stepPush = 1;
        }else{
            cursor.moveToNext();
            appBarTitle = cursor.getString(0);
            countPush = cursor.getInt(1);
            stepPush = cursor.getInt(2);
        }

        MaterialToolbar appBar = findViewById(R.id.topAppBar);
        appBar.setTitle(appBarTitle);

        appBar.setNavigationIcon(R.drawable.baseline_format_list_bulleted_24);
        appBar.setOnMenuItemClickListener(item -> {
            int position = item.getItemId();
            if (position == R.id.refresg) {
                bottomSheetDialog.show();
            } else if (position == R.id.setting) {
                bottomSheetDialog.show();
            }
            return true;
        });
        appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();
            }
        });

        //Добавляем bottobSheet
        public void showBottomSheetDialog() {

            bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(R.layout.fragment_bottom);

//            animalsSpinerSheet = bottomSheetDialog.findViewById(R.id.menu);
//            categorySpinerSheet = bottomSheetDialog.findViewById(R.id.menu2);
//
//            animalsSpinerSheet.stVisibility(View.GONE);
//            categorySpinerSheet.setVisibility(View.GONE);
//
//            dataSheet = bottomSheetDialog.findViewById(R.id.data_sheet);
//            buttonSheet = bottomSheetDialog.findViewById(R.id.button_sheet);
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