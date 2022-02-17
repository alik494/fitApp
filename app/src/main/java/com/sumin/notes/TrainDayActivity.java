package com.sumin.notes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TrainDayActivity extends AppCompatActivity {
    private RecyclerView recyclerViewWorkouts;
    private final ArrayList<Workout> workouts = new ArrayList<>();
    private WorkoutAdapter workoutAdapter;
    private WorkoutDBHelper dbHelper;
    private SQLiteDatabase database;
    private String sDayString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_day);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Intent intent = getIntent();
        if (intent.hasExtra("idDay")) {
            sDayString = intent.getStringExtra("idDay");
        }
        recyclerViewWorkouts = (RecyclerView) findViewById(R.id.recyclerViewWorkouts);
        dbHelper = new WorkoutDBHelper(this);
        database = dbHelper.getWritableDatabase();
        getData();
        workoutAdapter = new WorkoutAdapter(workouts);//workoutsFromDB
        recyclerViewWorkouts.setLayoutManager(new GridLayoutManager(this, 5, GridLayoutManager.HORIZONTAL, false));
        recyclerViewWorkouts.setAdapter(workoutAdapter);
        //устанавливаем разметку по горизонтале или как
        workoutAdapter.setOnWorkoutClickListener(new WorkoutAdapter.OnWorkoutClickListener() {
            @Override
            public void onWorkoutClick(int position) {
                onClickUpdateWorkout(position);
            }

            @Override
            public void onLongWorkoutClick(int position) {
                remove(position);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.DOWN | ItemTouchHelper.UP) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerViewWorkouts);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        workoutAdapter.notifyDataSetChanged();
    }

    private void remove(int position) {
        int id = workouts.get(position).getId();
        String where = WorkoutDBHelper.WorkoutsEntry._ID + " = ?";
        String[] whereArgs = new String[]{Integer.toString(id)};
        database.delete(WorkoutDBHelper.WorkoutsEntry.TABLE_NAME, where, whereArgs);
        //workouts.remove(position);  удаление
        getData();
        workoutAdapter.notifyDataSetChanged();
    }

    public void onClickAddWorkout(View view) {
        Intent intent = new Intent(this, AddWorkoutActivity.class);
        intent.putExtra("idDay", sDayString);
        intent.putExtra("isUpdate", false);
        intent.putExtra("sizeW", workouts.size() + 1);
        startActivity(intent);
    }

    private void getData() {
        workouts.clear();
        String titleQuery = "day == '" + sDayString + "'";
        Cursor cursor = database.query(WorkoutDBHelper.WorkoutsEntry.TABLE_NAME, null, titleQuery, null, null, null, WorkoutDBHelper.WorkoutsEntry.COLUMN_SET, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(WorkoutDBHelper.WorkoutsEntry._ID));
            String title = cursor.getString(cursor.getColumnIndex(WorkoutDBHelper.WorkoutsEntry.COLUMN_TITLE));
            String weight = cursor.getString(cursor.getColumnIndex(WorkoutDBHelper.WorkoutsEntry.COLUMN_WEIGHT));
            String reps = cursor.getString(cursor.getColumnIndex(WorkoutDBHelper.WorkoutsEntry.COLUMN_REPS));
            int set = cursor.getInt(cursor.getColumnIndex(WorkoutDBHelper.WorkoutsEntry.COLUMN_SET));
            String description = cursor.getString(cursor.getColumnIndex(WorkoutDBHelper.WorkoutsEntry.COLUMN_DESCRIPTION));
            boolean odd = (cursor.getInt(cursor.getColumnIndex(WorkoutDBHelper.WorkoutsEntry.COLUMN_ODD))) == 1;
            Workout workout = new Workout(id, title, weight, reps, set, description, odd, null);
            workouts.add(workout);
        }
        cursor.close();
    }


    public void onClickUpdateWorkout(int position) {
        Intent intent = new Intent(this, AddWorkoutActivity.class);
        intent.putExtra("idDay", sDayString);
        intent.putExtra("isUpdate", true);
        intent.putExtra("idWorkout", position);
        startActivity(intent);
    }
}
