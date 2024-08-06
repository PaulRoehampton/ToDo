package com.bratuha.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> tasks;
    private DatabaseHelper dbHelper;
    private OnTaskClickListener listener;

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
    }

    public TaskAdapter(List<Task> tasks, DatabaseHelper dbHelper, OnTaskClickListener listener) {
        this.tasks = tasks;
        this.dbHelper = dbHelper;
        this.listener = listener;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxTask;
        TextView textViewTask;
        ImageButton buttonDelete;

        TaskViewHolder(View itemView) {
            super(itemView);
            checkBoxTask = itemView.findViewById(R.id.checkBoxTask);
            textViewTask = itemView.findViewById(R.id.textViewTask);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }

        void bind(final Task task) {
            checkBoxTask.setChecked(task.isCompleted());
            textViewTask.setText(task.getTitle());

            checkBoxTask.setOnCheckedChangeListener((buttonView, isChecked) -> {
                task.setCompleted(isChecked);
                dbHelper.updateTask(task);
            });

            buttonDelete.setOnClickListener(v -> {
                dbHelper.deleteTask(task);
                tasks.remove(task);
                notifyDataSetChanged();
            });

            itemView.setOnClickListener(v -> listener.onTaskClick(task));
        }
    }

    public void updateTasks(List<Task> newTasks) {
        tasks.clear();
        tasks.addAll(newTasks);
        notifyDataSetChanged();
    }
}
