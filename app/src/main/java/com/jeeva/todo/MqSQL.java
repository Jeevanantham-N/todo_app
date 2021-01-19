package com.jeeva.todo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Todo.class},version = 1,exportSchema = false)
abstract class MySQL extends RoomDatabase {

    public abstract TodoDao todoDao();
    public  static MySQL instance;

    public  static  MySQL getInstance(Context context){
        if (instance == null){
            synchronized (MySQL.class){
                if (instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            MySQL.class,"todo_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }

}