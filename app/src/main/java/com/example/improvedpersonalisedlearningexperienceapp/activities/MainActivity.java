package com.example.improvedpersonalisedlearningexperienceapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.improvedpersonalisedlearningexperienceapp.R;
import com.example.improvedpersonalisedlearningexperienceapp.database.AppDatabase;
import com.example.improvedpersonalisedlearningexperienceapp.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    TextView textWelcome, taskHeader;
    ImageView profileButton;
    LinearLayout taskContainer;
    String username;
    List<String> completedTasks = new ArrayList<>();
    AppDatabase db;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textWelcome = findViewById(R.id.textWelcome);
        profileButton = findViewById(R.id.profileButton);
        taskContainer = findViewById(R.id.taskContainer);
        taskHeader = findViewById(R.id.taskHeader);

        db = AppDatabase.getInstance(this);
        prefs = getSharedPreferences("PLEAppPrefs", MODE_PRIVATE);
        username = getIntent().getStringExtra("username");

        if (username != null) {
            loadCompletedTasks();

            new Thread(() -> {
                User user = db.userDao().findByUsername(username);
                runOnUiThread(() -> {
                    if (user != null) {
                        textWelcome.setText("Hello, " + user.firstName + "!");
                        generateTasks(user);
                        profileButton.setOnClickListener(v -> {
                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                        });
                    }
                });
            }).start();
        }
    }

    private void loadCompletedTasks() {
        Set<String> saved = prefs.getStringSet("completedTasks_" + username, new HashSet<>());
        completedTasks = new ArrayList<>(saved);
    }

    private void saveCompletedTasks() {
        Set<String> saveSet = new HashSet<>(completedTasks);
        prefs.edit().putStringSet("completedTasks_" + username, saveSet).apply();
    }

    private String[] getDummyTasksForPlan(String plan) {
        int count;
        switch (plan != null ? plan : "") {
            case "Starter": count = 2; break;
            case "Intermediate": count = 3; break;
            case "Advanced": count = 4; break;
            case "Free":
            default: count = 1; break;
        }

        String[] tasks = new String[count];
        for (int i = 0; i < count; i++) {
            tasks[i] = String.valueOf(i + 1);
        }
        return tasks;
    }

    private void generateTasks(User user) {
        taskContainer.removeAllViews();
        String[] dummyTasks = getDummyTasksForPlan(user.plan);
        taskHeader.setText("🔔 You have " + dummyTasks.length + " task" + (dummyTasks.length > 1 ? "s" : "") + " due");

        for (String taskTitle : dummyTasks) {
            View taskCard = getLayoutInflater().inflate(R.layout.item_task_card, taskContainer, false);

            TextView titleView = taskCard.findViewById(R.id.textTaskTitle);
            TextView descView = taskCard.findViewById(R.id.textTaskDesc);
            Button startBtn = taskCard.findViewById(R.id.btnStartTask);
            ImageButton replayBtn = taskCard.findViewById(R.id.btnReplay);

            titleView.setText("✨Generated Task " + taskTitle);
            descView.setText("Test your knowledge!");

            if (completedTasks.contains(taskTitle)) {
                startBtn.setText("Completed");
                startBtn.setEnabled(false);
                startBtn.setBackgroundTintList(getResources().getColorStateList(android.R.color.darker_gray));
                replayBtn.setVisibility(View.VISIBLE);
            } else {
                startBtn.setEnabled(true);
                startBtn.setText("Start Task");
                startBtn.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_green_light));
                replayBtn.setVisibility(View.GONE);
            }

            startBtn.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, TaskActivity.class);
                intent.putExtra("interests", user.interests);
                intent.putExtra("username", user.username);
                intent.putExtra("taskName", taskTitle);
                startActivityForResult(intent, 100);
            });

            replayBtn.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, TaskActivity.class);
                intent.putExtra("interests", user.interests);
                intent.putExtra("username", user.username);
                intent.putExtra("taskName", taskTitle);
                startActivity(intent);
            });

            taskContainer.addView(taskCard);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 100 || requestCode == 101) && resultCode == RESULT_OK) {
            String completedTask = data.getStringExtra("completedTask");
            if (completedTask != null && !completedTasks.contains(completedTask)) {
                completedTasks.add(completedTask);
                saveCompletedTasks();
                refreshTasks();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (username != null) {
            loadCompletedTasks();
            refreshTasks();
        }
    }

    private void refreshTasks() {
        new Thread(() -> {
            User user = db.userDao().findByUsername(username);
            runOnUiThread(() -> generateTasks(user));
        }).start();
    }
}
