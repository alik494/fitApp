package com.sumin.notes;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workouts")
public class Workout {
    @ColumnInfo(name = "_id")
    @PrimaryKey(autoGenerate = true)
    private int _id;
    private String title;
    private String weight;
    private String reps;
    private int set;
    private String description;
    private String day;

    public Workout(int id, String title, String weight, String reps, int set, String description,String day) {
        this._id = id;
        this.title = title;
        this.weight = weight;
        this.reps = reps;
        this.set = set;
        this.description = description;
        this.day=day;
    }

    public String getDay() {
        return day;
    }

    public int getId() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public String getWeight() {
        return weight;
    }


    public String getReps() {
        return reps;
    }

    public int getSet() {
        return set;
    }

    public String getDescription() {
        return description;
    }
}
