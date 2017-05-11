package com.fallingstar.yorimi.Helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jiran on 2017-05-08.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME="YORIMI.db";
    SQLiteDatabase mDB = null;

    public DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, VERSION);
        mDB = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE YORIBI (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, mainRuleTime INTEGER, mainRulePrice INTEGER, optRule BOOL, optRuleTime INTEGER, optRulePrice INTEGER, alarm INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void closeDB()
    {
        mDB.close();
    }

    public Cursor select(String query)
    {
        return mDB.rawQuery(query, null);
    }

    public void insert(String title, int mainT, int mainP, int optB, int optT, int optP, int alarm)
    {
        mDB = this.getWritableDatabase();
        mDB.execSQL("INSERT INTO YORIBI VALUES(null, \""+title+"\", "+mainT+", "+mainP+", "+optB+", "+optT+", "+optP+", "+alarm+");");
        closeDB();
    }

    public void update(String set, String where)
    {
        mDB = this.getWritableDatabase();
        mDB.execSQL("UPDATE YORIBI SET "+set+" WHERE "+where+";");
        closeDB();
    }

    public void delete(String where)
    {
        mDB = this.getWritableDatabase();
        mDB.execSQL("DELETE FROM YORIBI WHERE "+where);
        closeDB();
    }

    public int getCount()
    {
        mDB = this.getReadableDatabase();

        Cursor cursor = select("SELECT DISTINCT * FROM YORIBI");

        closeDB();

        return cursor.getCount();
    }

    public String getResult()
    {
        mDB = this.getReadableDatabase();

        String result = "";

        Cursor cursor = select("SELECT DISTINCT * FROM YORIBI");

        while(cursor.moveToNext())
        {
            result+= cursor.getString(0) + " " +
                    cursor.getString(1) + " " +
                    cursor.getInt(2) + " " +
                    cursor.getInt(3) + " " +
                    cursor.getInt(4) + " " +
                    cursor.getInt(5) + " " +
                    cursor.getInt(6) + "\n";
        }

        closeDB();

        return result;
    }

    public String getTitle(int order)
    {
        mDB = this.getReadableDatabase();

        String result = "";

        Cursor cursor = select("SELECT DISTINCT title FROM YORIBI WHERE id = "+order+";");

        result += cursor.getString(1);

        closeDB();

        return result;
    }

    public String getMainRuleTime(int order)
    {
        mDB = this.getReadableDatabase();

        String result = "";

        Cursor cursor = select("SELECT DISTINCT mainRuleTime FROM YORIBI WHERE id = "+order+";");

        result += cursor.getInt(2);

        closeDB();

        return result;
    }

    public String getMainRulePrice(int order)
    {
        mDB = this.getReadableDatabase();

        String result = "";

        Cursor cursor = select("SELECT DISTINCT mainRulePrice FROM YORIBI WHERE id = "+order+";");

        result += cursor.getInt(3);

        closeDB();

        return result;
    }

    public int getoptRuleBool(int order)
    {
        mDB = this.getReadableDatabase();

        Cursor cursor = select("SELECT DISTINCT optRule FROM YORIBI WHERE id = "+order+";");

        closeDB();

        return cursor.getInt(4);
    }

    public String getoptRuleTime(int order)
    {
        mDB = this.getReadableDatabase();

        String result = "";

        Cursor cursor = select("SELECT DISTINCT optRuleTime FROM YORIBI WHERE id = "+order+";");

        result += cursor.getInt(5);

        closeDB();

        return result;
    }

    public String getoptRulePrice(int order)
    {
        mDB = this.getReadableDatabase();

        String result = "";

        Cursor cursor = select("SELECT DISTINCT optRulePrice FROM YORIBI WHERE id = "+order+";");

        result += cursor.getInt(6);

        closeDB();

        return result;
    }

    public String getPushAlarm(int order)
    {
        mDB = this.getReadableDatabase();

        String result = "";

        Cursor cursor = select("SELECT DISTINCT alarm FROM YORIBI WHERE id = "+order+";");

        result += cursor.getInt(7);

        closeDB();

        return result;
    }
}