package com.zaroslikov.mycountjava2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.yandex.mobile.ads.banner.BannerAdEventListener;
import com.yandex.mobile.ads.banner.BannerAdSize;
import com.yandex.mobile.ads.banner.BannerAdView;
import com.yandex.mobile.ads.common.AdError;
import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.AdRequestConfiguration;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.common.AdSize;
import com.yandex.mobile.ads.common.ImpressionData;
import com.yandex.mobile.ads.interstitial.InterstitialAd;
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener;
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener;
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader;
import com.zaroslikov.mycountjava2.db.AdapterList;
import com.zaroslikov.mycountjava2.db.CountPerson;
import com.zaroslikov.mycountjava2.db.MyDataBaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MyDataBaseHelper myDB;
    private TextView count;
    private ImageButton plus, minus, add, back;
    private int countPush, stepPush, iD;
    private BottomSheetDialog bottomSheetDialogSetting, bottomSheetDialogList;
    private AdapterList adapterList;
    private List<CountPerson> countPerson;
    private RecyclerView recyclerView;
    private Button buttonSetting;
    private TextInputLayout nameEdit, stepEdit;
    private String appBarTitle;
    @Nullable
    private InterstitialAd mInterstitialAd = null;
    @Nullable
    private InterstitialAdLoader mInterstitialAdLoader = null;
    private BannerAdView mBannerAdView;//Реклама от Яндекса

    private MaterialToolbar appBar;

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

        appBar = findViewById(R.id.topAppBar);
        appBar.setTitle(appBarTitle);

        appBar.setNavigationIcon(R.drawable.baseline_format_list_bulleted_24);
        appBar.setOnMenuItemClickListener(item -> {
            int position = item.getItemId();
            if (position == R.id.refresg) {
                countPush = 0;
                count.setText(String.valueOf(countPush));
//                Toast.makeText(this, "Обновление счета", Toast.LENGTH_SHORT).show();
            } else if (position == R.id.setting) {
                showBottomSheetSetting();
                bottomSheetDialogSetting.show();
            } else if (position == R.id.delet) {
                delete();
            }

            return true;
        });
        appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetList();
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

        mBannerAdView = (BannerAdView) findViewById(R.id.banner_ad_view);
        mBannerAdView.setAdUnitId("R-M-4928527-1"); //Вставляется свой айди от яндекса
        mBannerAdView.setAdSize(BannerAdSize.stickySize(this, 320));//Размер банера
        final AdRequest adRequest = new AdRequest.Builder().build();
        mBannerAdView.loadAd(adRequest);

//рекламав


        mInterstitialAdLoader = new InterstitialAdLoader(this);
        mInterstitialAdLoader.setAdLoadListener(new InterstitialAdLoadListener() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull AdRequestError adRequestError) {
            }
        });
        loadInterstitialAd();
    }

    private void loadInterstitialAd() {
        if (mInterstitialAdLoader != null ) {
            final AdRequestConfiguration adRequestConfiguration =
                    new AdRequestConfiguration.Builder("R-M-4928527-2").build();
            mInterstitialAdLoader.loadAd(adRequestConfiguration);
        }
    }


    private void showAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd.setAdEventListener(new InterstitialAdEventListener() {
                @Override
                public void onAdShown() {
                    // Called when ad is shown.
                }

                @Override
                public void onAdFailedToShow(@NonNull final AdError adError) {
                    // Called when an InterstitialAd failed to show.
                }

                @Override
                public void onAdDismissed() {
                    // Called when ad is dismissed.
                    // Clean resources after Ad dismissed
                    if (mInterstitialAd != null) {
                        mInterstitialAd.setAdEventListener(null);
                        mInterstitialAd = null;
                    }

                    // Now you can preload the next interstitial ad.
                    loadInterstitialAd();
                }

                @Override
                public void onAdClicked() {
                    // Called when a click is recorded for an ad.
                }

                @Override
                public void onAdImpression(@Nullable final ImpressionData impressionData) {
                    // Called when an impression is recorded for an ad.
                }
            });
            mInterstitialAd.show(this);
        }
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
                    appBarTitle = nameEdit.getEditText().getText().toString();
                    stepPush = Integer.parseInt(stepEdit.getEditText().getText().toString().replaceAll(",", ".").replaceAll("[^\\d.]", ""));
                    myDB.updateToSetting(iD, appBarTitle, stepPush);

                    appBar.setTitle(appBarTitle);

                    bottomSheetDialogSetting.dismiss();
                }
            }
        });
    }

    public void showBottomSheetList() {

        bottomSheetDialogList = new BottomSheetDialog(this);
//        bottomSheetDialogList.setContentView(R.layout.fragment_bottom);


        View bottomSheetView = LayoutInflater.from(this).inflate(R.layout.fragment_bottom, null);
        bottomSheetDialogList.setContentView(bottomSheetView);

        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        LinearLayout layout = bottomSheetDialogList.findViewById(R.id.ll);
        layout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);

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

                appBar.setTitle(appBarTitle);
                count.setText(String.valueOf(countPush));

                bottomSheetDialogList.dismiss();

            }
        });

        add = bottomSheetDialogList.findViewById(R.id.add_count);
        back = bottomSheetDialogList.findViewById(R.id.back);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_edit, null);
                TextInputEditText editText1 = view1.findViewById(R.id.editText);

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
                builder.setTitle("Добавить новый счетчик");
                builder.setMessage("Укажите название");
                builder.setView(view1);


//                TextInputLayout input = new TextInputLayout(MainActivity.this);
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.MATCH_PARENT);
//                input.setLayoutParams(lp);
//                builder.setView(input);

                builder.setPositiveButton("Создать", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        textView.setText(MessageFormat.format("Typed text is: {0}", Objects.requireNonNull(editText.getText())));
                        String name = editText1.getText().toString();
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

                        appBar.setTitle(appBarTitle);
                        count.setText(String.valueOf(countPush));

                        bottomSheetDialogList.cancel();

                        showAd();

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogList.cancel();
            }
        });

    }

    public void delete() {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
        builder.setTitle("Удалить счетчик " + appBarTitle + " ?");
        builder.setMessage("Вы уверены, что хотите удалить счетчик?");
        builder.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                myDB.deleteOneRow(iD);

                Cursor cursor = myDB.allProject();

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

                appBar.setTitle(appBarTitle);
                count.setText(String.valueOf(countPush));

            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
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