package com.sumin.notes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.ArrayList;

public class AddWorkoutActivity extends AppCompatActivity {
    private Spinner spinnerExercise;
    private EditText editTextWeight;
    private EditText editTextReps;
    private EditText editTextSet;
    private EditText editTextDescription;
    private TextView textViewTimer;
    private TextView textViewRed;
    private ExerciseAdapter adapter;
    private ExerciseDBHelper databaseExpHelper;
    private SQLiteDatabase databaseExp;
    private  final ArrayList<Exercise> exercises=new ArrayList<>();
    private WorkoutDBHelper dbHelper;
    private SQLiteDatabase database;

    private String sDayString;
    private int idWorkout;
    private int seconds=60;

    private boolean isUpdate;

    @Override
    protected void onResume() {
        super.onResume();
        updateExercise();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);
        

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        editTextSet = findViewById(R.id.editTextSet);
        editTextReps = findViewById(R.id.editTextReps);
        editTextWeight = findViewById(R.id.editTextWeight);
        spinnerExercise = findViewById(R.id.spinnerExercise);
        editTextDescription = findViewById(R.id.editTextDescription);
        textViewTimer=findViewById(R.id.textViewTimer);
        textViewRed=findViewById(R.id.textViewExercise);
        editTextSet.setText(String.format(""+exercises.size()));

        Intent intent= getIntent();
        sDayString = intent.getStringExtra("idDay");
        int sizeW=intent.getIntExtra("sizeW",0);
        editTextSet.setText(String.format(""+sizeW));
        initWorkout();

