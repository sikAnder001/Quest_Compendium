package com.questcompendium.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.questcompendium.model.Notification;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";

    public static final String DATABASE_NAME = "Compendium.db";
    public static final String TABLE_NOTIFICATION = "tblNotification";
    public static final String NOTIFY_COLUMN_NOT_ID = "not_id";
    public static final String NOTIFY_COLUMN_TITLE = "title";
    public static final String NOTIFY_COLUMN_MSG = "message";
    public static final String NOTIFY_COLUMN_DATE = "date";
    public static final String NOTIFY_COLUMN_IMAGE= "image";
    public static final String NOTIFY_COLUMN_BTN_TXT = "btnTxt";
    public static final String NOTIFY_COLUMN_BTN_LNK = "btnLnk";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    //"(id integer primary key, MediaActionId text,FileName text,Filepath text, ContentType text,MediaPath text)"
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + TABLE_NOTIFICATION +
                        // "(id integer primary key, name text,phone text,email text, street text,place text)"
                        "(id integer primary key, " +
                        NOTIFY_COLUMN_TITLE + " text, " +
                        NOTIFY_COLUMN_MSG + " text , " +
                        NOTIFY_COLUMN_NOT_ID + " text , " +
                        NOTIFY_COLUMN_DATE + " text , " +
                        NOTIFY_COLUMN_IMAGE + " text , " +
                        NOTIFY_COLUMN_BTN_TXT + " text , " +
                        NOTIFY_COLUMN_BTN_LNK + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
        onCreate(db);
    }

    public boolean insertNotification(String notId, String title, String message, String date, String fsImage, String btnTxt, String btnLink) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTIFY_COLUMN_NOT_ID, notId);
        contentValues.put(NOTIFY_COLUMN_TITLE, title);
        contentValues.put(NOTIFY_COLUMN_MSG, message);
        contentValues.put(NOTIFY_COLUMN_DATE, date);
        contentValues.put(NOTIFY_COLUMN_IMAGE, fsImage);
        contentValues.put(NOTIFY_COLUMN_BTN_TXT, btnTxt);
        contentValues.put(NOTIFY_COLUMN_BTN_LNK, btnLink);
        db.insert(TABLE_NOTIFICATION, null, contentValues);
        return true;
    }

    public ArrayList<Notification> getAllNotificationList() {
        ArrayList<Notification> loNotificationList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NOTIFICATION + " order by id Desc", null);
        res.moveToFirst();
        Log.d(TAG, "NOTIFICATION COUNT:: " + res.getCount());
        while (!res.isAfterLast()) {
            Notification notification = new Notification();
            notification.setNotId(res.getString(res.getColumnIndex(NOTIFY_COLUMN_NOT_ID)));
            notification.setTitle(res.getString(res.getColumnIndex(NOTIFY_COLUMN_TITLE)));
            notification.setDesc(res.getString(res.getColumnIndex(NOTIFY_COLUMN_MSG)));
            notification.setDateTime(res.getString(res.getColumnIndex(NOTIFY_COLUMN_DATE)));
            notification.setImageUrl(res.getString(res.getColumnIndex(NOTIFY_COLUMN_IMAGE)));
            notification.setBtnTxt(res.getString(res.getColumnIndex(NOTIFY_COLUMN_BTN_TXT)));
            notification.setBtnLink(res.getString(res.getColumnIndex(NOTIFY_COLUMN_BTN_LNK)));
            loNotificationList.add(notification);
            res.moveToNext();
        }
        return loNotificationList;
    }

    public Integer deleteNotification(String fsDate, String fsTitle) {
        Log.i(TAG, "Deleted Notification :: " + fsTitle);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NOTIFICATION,
                NOTIFY_COLUMN_TITLE + " = ? AND " +
                        NOTIFY_COLUMN_DATE + " = ?",
                new String[]{fsTitle, fsDate});

    }

    public boolean deleteAllNotifications() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTIFICATION, null, null);
        return true;
    }

}