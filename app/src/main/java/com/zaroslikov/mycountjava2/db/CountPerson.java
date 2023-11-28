package com.zaroslikov.mycountjava2.db;

public class CountPerson {

    public CountPerson(String name, int count, String time, int step, int id) {
        this.name = name;
        this.count = count;
        this.time = time;
        this.step = step;
        this.id = id;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private int step;

    private int id;
    private String name;
    private int count;
    private String time;

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }




}
