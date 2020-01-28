package com.sumin.notes;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class WorkoutDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME  = "workouts.db";
    private static final int DB_VERSION=1;

    //public WorkoutDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    public WorkoutDBHelper(Context context) {
        super(context,  DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WorkoutDBHelper.WorkoutsEntry.CREATE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(WorkoutDBHelper.WorkoutsEntry.DROP_COMMAND);
        onCreate(db);
    }

    public static final class WorkoutsEntry implements BaseColumns {
        public static final String TABLE_NAME="workouts" ;
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_REPS = "reps";
        public static final String COLUMN_SET = "setS";
        public static final String COLUMN_DESCRIPTION = "description";

        public static final String COLUMN_DAY= "day";

        public static final String TYPE_TEXT = "TEXT";
        public static final String TYPE_INTEGER = "INTEGER";


        public static final String CREATE_COMMAND = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(" + _ID + " " + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " " + TYPE_TEXT + ", " +
                COLUMN_WEIGHT +" " + TYPE_TEXT + ", " +
                COLUMN_REPS +" " + TYPE_TEXT + ", " +
                COLUMN_SET +" " + TYPE_INTEGER + ", " +
                COLUMN_DESCRIPTION + " " + TYPE_TEXT +", " +
                COLUMN_DAY+ " "+ TYPE_TEXT + ")";

        public static final String DROP_COMMAND = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
