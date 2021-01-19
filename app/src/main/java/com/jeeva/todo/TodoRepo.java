package com.jeeva.todo;

import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TodoRepo {
    static List<Todo> todoList;
    static Todo todo;
    static ExecutorService thread = Executors.newFixedThreadPool(10);

    public static int insert(Context context,Todo todo){
        long id = 0;
        try {
            id = thread.submit(() -> MySQL.getInstance(context).todoDao().insert(todo)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return (int) id;
    }

    public static void delete(Context context,Todo todo){
        thread.execute(() -> MySQL.getInstance(context).todoDao().delete(todo));
    }

    public static List<Todo> getAll(Context context){
        try {
            todoList = thread.submit(()->MySQL.getInstance(context).todoDao().getAll()).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return todoList;
    }

    public static Todo findById(Context context,int id){
        try {
             todo = thread.submit(()->MySQL.getInstance(context).todoDao().findById(id)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return todo;
    }

}