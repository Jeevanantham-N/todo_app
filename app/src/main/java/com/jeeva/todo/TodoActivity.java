package com.jeeva.todo;

import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.jeeva.todo.databinding.ActivityTodoBinding;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class TodoActivity extends AppCompatActivity implements ClickListener {
    ActivityTodoBinding activityTodoBinding;
    RecyclerTodoAdapter recyclerTodoAdapter;
    List<Todo> todoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        activityTodoBinding = ActivityTodoBinding.inflate(getLayoutInflater());
        setContentView(activityTodoBinding.getRoot());
        setTodoLists();
        recyclerTodoAdapter = new RecyclerTodoAdapter(todoList, this);
        activityTodoBinding.recyclerTodo.setAdapter(recyclerTodoAdapter);
        activityTodoBinding.recyclerTodo.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
        activityTodoBinding.addTodo.setOnClickListener(addOnClick);
    }

    private void setTodoLists() {
        todoList = TodoRepo.getAll(getApplicationContext());
        Collections.reverse(todoList);
    }

    View.OnClickListener addOnClick = v -> {
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(activityTodoBinding.addTodo, "addTodo");
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(TodoActivity.this, pairs);
        startActivity(new Intent(TodoActivity.this, AddTodo.class), activityOptions.toBundle());
    };

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public void onDelClick(int position, Todo todo) {
        TodoRepo.delete(this, todo);
        todoList.remove(position);
        recyclerTodoAdapter.notifyItemRemoved(position);
        cancelAlarm(todo.getId());
    }

    private void cancelAlarm(int id){
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

}