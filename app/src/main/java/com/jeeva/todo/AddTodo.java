package com.jeeva.todo;

import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.jeeva.todo.databinding.ActivityAddTodoBinding;

import java.util.Calendar;
import java.util.Objects;

public class AddTodo extends AppCompatActivity {
    Calendar calendar, dateCalender, timeCalender;
    ActivityAddTodoBinding activityAddTodoBinding;
    int YEAR, MONTH, DATE, HOUR, MINUTE;
    boolean is24format;
    String todoNote;
    Todo todo;
    boolean val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityAddTodoBinding = ActivityAddTodoBinding.inflate(getLayoutInflater());
        setContentView(activityAddTodoBinding.getRoot());
        settingDate();
        val = true;
        activityAddTodoBinding.setDate.setOnClickListener(dateOnClick);
        activityAddTodoBinding.setTime.setOnClickListener(timeOnClick);
        activityAddTodoBinding.addTodo.setOnClickListener(addOnClick);
        activityAddTodoBinding.setNote.setOnClickListener(setNoteClick);
        activityAddTodoBinding.todoNote.setHint("Add your Remainder");
        activityAddTodoBinding.todoNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                todoNote = s.toString();
            }
        });
    }

    View.OnClickListener setNoteClick = v -> {
        if (val){
            activityAddTodoBinding.setTime.setVisibility(View.INVISIBLE);
            activityAddTodoBinding.setDate.setVisibility(View.INVISIBLE);
            activityAddTodoBinding.todoNote.setHint("Add your Note");
            activityAddTodoBinding.setNote.setTextColor(Color.parseColor("#138880"));
            val = false;
            settingTime(true);
        } else {
            activityAddTodoBinding.setTime.setVisibility(View.VISIBLE);
            activityAddTodoBinding.setDate.setVisibility(View.VISIBLE);
            activityAddTodoBinding.todoNote.setHint("Add your Remainder");
            activityAddTodoBinding.setNote.setTextColor(Color.parseColor("#b41417"));
            val = true;
            settingTime(false);
        }
    };

    private void settingDate() {
        calendar = Calendar.getInstance();
        YEAR = calendar.get(Calendar.YEAR);
        MONTH = calendar.get(Calendar.MONTH);
        DATE = calendar.get(Calendar.DATE);
        dateCalender = calendar;
        activityAddTodoBinding.setDate.setText(DateFormat.format("MMM d", dateCalender));
    }

    private void settingTime(boolean boo) {
        if (!boo){
            timeCalender = null;
            return;
        }
        calendar = Calendar.getInstance();
        HOUR = calendar.get(Calendar.HOUR);
        MINUTE = calendar.get(Calendar.MINUTE);
        timeCalender = calendar;
    }

    View.OnClickListener dateOnClick = v -> {
        calendar = Calendar.getInstance();
        YEAR = calendar.get(Calendar.YEAR);
        MONTH = calendar.get(Calendar.MONTH);
        DATE = calendar.get(Calendar.DATE);

        new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int date) {
                        dateCalender = Calendar.getInstance();
                        dateCalender.set(Calendar.YEAR, year);
                        dateCalender.set(Calendar.MONTH, month);
                        dateCalender.set(Calendar.DATE, date);
                        activityAddTodoBinding.setDate.setText(DateFormat.format("MMM d", dateCalender));
                    }
                },
                YEAR,
                MONTH,
                DATE).show();
    };
    View.OnClickListener timeOnClick = v -> {
        calendar = Calendar.getInstance();
        HOUR = calendar.get(Calendar.HOUR);
        MINUTE = calendar.get(Calendar.MINUTE);
        is24format = DateFormat.is24HourFormat(this);

        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                timeCalender = Calendar.getInstance();
                timeCalender.set(Calendar.HOUR, hour);
                timeCalender.set(Calendar.MINUTE, minute);
                activityAddTodoBinding.setTime.setText(DateFormat.format("h:mm a", timeCalender));
            }
        }, HOUR, MINUTE, is24format).show();
    };
    View.OnClickListener addOnClick = v -> {
        if (todoNote == null || todoNote.equals("")) {
            activityAddTodoBinding.setTime.setTextColor(Color.parseColor("#138880"));
            return;
        }
        else if ((timeCalender == null ^ dateCalender == null)) {
                activityAddTodoBinding.setTime.setTextColor(Color.parseColor("#b41417"));
                return;
        }
        if (timeCalender != null){
            calendar = Calendar.getInstance();

            YEAR = dateCalender.get(Calendar.YEAR);
            MONTH = dateCalender.get(Calendar.MONTH);
            DATE = dateCalender.get(Calendar.DATE);

            HOUR = timeCalender.get(Calendar.HOUR);
            MINUTE = timeCalender.get(Calendar.MINUTE);

            calendar.set(Calendar.YEAR, YEAR);
            calendar.set(Calendar.MONTH, MONTH);
            calendar.set(Calendar.DATE, DATE);

            calendar.set(Calendar.HOUR, HOUR);
            calendar.set(Calendar.MINUTE, MINUTE);

            CharSequence format = DateFormat.format("MMM d, h:mm a", calendar);
            todo = new Todo(format.toString(), Objects.requireNonNull(activityAddTodoBinding.todoNote.getText()).toString());
        } else todo = new Todo(null, Objects.requireNonNull(activityAddTodoBinding.todoNote.getText()).toString());

        int primaryID = TodoRepo.insert(getApplicationContext(), todo);
        todo.setId(primaryID);

        if (timeCalender != null && dateCalender != null && val) {
            setAlarm(calendar,todo);
        }
        callActivity();
    };

    @Override
    public void onBackPressed() {
        callActivity();
    }

    private void callActivity() {
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(activityAddTodoBinding.todoCard, "addTodo");
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(AddTodo.this, pairs);
        startActivity(new Intent(AddTodo.this, TodoActivity.class), activityOptions.toBundle());
        finish();
    }

    private void setAlarm(Calendar calender,Todo todo){
        createNotificationChannel();
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        intent.putExtra("note",todo.getNote());
        intent.putExtra("id",String.valueOf(todo.getId()));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), todo.getId(), intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), pendingIntent);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", "Todo", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription( "todo remainder");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}