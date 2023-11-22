package com.zaroslikov.mycountjava2;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zaroslikov.mycountjava2.db.MyDataBaseHelper;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private MyDataBaseHelper myDB;
    private TextView count;
    private ImageButton plus, minus;
    private int countPush, stepPush, iD;
    private BottomSheetDialog bottomSheetDialogSetting, bottomSheetDialogList;

    private Button buttonSetting;

    private TextInputLayout nameEdit, stepEdit;
    private String appBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        count = findViewById(R.id.count);
        myDB = new MyDataBaseHelper(this);

        plus = findViewById(R.id.button_plus);
        minus = findViewById(R.id.button_minus);

        showBottomSheetList();
        showBottomSheetSetting();

        Cursor cursor = myDB.lastReadProject();

        if (cursor == null && cursor.getCount() == 0) {
            Calendar calendar = Calendar.getInstance();
            String time = (calendar.get(Calendar.DAY_OF_MONTH)) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + (calendar.get(Calendar.YEAR));
            myDB.insertToDb("Мой счет", 0, 1, 0, time);
            appBarTitle = "Мой счет";
            iD = 1;
            countPush = 0;
            stepPush = 1;
        } else {
            cursor.moveToNext();
            iD = cursor.getInt(0);
            appBarTitle = cursor.getString(1);
            countPush = cursor.getInt(2);
            stepPush = cursor.getInt(3);
        }
        cursor.close();

        MaterialToolbar appBar = findViewById(R.id.topAppBar);
        appBar.setTitle(appBarTitle);

        appBar.setNavigationIcon(R.drawable.baseline_format_list_bulleted_24);
        appBar.setOnMenuItemClickListener(item -> {
            int position = item.getItemId();
            if (position == R.id.refresg) {
                countPush = 0;
                Toast.makeText(this, "Обновление счета", Toast.LENGTH_SHORT).show();
            } else if (position == R.id.setting) {
                bottomSheetDialogSetting.show();
            }
            return true;
        });
        appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogSetting.show();
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countPush += stepPush;
                count.setText(String.valueOf(countPush));
                myDB.updateToCount(iD, countPush, 1);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countPush -= stepPush;
                count.setText(String.valueOf(countPush));
                myDB.updateToCount(iD, countPush, 1);
            }
        });
    }

    //Добавляем bottobSheet
    public void showBottomSheetSetting() {

        bottomSheetDialogSetting = new BottomSheetDialog(this);
        bottomSheetDialogSetting.setContentView(R.layout.fragment_bottom);

        stepEdit = bottomSheetDialogSetting.findViewById(R.id.step_count);
        nameEdit = bottomSheetDialogSetting.findViewById(R.id.name_count);

        buttonSetting = bottomSheetDialogSetting.findViewById(R.id.name_count);

        nameEdit.getEditText().setText(appBarTitle);

        buttonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nameEdit.setErrorEnabled(false);
                stepEdit.setErrorEnabled(false);

                if (nameEdit.getEditText().getText().toString().equals("") || stepEdit.getEditText().getText().toString().equals("")) {

                    if (nameEdit.getEditText().getText().toString().equals("")) {
                        nameEdit.setError("Укажите название!");
                        nameEdit.getError();
                    }

                    if (stepEdit.getEditText().getText().toString().equals("")) {
                        stepEdit.setError("Укажите размер шага!");
                        stepEdit.getError();
                    }

                } else {
                    String name = nameEdit.getEditText().getText().toString().replaceAll(",", ".").replaceAll("[^\\d.]", "");
                    int step = Integer.parseInt(stepEdit.getEditText().getText().toString().replaceAll(",", ".").replaceAll("[^\\d.]", ""));

                    myDB.updateToSetting(iD, name, step);

                }
            }
        });
    }

    public void showBottomSheetList() {

        bottomSheetDialogList = new BottomSheetDialog(this);
        bottomSheetDialogList.setContentView(R.layout.fragment_setting_bottom);

//            animalsSpinerSheet = bottomSheetDialog.findViewById(R.id.menu);
//            categorySpinerSheet = bottomSheetDialog.findViewById(R.id.menu2);
//
//            animalsSpinerSheet.stVisibility(View.GONE);
//            categorySpinerSheet.setVisibility(View.GONE);
//
//            dataSheet = bottomSheetDialog.findViewById(R.id.data_sheet);
//            buttonSheet = bottomSheetDialog.findViewById(R.id.button_sheet);
    }


}