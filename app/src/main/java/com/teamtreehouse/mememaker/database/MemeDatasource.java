package com.teamtreehouse.mememaker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.teamtreehouse.mememaker.models.Meme;
import com.teamtreehouse.mememaker.models.MemeAnnotation;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Evan Anger on 8/17/14.
 */
public class MemeDataSource {
    // exposes sql operation to model objects

    private Context mContext;
    private MemeSQLiteHelper mMemeSqlLiteHelper;

    public MemeDataSource(Context context) {
        mContext = context;
        mMemeSqlLiteHelper = new MemeSQLiteHelper(context);
    }

    private SQLiteDatabase open() {
        return mMemeSqlLiteHelper.getWritableDatabase();
    }

    private void close(SQLiteDatabase db) {
        db.close();
    }

    public  ArrayList<Meme> read() {
        return null;
    }

    public  ArrayList<Meme> readMemes() {
        SQLiteDatabase db = open();
        Cursor cursor = db.query(
                MemeSQLiteHelper.MEMES_TABLE,
                new String[]{MemeSQLiteHelper.COLUMN_MEME_NAME, BaseColumns._ID, MemeSQLiteHelper.COLUMN_MEME_ASSET},
                null, //selection
                null, //selection args
                null, //group by
                null, //having
                null); //order

        ArrayList<Meme> memes = new ArrayList<Meme>();
        if (cursor.moveToFirst()) { //move to first is elements exists
            do {
                Meme meme = new Meme(getIntFromColumnName(cursor, BaseColumns._ID),
                        getStringFromColumnName(cursor, MemeSQLiteHelper.COLUMN_MEME_ASSET),
                        getStringFromColumnName(cursor, MemeSQLiteHelper.COLUMN_MEME_NAME),
                        null);//annotations
                memes.add(meme);
            }while(cursor.moveToNext()); //loop while there are elements
        }
        cursor.close();
        db.close();
        return memes;
    }

    public void addMemeAnnotations(ArrayList<Meme> memes) {
        SQLiteDatabase db = open();
        for (Meme meme: memes) {
            ArrayList<MemeAnnotation> annotations = new ArrayList<MemeAnnotation>();
            Cursor cursor = db.rawQuery(
                    "SELECT * FROM " + MemeSQLiteHelper.ANNOTATIONS_TABLE +
                            " WHERE MEME_ID = "+ meme.getId(), null
            );

            if (cursor.moveToFirst()) {
                do {
                    MemeAnnotation annotation = new MemeAnnotation(
                            getIntFromColumnName(cursor, BaseColumns._ID),
                            getStringFromColumnName(cursor, MemeSQLiteHelper.COLUMN_ANNOTATION_COLOR),
                            getStringFromColumnName(cursor, MemeSQLiteHelper.COLUMN_ANNOTATION_TITLE),
                            getIntFromColumnName(cursor, MemeSQLiteHelper.COLUMN_ANNOTATION_X),
                            getIntFromColumnName(cursor, MemeSQLiteHelper.COLUMN_ANNOTATION_Y));
                    annotations.add(annotation);

                }while(cursor.moveToNext());
            }
            meme.setAnnotations(annotations);
            cursor.close();
        }
        db.close();
    }
    private int getIntFromColumnName(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return cursor.getInt(columnIndex);
    }

    private String getStringFromColumnName(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return cursor.getString(columnIndex);
    }

    public void create(Meme meme) {
        SQLiteDatabase db = open();
        db.beginTransaction();

        ContentValues memeValues = new ContentValues();
        memeValues.put(MemeSQLiteHelper.COLUMN_MEME_NAME, meme.getName() );
        memeValues.put(MemeSQLiteHelper.COLUMN_MEME_ASSET, meme.getAssetLocation());
        long memeID = db.insert(MemeSQLiteHelper.MEMES_TABLE, null, memeValues);

        for (MemeAnnotation annotation: meme.getAnnotations()) {
            ContentValues annotationValues = new ContentValues();
            annotationValues.put(MemeSQLiteHelper.COLUMN_ANNOTATION_COLOR, annotation.getColor());
            annotationValues.put(MemeSQLiteHelper.COLUMN_ANNOTATION_TITLE, annotation.getTitle());
            annotationValues.put(MemeSQLiteHelper.COLUMN_ANNOTATION_X, annotation.getLocationX());
            annotationValues.put(MemeSQLiteHelper.COLUMN_ANNOTATION_Y, annotation.getLocationY());
            annotationValues.put(MemeSQLiteHelper.COLUMN_FOREIGN_KEY_MEME, memeID);

            db.insert(MemeSQLiteHelper.ANNOTATIONS_TABLE, null, annotationValues);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        close(db);

    }
}