        if (intent.getBooleanExtra("isUpdate", false)) {
            isUpdate=true;
            runTimer();
            idWorkout = intent.getIntExtra("idWorkout", 0);
            updateWorkout();
        }
    }

    public void onClickAddNewExercise(View view) {
        Intent intent = new Intent(this, ExerciseActivity.class);
        startActivity(intent);
    }
    private void runTimer(){
        final Handler handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                textViewTimer.setText(String.format(""+seconds));
                if (isUpdate){
                    if (seconds>0) {
                        seconds--;
                    }
                    else {
                        textViewTimer.setText("Время подхода!!!");
                        textViewTimer.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                        textViewRed.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                    }
                }
                handler.postDelayed(this,1000);
            }
        });
    }

    public void onClickAddExercise(View view) {

        String reps = (editTextReps.getText().toString());
        String setSt = (editTextSet.getText().toString());
        String weight = (editTextWeight.getText().toString());
        String exercise = spinnerExercise.getSelectedItem().toString();
        String description = editTextDescription.getText().toString().trim();

        if (isFilled(exercise,weight,reps,setSt)){

            int set=Integer.parseInt(setSt);

            ContentValues contentValues=new ContentValues();
            contentValues.put(WorkoutDBHelper.WorkoutsEntry.COLUMN_TITLE,exercise);
            contentValues.put(WorkoutDBHelper.WorkoutsEntry.COLUMN_WEIGHT,weight);
            contentValues.put(WorkoutDBHelper.WorkoutsEntry.COLUMN_REPS,reps);
            contentValues.put(WorkoutDBHelper.WorkoutsEntry.COLUMN_SET,set);
            contentValues.put(WorkoutDBHelper.WorkoutsEntry.COLUMN_DESCRIPTION,description);
            contentValues.put(WorkoutDBHelper.WorkoutsEntry.COLUMN_DAY,sDayString);
            if (isUpdate) {
               int id=idWorkout;
               String where= WorkoutDBHelper.WorkoutsEntry._ID+" = ?";
               String[] whereArgs=new String[]{Integer.toString(id)};
               database.update(WorkoutDBHelper.WorkoutsEntry.TABLE_NAME,contentValues,where,whereArgs);
            }
            else{
            /*   String whereSet= WorkoutDBHelper.WorkoutsEntry.COLUMN_SET+" = ?";
                String[] whereArgsSet=new String[]{(setSt)};
                database.delete(WorkoutDBHelper.WorkoutsEntry.TABLE_NAME,whereSet,whereArgsSet);*/
            database.insert(WorkoutDBHelper.WorkoutsEntry.TABLE_NAME,null,contentValues);}
            finish();
        }else {
            Toast.makeText(this, R.string.warning_fill_fileds, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateExercise()
    {
        Cursor cursorS2 = databaseExp.query(ExerciseDBHelper.ExerciseEntry.TABLE_NAME,null,null,null,null,null, null);
        ArrayList<String> arrayList3 = new ArrayList<>();
        while (cursorS2.moveToNext()) {
            String title = cursorS2.getString(cursorS2.getColumnIndex(ExerciseDBHelper.ExerciseEntry.COLUMN_TITLE));
            arrayList3.add(title);
        }
        cursorS2.close();
        if (arrayList3.isEmpty()) {
            exercises.add(new Exercise(0,"Жим ган. над собой", "Подьем гантелей над собой большим весом к себе"));
            exercises.add(new Exercise(1,"Тяга ган. в наклоне", "Подьем гантелей в наклоне с прямой спиной"));
            exercises.add(new Exercise(2,"Махи гантелями", "В полу наклоне с прямой спиной стараться поднять гантеля прям перед собой"));
            exercises.add(new Exercise(3,"Под. ган. на Биц.", "Медленно сгибайте руки в локтях. Когда предплечья будут параллельны полу, начинайте разворачивать кисти наружу, то есть кверху запястьями.Поднимите гантели до того уровня, когда запястья практически коснутся плеч"));
            exercises.add(new Exercise(4,"Тяга шт. в наклоне", "Выставить руки на ширене плеч и не прогибая спину поднимать штангу работая средними мышцами спины"));
            exercises.add(new Exercise(5,"Подт. на Биц.", "выставить руки ладоньями к себе и медлено подтягиваться"));

            exercises.add(new Exercise(6,"Жим ган. лежа", "Желательно на скамье с углом от 20 градусов"));
            exercises.add(new Exercise(7,"Подт. широк. хватом", "руки максимально широкои и стараться работать только средней частью"));
            exercises.add(new Exercise(8,"Жим ган. из-за головы", "руки максимально широкои и стараться работать только средней частью"));
            exercises.add(new Exercise(9,"Жим Ган. лежа с супин.", "Обычный жим но с супенацией гантели (поворотом по горизонтальной оси"));
            exercises.add(new Exercise(10,"Подьем на носки", "Стараемся использовать только икроножные мышцы"));


            for (Exercise exercise:exercises){
                ContentValues contentValues= new ContentValues();
                contentValues.put(ExerciseDBHelper.ExerciseEntry.COLUMN_TITLE,exercise.getTitle());
                contentValues.put(ExerciseDBHelper.ExerciseEntry.COLUMN_DESCRIPTION,exercise.getDescription());
                databaseExp.insert(ExerciseDBHelper.ExerciseEntry.TABLE_NAME,null,contentValues);
            }
        }

        Cursor cursorS = databaseExp.query(ExerciseDBHelper.ExerciseEntry.TABLE_NAME,null,null,null,null,null, ExerciseDBHelper.ExerciseEntry.COLUMN_TITLE);
        ArrayList<String> arrayList2 = new ArrayList<>();
        while (cursorS.moveToNext()) {
            String title = cursorS.getString(cursorS.getColumnIndex(ExerciseDBHelper.ExerciseEntry.COLUMN_TITLE));
            arrayList2.add(title);
        }
        cursorS.close();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(!isUpdate){
        spinnerExercise.setAdapter(adapter2);}
    }

    private boolean isFilled(String exercise, String weight,String reps,String set){
        return !exercise.isEmpty()&&  !weight.isEmpty() && !reps.isEmpty() &&!set.isEmpty();
    }

    private  void initWorkout()
    {
        databaseExpHelper=new ExerciseDBHelper(this);
        databaseExp = databaseExpHelper.getWritableDatabase();
        dbHelper = new WorkoutDBHelper(this);
        database = dbHelper.getWritableDatabase();
        updateExercise();
    }

    private void updateWorkout(){
        String idSelect = "_id == '" + idWorkout + "'";
        Cursor cursor = database.query(WorkoutDBHelper.WorkoutsEntry.TABLE_NAME,null, idSelect, null, null,null, null,null);
        while (cursor.moveToNext()) {
            int set = cursor.getInt(cursor.getColumnIndex(WorkoutDBHelper.WorkoutsEntry.COLUMN_SET));
            String reps = cursor.getString(cursor.getColumnIndex(WorkoutDBHelper.WorkoutsEntry.COLUMN_REPS));
            String title = cursor.getString(cursor.getColumnIndex(WorkoutDBHelper.WorkoutsEntry.COLUMN_TITLE));
            String weight = cursor.getString(cursor.getColumnIndex(WorkoutDBHelper.WorkoutsEntry.COLUMN_WEIGHT));
            String description = cursor.getString(cursor.getColumnIndex(WorkoutDBHelper.WorkoutsEntry.COLUMN_DESCRIPTION));
            SpinnerAdapter spinnerAdapter = spinnerExercise.getAdapter();
            int count = spinnerAdapter.getCount();
            for (int i = 0; i < count; i++) {
                if (spinnerAdapter.getItem(i).toString().equals(title)) {
                    spinnerExercise.setSelection(i);
                    break;
                }
            }
            editTextWeight.setText(weight);
            editTextReps.setText(reps);
            editTextSet.setText(String.format(""+set));
            editTextDescription.setText(description);
            break;
        }
    }

}