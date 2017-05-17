package com.fallingstar.yorimi.Helper.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Jiran on 2017-05-08.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME="YORIBI.db";
    private SQLiteDatabase mDB = null;
    private static int lastIdx = 1;

    public DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, VERSION);
        mDB = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE YORIBI (id INTEGER PRIMARY KEY NOT NULL, title TEXT, mainRuleTime INTEGER, mainRulePrice INTEGER, optRule BOOL, optRuleTime INTEGER, optRulePrice INTEGER, alarm INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void closeDB()
    {
        mDB.close();
    }

    private Cursor select(String query)
    {
        return mDB.rawQuery(query, null);
    }

    public void insert(String title, int mainT, int mainP, int optB, int optT, int optP, int alarm)
    {
        mDB = this.getWritableDatabase();
        mDB.execSQL("INSERT INTO YORIBI VALUES("+lastIdx+", \""+title+"\", "+mainT+", "+mainP+", "+optB+", "+optT+", "+optP+", "+alarm+");");
        lastIdx++;
        closeDB();
    }

    @SuppressWarnings("unused")
    public void update(String set, String where)
    {
        mDB = this.getWritableDatabase();
        mDB.execSQL("UPDATE YORIBI SET "+set+" WHERE "+where+";");
        closeDB();
    }

    @SuppressWarnings("unused")
    public void delete(String where)
    {
        mDB = this.getWritableDatabase();
        mDB.execSQL("DELETE FROM YORIBI WHERE "+where);
        closeDB();
    }

    public void delete(int idx)
    {
        mDB = this.getWritableDatabase();
        mDB.execSQL("DELETE FROM YORIBI WHERE id="+idx);
        closeDB();

        refreshDataFromId(idx);
    }

    private void refreshDataFromId(int idx)
    {
        mDB = this.getWritableDatabase();
        mDB.execSQL("BEGIN TRANSACTION;");
        mDB.execSQL("CREATE TABLE YORIBI_NEW (id INTEGER PRIMARY KEY NOT NULL, title TEXT, mainRuleTime INTEGER, mainRulePrice INTEGER, optRule BOOL, optRuleTime INTEGER, optRulePrice INTEGER, alarm INTEGER);");
        mDB.execSQL("INSERT INTO YORIBI_NEW(title, mainRuleTime, mainRulePrice, optRule, optRuleTime, optRulePrice, alarm) SELECT title, mainRuleTime, mainRulePrice, optRule, optRuleTime, optRulePrice, alarm FROM YORIBI");
        mDB.execSQL("DROP TABLE YORIBI");
        mDB.execSQL("ALTER TABLE YORIBI_NEW RENAME TO YORIBI");
        mDB.execSQL("COMMIT;");

        closeDB();
    }

    public int getCount()
    {
        int cnt;
        mDB = this.getReadableDatabase();

        Cursor cursor = select("SELECT DISTINCT * FROM YORIBI");
        cursor.moveToFirst();

        cnt = cursor.getCount();
        closeDB();

        lastIdx = cnt+1;

        return cnt;
    }

    @SuppressWarnings("unused")
    public String getResult()
    {
        mDB = this.getReadableDatabase();

        String result = "";

        Cursor cursor = select("SELECT DISTINCT * FROM YORIBI");
        cursor.moveToFirst();

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
        cursor.moveToFirst();

        result += cursor.getString(0);
        closeDB();

        return result;
    }

    public String getMainRuleTime(int order)
    {
        mDB = this.getReadableDatabase();

        String result = "";

        Cursor cursor = select("SELECT DISTINCT mainRuleTime FROM YORIBI WHERE id = "+order+";");
        cursor.moveToFirst();

        result += cursor.getInt(0);
        closeDB();

        return result;
    }

    public String getMainRulePrice(int order)
    {
        mDB = this.getReadableDatabase();

        String result = "";

        Cursor cursor = select("SELECT DISTINCT mainRulePrice FROM YORIBI WHERE id = "+order+";");
        cursor.moveToFirst();

        result += cursor.getInt(0);
        closeDB();

        return result;
    }

    public int getoptRuleBool(int order)
    {
        int isOpt;

        mDB = this.getReadableDatabase();

        Cursor cursor = select("SELECT DISTINCT optRule FROM YORIBI WHERE id = "+order+";");
        cursor.moveToFirst();

        isOpt = cursor.getInt(0);
        closeDB();

        return isOpt;
    }

    public String getoptRuleTime(int order)
    {
        mDB = this.getReadableDatabase();

        String result = "";

        Cursor cursor = select("SELECT DISTINCT optRuleTime FROM YORIBI WHERE id = "+order+";");
        cursor.moveToFirst();

        result += cursor.getInt(0);
        closeDB();

        return result;
    }

    public String getoptRulePrice(int order)
    {
        mDB = this.getReadableDatabase();

        String result = "";

        Cursor cursor = select("SELECT DISTINCT optRulePrice FROM YORIBI WHERE id = "+order+";");
        cursor.moveToFirst();

        result += cursor.getInt(0);
        closeDB();

        return result;
    }

    public String getPushAlarm(int order)
    {
        mDB = this.getReadableDatabase();

        String result = "";

        Cursor cursor = select("SELECT DISTINCT alarm FROM YORIBI WHERE id = "+order+";");
        cursor.moveToFirst();

        result += cursor.getInt(0);
        closeDB();

        return result;
    }
}
