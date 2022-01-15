package com.sumin.notes;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface NotesDao {
    @Query("SELECT * FROM notes ORDER BY dayOfWeek ASC")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM notes WHERE title == :title")
    LiveData<List<Note>> getByTitle(String title);

    @Query("SELECT * FROM notes WHERE id == :noteId")
    LiveData<List<Note>> getById(int noteId);

    @Insert
    void insertNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("DELETE FROM notes")
    void deleteAllNotes();
}
