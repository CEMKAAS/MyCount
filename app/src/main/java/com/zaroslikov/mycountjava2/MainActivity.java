package com.zaroslikov.mycountjava2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zaroslikov.mycountjava2.db.AdapterList;
import com.zaroslikov.mycountjava2.db.CountPerson;
import com.zaroslikov.mycountjava2.db.MyDataBaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private MyDataBaseHelper myDB;
    private TextView count;
    private ImageButton plus, minus, add;
    private int countPush, stepPush, iD;
    private BottomSheetDialog bottomSheetDialogSetting, bottomSheetDialogList;
    private AdapterList adapterList;
    private List<CountPerson> countPerson;
    private RecyclerView recyclerView;
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


        Cursor cursor = myDB.lastReadProject();

        if (cursor == null || cursor.getCount() == 0) {
            Calendar calendar = Calendar.getInstance();
            String time = (calendar.get(Calendar.DAY_OF_MONTH)) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + (calendar.get(Calendar.YEAR));
            myDB.insertToDb("Мой счет", 0, 1, 1, time);
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

        count.setText(String.valueOf(countPush));

        showBottomSheetList();
        showBottomSheetSetting();

        MaterialToolbar appBar = findViewById(R.id.topAppBar);
        appBar.setTitle(appBarTitle);

        appBar.setNavigationIcon(R.drawable.baseline_format_list_bulleted_24);
        appBar.setOnMenuItemClickListener(item -> {
            int position = item.getItemId();
            if (position == R.id.refresg) {
                countPush = 0;
                count.setText(String.valueOf(countPush));
                Toast.makeText(this, "Обновление счета", Toast.LENGTH_SHORT).show();
            } else if (position == R.id.setting) {
                bottomSheetDialogSetting.show();
            }
            return true;
        });
        appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogList.show();
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
        bottomSheetDialogSetting.setContentView(R.layout.fragment_setting_bottom);

        stepEdit = bottomSheetDialogSetting.findViewById(R.id.step_count);
        nameEdit = bottomSheetDialogSetting.findViewById(R.id.name_count);

        buttonSetting = bottomSheetDialogSetting.findViewById(R.id.button_sheet);

        nameEdit.getEditText().setText(appBarTitle);
        stepEdit.getEditText().setText(String.valueOf(stepPush));

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
        bottomSheetDialogList.setContentView(R.layout.fragment_bottom);
        recyclerView = bottomSheetDialogList.findViewById(R.id.recyclerView);

        countPerson = new ArrayList<>();
        setAdapterList();

        //Создание адаптера
        adapterList = new AdapterList(countPerson);

        recyclerView.setAdapter(adapterList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapterList.setListener(new AdapterList.Listener() {
            @Override
            public void onClick(int position, CountPerson countPerson) {
                myDB.updateToLast();
                appBarTitle = countPerson.getName();
                countPush = countPerson.getCount();
                stepPush = countPerson.getStep();
                iD = countPerson.getId();
                myDB.updateToCount(iD, countPush, 1);

            }
        });

        add = bottomSheetDialogList.findViewById(R.id.add_count);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
                builder.setTitle("Добавить новый счетчик");
                builder.setMessage("Укажите новый название");

                TextInputLayout input = new TextInputLayout(MainActivity.this);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                builder.setView(input);

                builder.setPositiveButton("Создать", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = input.getEditText().getText().toString();
                        myDB.updateToLast();
                        Calendar calendar = Calendar.getInstance();
                        String time = (calendar.get(Calendar.DAY_OF_MONTH)) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + (calendar.get(Calendar.YEAR));
                        myDB.insertToDb(name, 0, 1, 1, time);

                        Cursor cursor = myDB.lastReadProject();
                        cursor.moveToNext();
                        iD = cursor.getInt(0);
                        appBarTitle = cursor.getString(1);
                        countPush = cursor.getInt(2);
                        stepPush = cursor.getInt(3);
                        cursor.close();

                        bottomSheetDialogList.cancel();


                    }
                });
                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();
            }
        });


    }

    public void setAdapterList() {
        Cursor cursor = myDB.allProject();

        countPerson.clear();

        if (cursor != null || cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                countPerson.add(new CountPerson(cursor.getString(1), cursor.getInt(2), cursor.getString(5),
                        cursor.getInt(3), cursor.getInt(0)));
            }
        }
        cursor.close();
    }

}