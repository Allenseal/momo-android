package com.cs.app.momo;

/**
 * Created by ChinHsien on 4/22/14.
 */
public class Statistics {
    private String month = null;
    private String date = null;
    private int type = 0;
    private int kind = 0;
    private int sum = 0;
    private int money =  0;
    private String comment = null;
    private String year = null;
    private String id = null;

    public Statistics (String id, String month, String date, int type, int kind, int money,String comment, String year) {
        this.month = month;
        this.date = date;
        this.type = type;
        this.kind = kind;
        this.money = money;
        this.comment = comment;
        this.year = year;
        this.id = id;
    }

    public Statistics (String month, String date, int type, int kind, int money,String comment, String year) {
        this.month = month;
        this.date = date;
        this.type = type;
        this.kind = kind;
        this.money = money;
        this.comment = comment;
        this.year = year;
    }

    public Statistics (String month, String year, int sum) {
        this.month = month;
        this.sum = sum;
        this.year = year;
    }

    public String getId() {return this.id;}

    public String getMonth() {
        return this.month;
    }

    public String getDate() {
        return this.date;
    }

    public String getComment() {
        return this.comment;
    }

    public String getYear() {return this.year; }

    public int getType() {
        return this.type;
    }

    public int getKind() {
        return this.kind;
    }

    public int getMoney() {
        return this.money;
    }

    public int getSum() {return this.sum;}

    public String toString() {
        return super.toString() + this.month + "/" + this.date + "/" + this.type + "/" + this.kind + "/" + this.comment;
    }
}
