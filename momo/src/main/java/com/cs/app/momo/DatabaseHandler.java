package com.cs.app.momo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChinHsien on 4/21/14.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String database_Name = "MoMo";
    private static final int database_Version = 1;

    private static String momo_Month = null; //Table Name
    private static String momo_Year = null;
    //MomoYearMonth
    private static final String key_Id = "_ID";
    private static final String key_Date = "Date"; //Column date
    private static final String key_Type = "Type"; //Column type
    private static final String key_Kind = "Kind"; //Column kind
    private static final String key_Money = "Money"; //Column Money
    private static final String key_Comment = "Comment"; //Column Comment
    //sumTotal
    private static final String momo_Sum = "sumTotal"; //tableName
    private static final String key_Month = "Month"; //Column month
    private static final String key_Sum = "Sum"; //Column sum

    private int tsum = 0;
    private int ssum = 0;

    public DatabaseHandler(Context context, String month, String year) {
        super(context, database_Name, null, database_Version);
        this.momo_Month = month;
        this.momo_Year = year;
    }

    public DatabaseHandler(Context context) {
        super(context, database_Name, null, database_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createSql = "CREATE TABLE IF NOT EXISTS sumTotal(_ID INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL, Month Text, Sum Integer)";
        sqLiteDatabase.execSQL(createSql);
        /*createSql = "CREATE TABLE IF NOT EXISTS Momo" + momo_Year + momo_Month + "( _ID INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL, Date Text, Type Integer, Kind Integer, Money Integer, Comment Text)";
        sqLiteDatabase.execSQL(createSql);*/
        //ContentValues values = new ContentValues();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS sumTotal");
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Momo20145");
        onCreate(sqLiteDatabase);
    }

    public void create(String month, String year) {
            String createSql = "CREATE TABLE IF NOT EXISTS Momo" + year+month + "( _ID INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL, Date Integer, Type Integer, Kind Integer, Money Integer, Comment Text)";
            SQLiteDatabase db =this.getWritableDatabase();
            db.execSQL(createSql);
            db.close();
    }

    public void delete(int id, int year, int month, int money, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        String tableName = "Momo" + year + month;
        db.delete(tableName, "_ID=?", new String[] {String.valueOf(id)});
        db.close();

        db = this.getWritableDatabase();
        Cursor cursor = db.query (this.momo_Sum,   //table name
                new String[] {key_Month, key_Sum},  //column
                key_Month + "=?",  //
                new String[] {tableName}, //
                null,
                null,
                null,
                null);
        int sum = 0;
        if (cursor.moveToFirst()) {
            sum = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Sum")));
            if (type.equals("feed")) {
                sum -= money;
            } else {
                sum += money;
            }
        }
        db.close();
        db = this.getWritableDatabase();
        if (sum != 0) {
            ContentValues values = new ContentValues();
            values.put(key_Month, tableName);
            values.put(key_Sum, sum);

            db.update(this.momo_Sum,
                    values,
                    key_Month + "=?",
                    new String[]{tableName});
            //db.close();
        } else {
            db.delete(this.momo_Sum, key_Month + "=?", new String[] {tableName});
        }
        db.close();


    }

    public void update(Statistics statistics) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(key_Date, Integer.parseInt(statistics.getDate()));
        values.put(key_Type, statistics.getType());
        values.put(key_Kind, statistics.getKind());
        values.put(key_Money, statistics.getMoney());
        values.put(key_Comment, statistics.getComment());

        String tableName = statistics.getYear()+statistics.getMonth();

        db.update("Momo"+tableName,
                  values,
                  key_Id + "=?",
                  new String[] {statistics.getId()});
        db.close();
        //values.clear();

        db = this.getWritableDatabase();
        String selectSql = "SELECT  * FROM Momo" + tableName;
        Cursor cursor = db.rawQuery(selectSql, null);
        int sum  = 0;
        if (cursor.moveToFirst()) {
            do {
                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex("Type"))) == 0) {
                    sum += Integer.parseInt(cursor.getString(cursor.getColumnIndex("Money")));
                } else {

                    sum -= Integer.parseInt(cursor.getString(cursor.getColumnIndex("Money")));
                    ssum =  sum;
                }
            } while(cursor.moveToNext());
        }
        db.close();
        db = this.getWritableDatabase();
        values = new ContentValues();
        values.put(key_Month, "Momo" + tableName);
        values.put(key_Sum, sum);
        db.update(this.momo_Sum,
                values,
                key_Month + "=?",
                new String[]{"Momo" + tableName});
        db.close();
        //Toast.makeText(Cost.this, String.valueOf(sum), Toast.LENGTH_LONG).show();


    }

    /*public int getSum() {
        return this.tsum;
    }*/
    public int getsum() {
        return ssum;
    }

    public void insert(Statistics statistics) {
        String tableName = "Momo" + statistics.getYear() + statistics.getMonth();

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(key_Date, Integer.parseInt(statistics.getDate()));
        values.put(key_Type, statistics.getType());
        values.put(key_Kind, statistics.getKind());
        values.put(key_Money, statistics.getMoney());
        values.put(key_Comment, statistics.getComment());

        db.insert(tableName, null, values);
        db.close();

        int tag = 0;
        int sum = 0;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + this.momo_Sum, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(key_Month)).equals(tableName)) {
                    tag = 0;
                    if (statistics.getType() == 0) {
                        sum = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Sum"))) + statistics.getMoney();
                    } else {
                        sum = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Sum"))) - statistics.getMoney();
                    }
                } else
                {
                    tag = 1;
                    if (statistics.getType() == 0) { //0:feed, 1:cost
                        sum = statistics.getMoney();
                    } else {
                        sum = statistics.getMoney() * -1;
                    }
                }
            }while(cursor.moveToNext());
        } else {
            tag = 1;
            if (statistics.getType() == 0) { //0:feed, 1:cost
                sum = statistics.getMoney();
            } else {
                sum = statistics.getMoney() * -1;
            }
        }

        db.close();

        db = this.getWritableDatabase();
        values = new ContentValues();
        if (tag == 0) {
            values.put(key_Month, tableName);
            values.put(key_Sum, sum);

            db.update(this.momo_Sum,
                    values,
                    key_Month + "=?",
                    new String[]{tableName});
        } else {
            values.put(key_Month, tableName);
            values.put(key_Sum, sum);
            db.insert(this.momo_Sum, null, values);
        }
        db.close();
        tsum = sum;
        //Log.v("sum", String.valueOf(sum));
    }
    //Date Text, Type Integer, Kind Integer, Money Integer, Comment Text
    public List<Statistics> getMonthPerday(Statistics statistics) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Statistics> oneDay = new ArrayList<Statistics>();

        String tableName = statistics.getYear()+statistics.getMonth();

        String selectSql = "SELECT  * FROM Momo" + tableName + " ORDER BY " + key_Date + " ASC";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectSql, null);
        } catch(SQLiteException e) {

        }
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Statistics statist = new Statistics(cursor.getString(cursor.getColumnIndex("_ID")),
                            statistics.getMonth(),
                            cursor.getString(cursor.getColumnIndex("Date")),
                            Integer.parseInt(cursor.getString(cursor.getColumnIndex("Type"))),
                            Integer.parseInt(cursor.getString(cursor.getColumnIndex("Kind"))),
                            Integer.parseInt(cursor.getString(cursor.getColumnIndex("Money"))),
                            cursor.getString(cursor.getColumnIndex("Comment")),
                            statistics.getYear());
                    oneDay.add(statist);
                } while (cursor.moveToNext());
            }
        }
        db.close();
        return oneDay;
    }

    public Statistics getMonth (Statistics statistics) {
        SQLiteDatabase db = this.getReadableDatabase();

        String tableName = statistics.getYear() + statistics.getMonth();
        Cursor cursor = db.query (this.momo_Sum,   //table name
                                  new String[] {key_Month, key_Sum},  //column
                                  key_Month + "=?",  //
                                  new String[] {tableName}, //
                                  null,
                                  null,
                                  null,
                                  null);
            Statistics re = null;
        if (cursor.moveToFirst()) {
            re = new Statistics(cursor.getString(cursor.getColumnIndex("Month")),
                    statistics.getYear(),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex("Sum"))));
        } else {
            re = new Statistics(statistics.getMonth(), statistics.getYear(), 0);
        }
        db.close();
        return re;
    }

    public int getMonthCost(String year, String month) {
        String tableName = year + month;
        int cost = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String selectSql = "SELECT sum(" + key_Money + ") from Momo" + tableName + " Where " + key_Type + "=1";
        Cursor cursor = db.rawQuery(selectSql, null);
        if (cursor.moveToFirst()) {
            cost = cursor.getInt(0);
        }
        db.close();
        return cost;
    }

    public int getSum (String year, String month) {
        String tableName = year+month;
        int sum = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String selectSql = "SELECT sum(" + key_Sum + ") from " + momo_Sum;
        Cursor cursor = db.rawQuery(selectSql, null);
        if (cursor.moveToFirst()) {
            sum = cursor.getInt(0);
        }
        db.close();
        return sum;
    }


}
