package com.sumin.notes;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class ExerciseDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="exercise.db";
    private static final int DB_VERSION=1;

    public ExerciseDBHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ExerciseDBHelper.ExerciseEntry.CREATE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ExerciseDBHelper.ExerciseEntry.DROP_COMMAND);
        onCreate(db);
    }
    public static final class ExerciseEntry implements BaseColumns {
        public static final String TABLE_NAME ="exercise";
        public static final String COLUMN_TITLE ="title";
        public static final String COLUMN_DESCRIPTION ="description";

        public static final String TYPE_TEXT ="TEXT";
        public static final String TYPE_INTEGER ="integer";

        public static final String CREATE_COMMAND = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(" + _ID + " " + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT, " + COLUMN_TITLE +
                " " + TYPE_TEXT + ", " + COLUMN_DESCRIPTION + " " + TYPE_TEXT  + ")";

        public static final String DROP_COMMAND = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
