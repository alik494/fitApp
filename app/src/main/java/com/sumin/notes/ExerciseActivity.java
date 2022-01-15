package com.sumin.notes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;

public class ExerciseActivity extends AppCompatActivity {
    private RecyclerView recyclerViewExercise;
    private EditText editTextTitleEx;
    private EditText editTextDescriptionEx;
    private  final ArrayList<Exercise> exercises=new ArrayList<>();
    private ExerciseAdapter adapter;
    private ExerciseDBHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        editTextTitleEx=findViewById(R.id.editTextTitleEx);
        editTextDescriptionEx=findViewById(R.id.editTextDescriptionEx);
        recyclerViewExercise= (RecyclerView) findViewById(R.id.recyclerViewExercise);

        dbHelper=new ExerciseDBHelper(this);
        database = dbHelper.getWritableDatabase();
        getData();

        adapter= new ExerciseAdapter(exercises);//exercisesFromDB
        recyclerViewExercise.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,true)   );
        recyclerViewExercise.setAdapter(adapter);

        adapter.setOnExerciseClickListener(new ExerciseAdapter.OnExerciseClickListener() {
            @Override
            public void onExerciseClick(int position) {
                Toast.makeText(ExerciseActivity.this, getString(R.string.Swipe_to_delete), Toast.LENGTH_SHORT).show();
            }
        });

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                remove(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerViewExercise);

    }



    public void remove(int position){
        int id = exercises.get(position).getId();
        String where = ExerciseDBHelper.ExerciseEntry._ID+" = ?";
        String[] whereArgs=new   String[]{Integer.toString(id)};
        database.delete(ExerciseDBHelper.ExerciseEntry.TABLE_NAME,where,whereArgs);
               getData();
        adapter.notifyDataSetChanged();
    }
    public void onClickAddExerciseAct(View view) {
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        dbHelper=new ExerciseDBHelper(this);
        String title=editTextTitleEx.getText().toString().trim();
        String description=editTextDescriptionEx.getText().toString().trim();
        if (isFilled(title,description)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ExerciseDBHelper.ExerciseEntry.COLUMN_TITLE, title);
            contentValues.put(ExerciseDBHelper.ExerciseEntry.COLUMN_DESCRIPTION, description);
            database.insert(ExerciseDBHelper.ExerciseEntry.TABLE_NAME, null, contentValues);
            getData();
            adapter.notifyDataSetChanged();
            editTextDescriptionEx.setText("");
            editTextTitleEx.setText("");
        }else
            Toast.makeText(this, R.string.warning_fill_fileds, Toast.LENGTH_SHORT).show();

    }

    private boolean isFilled(String title,String description){
        return !title.isEmpty()&&!description.isEmpty();
    }

    private  void getData(){
        exercises.clear();
        Cursor cursor =database.query(ExerciseDBHelper.ExerciseEntry.TABLE_NAME,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            int id=cursor.getInt(cursor.getColumnIndex(ExerciseDBHelper.ExerciseEntry._ID));
            String title= cursor.getString(cursor.getColumnIndex(ExerciseDBHelper.ExerciseEntry.COLUMN_TITLE));
            String description= cursor.getString(cursor.getColumnIndex(ExerciseDBHelper.ExerciseEntry.COLUMN_DESCRIPTION));
            Exercise exercise= new Exercise(id,title,description);
            exercises.add(exercise);
        }
        cursor.close();
    }
}
