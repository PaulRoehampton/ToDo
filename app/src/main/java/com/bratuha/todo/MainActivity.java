package com.bratuha.todo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.app.AlertDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskClickListener {

    private RecyclerView recyclerViewTasks;
    private TaskAdapter taskAdapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        List<Task> tasks = dbHelper.getAllTasks();
        taskAdapter = new TaskAdapter(tasks, dbHelper, this);
        recyclerViewTasks.setAdapter(taskAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> showAddTaskDialog());
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Task");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null);
        final EditText input = viewInflated.findViewById(R.id.editTextTask);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            String taskTitle = input.getText().toString().trim();
            if (!taskTitle.isEmpty()) {
                Task newTask = new Task(taskTitle);
                long id = dbHelper.addTask(newTask);
                newTask.setId(id);
                taskAdapter.updateTasks(dbHelper.getAllTasks());
            }
        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public void onTaskClick(Task task) {
        showEditDialog(task);
    }

    private void showEditDialog(Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null);
        final EditText input = viewInflated.findViewById(R.id.editTextTask);
        input.setText(task.getTitle());
        builder.setView(viewInflated);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newTitle = input.getText().toString().trim();
            if (!newTitle.isEmpty()) {
                task.setTitle(newTitle);
                dbHelper.updateTask(task);
                taskAdapter.updateTasks(dbHelper.getAllTasks());
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}