package com.zaroslikov.mycountjava2.db;

public class CountPerson {

    public CountPerson(String name, int count, String time) {
        this.name = name;
        Count = count;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String name;
    private int Count;
    private String time;


}
