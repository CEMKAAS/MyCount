package com.zaroslikov.mycountjava2.db;

public class MyConstanta {
    public static final String DB_NAME = "myCounts"; //База данных
    public static final int DB_VERSION = 1; //Версия базы данных

    public static final String TABLE_NAME = "List"; // Название таблицы
    public static final String _ID = "id"; // Индефикатор НУМЕРАЦИЯ СТРОК
    public static final String TITLE  = "NameCount"; // Название описание (название счетчика)
    public static final String COUNT = "Count"; // Кол-во
    public static final String STEP = "Step"; // Шаг счетчика
    public static final String LASTCOUNT = "LactCount"; // Последний счетчик
    public static final String TIME = "Time"; // Последнее время изменения

    public static final String TABLE_STRUCTURE_PROJECT = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY, "
            + TITLE + " TEXT, "
            + COUNT + " INTEGER, "
            + STEP + " INTEGER, "
            + LASTCOUNT + " INTEGER, "
            + TIME + " TEXT)";
}
