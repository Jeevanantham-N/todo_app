package com.jeeva.todo;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jeeva.todo.databinding.RecyclerTodoCardBinding;

import java.util.List;

public class RecyclerTodoAdapter extends RecyclerView.Adapter<RecyclerTodoAdapter.ViewHolder> {
    List<Todo> todoList;
    ClickListener clickListener;

    public RecyclerTodoAdapter(List<Todo> todoList, ClickListener clickListener) {
        this.clickListener = clickListener;
        this.todoList = todoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerTodoCardBinding recyclerTodoCardBinding = RecyclerTodoCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        recyclerTodoCardBinding.todoCard.setMinimumHeight((parent.getMinimumHeight() / 3));
        return new ViewHolder(recyclerTodoCardBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (todoList.get(position).getCalendar() != null){
            holder.recyclerTodoCardBinding.dateTime.setText(todoList.get(position).getCalendar());
        } else {
            holder.recyclerTodoCardBinding.dateTime.setText("Note");
        }
        holder.recyclerTodoCardBinding.todoNote.setText(todoList.get(position).getNote());
        holder.recyclerTodoCardBinding.todoDel.setTag(todoList.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerTodoCardBinding recyclerTodoCardBinding;

        public ViewHolder(@NonNull RecyclerTodoCardBinding recyclerTodoCardBinding) {
            super(recyclerTodoCardBinding.getRoot());
            this.recyclerTodoCardBinding = recyclerTodoCardBinding;
            this.recyclerTodoCardBinding.todoDel.setOnClickListener(v ->
                    clickListener.onDelClick(getAdapterPosition(),todoList.get(getAdapterPosition())));
        }
    }
}