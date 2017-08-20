package com.sampletexteditor.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.sampletexteditor.model.TextEditorModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ranjith on 29/4/17.
 */

public class TextEditorDB extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "textEditorManager";

    // Contacts table name
    private static final String TABLE_TEXTS = "texteditor";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "texts";


    public TextEditorDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setWriteAheadLoggingEnabled(true);
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            db.enableWriteAheadLogging();
        } else {


            Log.d("DATABASE", "WAL is not supported on API levels below 11.");

        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TEXTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEXTS);

        // Create tables again
        onCreate(db);
    }


    public void addTexts(TextEditorModel texts) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, texts.get_texts());

        // Inserting Row
        db.insert(TABLE_TEXTS, null, values);
        db.close(); // Closing database connection
    }

    public List<TextEditorModel> getAllTexts() {
        List<TextEditorModel> contactList = new ArrayList<TextEditorModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TEXTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TextEditorModel contact = new TextEditorModel();
                contact.set_id(Integer.parseInt(cursor.getString(0)));
                contact.set_texts(cursor.getString(1));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }


    // Getting events Count
    public int getTextsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TEXTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }


    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_TEXTS);
        db.close();
    }
}
