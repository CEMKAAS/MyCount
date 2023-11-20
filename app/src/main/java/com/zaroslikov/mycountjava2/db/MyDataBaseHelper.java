package com.zaroslikov.mycountjava2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.util.ScopeUtil;
import com.zaroslikov.mycountjava2.MainActivity;

import java.util.Calendar;

public class MyDataBaseHelper extends SQLiteOpenHelper {
        private Context context;

        public MyDataBaseHelper(Context context) {
            super(context, MyConstanta.DB_NAME, null, MyConstanta.DB_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(MyConstanta.TABLE_STRUCTURE_PROJECT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(MyDataBaseHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS MyConstanta.DROP_TABLE_PROJECT");
            onCreate(db);
        }

        public void deleteAllData() {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + MyConstanta.TABLE_NAME);
        }

        public boolean deleteDatabase(Context context) {
            return context.deleteDatabase(MyConstanta.DB_NAME);
        }


        public Cursor lastReadProject() {
            String query = "SELECT * FROM " + MyConstanta.TABLE_NAME +
                    " Where " + MyConstanta.LASTCOUNT + "=?";

            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = null;
            if (db != null) {
                cursor = db.rawQuery(query, new String[]{String.valueOf(1)});
            }
            return cursor;
        }


        public void insertToDb(String title, int count, int step,int lastCount, String time) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(MyConstanta.TITLE, title);
            cv.put(MyConstanta.COUNT, count);
            cv.put(MyConstanta.STEP, step);
            cv.put(MyConstanta.LASTCOUNT, lastCount);
            cv.put(MyConstanta.TIME, time);
            db.insert(MyConstanta.TABLE_NAME, null, cv);
        }


        public void updateToCount(int idCount, int count, int lastCount) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            Calendar calendar = Calendar.getInstance();
            String time = (calendar.get(Calendar.DAY_OF_MONTH)) + "." + (calendar.get(Calendar.MONTH) + 1)+ "." + (calendar.get(Calendar.YEAR));

            cv.put(MyConstanta.COUNT, count);
            cv.put(MyConstanta.LASTCOUNT, lastCount);
            long id = db.update(MyConstanta.TABLE_NAME, cv, "id=?", new String[]{String.valueOf(idCount)});

            if (id == -1) {
                Toast.makeText(context, "Ошибка!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Успешно обновлено!", Toast.LENGTH_SHORT).show();
            }
        }






        //        public Cursor selectProductJoin(int propertyId, String productName, String tableName, String suffix) {
//            String query = "SELECT " + MyConstanta.TITLEPRODUCT +
//                    ", sum(" + MyConstanta.QUANTITY + "), " + MyConstanta.SUFFIX +
//                    " FROM " + tableName + " ad " +
//                    "JOIN " + MyConstanta.TABLE_NAME_PROJECT_PRODUCT + " pp " +
//                    "ON pp." + MyConstanta._ID + " = " + "ad." + MyConstanta.IDPP +
//
//                    " JOIN " + MyConstanta.TABLE_NAME_PRODUCT + " prod " +
//                    "ON prod." + MyConstanta._ID + " = " + " pp." + MyConstanta.IDPRODUCT +
//
//                    " JOIN " + MyConstanta.TABLE_NAME + " proj " +
//                    "ON proj." + MyConstanta._ID + " = " + " pp." + MyConstanta.IDPROJECT +
//
//                    " WHERE proj." + MyConstanta._ID + "=? and " + MyConstanta.TITLEPRODUCT + "=? and " + MyConstanta.SUFFIX + "=? " +
//                    "group by " + MyConstanta.TITLEPRODUCT + ", " + MyConstanta.SUFFIX;
//
//            SQLiteDatabase db = this.getReadableDatabase();
//
//            Cursor cursor = null;
//            if (db != null) {
//
//                cursor = db.rawQuery(query, new String[]{String.valueOf(propertyId), productName, suffix});
//            }
//
//            return cursor;
//        }
    }
