package com.jeeva.todo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TodoDao {
    @Insert
    long insert(Todo todo);

    @Delete
    void delete(Todo todo);

    @Query("SELECT * FROM TODO")
    List<Todo> getAll();

    @Query("SELECT * FROM TODO WHERE id LIKE :id LIMIT 1")
    Todo findById(int id);
}