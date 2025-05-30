package com.example.improvedpersonalisedlearningexperienceapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.improvedpersonalisedlearningexperienceapp.R;
import com.example.improvedpersonalisedlearningexperienceapp.database.AppDatabase;
import com.example.improvedpersonalisedlearningexperienceapp.model.User;

public class ProfileActivity extends AppCompatActivity {

    private TextView profileName, profileEmail, totalQuestions, correctAnswers, incorrectAnswers;
    private Button viewHistoryButton, shareProfileButton;
    private ImageView backButton;
    private TextView profileUserName, profilePlanView;
    private String name = "", email = "";
    private int total = 0, correct = 0, incorrect = 0;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Bind views
        profileName = findViewById(R.id.profileName);
        profileUserName = findViewById(R.id.profileUserName);
        profileEmail = findViewById(R.id.profileEmail);
        totalQuestions = findViewById(R.id.totalQuestions);
        correctAnswers = findViewById(R.id.correctAnswers);
        incorrectAnswers = findViewById(R.id.incorrectAnswers);
        viewHistoryButton = findViewById(R.id.viewHistoryButton);
        shareProfileButton = findViewById(R.id.shareProfileButton);
        backButton = findViewById(R.id.backButton);
        profilePlanView = findViewById(R.id.currentPlanText);

        username = getIntent().getStringExtra("username");

        reloadProfile();
        loadCurrentPlan();

        viewHistoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, HistoryActivity.class);
            intent.putExtra("username", username);
            startActivityForResult(intent, 1001); // For detecting history return
        });

        Button upgradeAccountButton = findViewById(R.id.upgradeAccountButton);
        upgradeAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, UpgradeActivity.class);
            intent.putExtra("username", username);
            startActivityForResult(intent, 1002); // For detecting upgrade return
        });

        shareProfileButton.setOnClickListener(v -> {
            String shareText = name + " has completed " + total + " questions and got " + correct + " correct!";
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Share Profile"));
        });

        TextView logoutLink = findViewById(R.id.textLogout);

        logoutLink.setOnClickListener(v -> {
            new AlertDialog.Builder(ProfileActivity.this)
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });



        backButton.setOnClickListener(v -> finish());
    }

    private void loadCurrentPlan() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            User user = db.userDao().findByUsername(username);
            runOnUiThread(() -> {
                if (user != null) {
                    String plan = (user.plan == null || user.plan.isEmpty()) ? "Free" : user.plan;
                    profilePlanView.setText("You are currently on the " + plan + " plan.");
                } else {
                    profilePlanView.setText("Unable to load current plan.");
                }
            });
        }).start();
    }

    private void reloadProfile() {
        if (username != null) {
            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                User user = db.userDao().findByUsername(username);

                if (user != null) {
                    name = user.firstName + " " + user.lastName;
                    email = user.email;
                    total = user.totalQuestions;
                    correct = user.correctAnswers;
                    incorrect = total - correct;

                    runOnUiThread(() -> {
                        profileName.setText(name);
                        profileUserName.setText(user.username);
                        profileEmail.setText(email);
                        totalQuestions.setText(String.valueOf(total));
                        correctAnswers.setText(String.valueOf(correct));
                        incorrectAnswers.setText(String.valueOf(incorrect));
                    });
                }
            }).start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1001 || requestCode == 1002) && resultCode == RESULT_OK) {
            reloadProfile();
            loadCurrentPlan();
        }
    }
}
