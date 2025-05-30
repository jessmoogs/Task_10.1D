package com.example.improvedpersonalisedlearningexperienceapp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.improvedpersonalisedlearningexperienceapp.R;
import com.example.improvedpersonalisedlearningexperienceapp.database.AppDatabase;
import com.example.improvedpersonalisedlearningexperienceapp.model.HistoryEntry;
import com.example.improvedpersonalisedlearningexperienceapp.model.User;

import java.text.SimpleDateFormat;
import java.util.*;

public class HistoryActivity extends AppCompatActivity {

    private LinearLayout historyContainer;
    private String username;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyContainer = findViewById(R.id.historyContainer);
        username = getIntent().getStringExtra("username");
        db = AppDatabase.getInstance(this);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        loadHistory();

        Button deleteHistoryButton = findViewById(R.id.deleteHistoryButton);
        deleteHistoryButton.setOnClickListener(v -> promptForPasswordAndDelete());
    }

    private void loadHistory() {
        new Thread(() -> {
            List<HistoryEntry> allEntries = db.historyDao().getHistoryForUser(username);

            Map<Long, List<HistoryEntry>> grouped = new LinkedHashMap<>();
            for (HistoryEntry entry : allEntries) {
                grouped.computeIfAbsent(entry.timestamp, k -> new ArrayList<>()).add(entry);
            }

            List<Map.Entry<Long, List<HistoryEntry>>> groupedList = new ArrayList<>(grouped.entrySet());

            runOnUiThread(() -> {
                historyContainer.removeAllViews();

                if (groupedList.isEmpty()) {
                    TextView noHistory = new TextView(this);
                    noHistory.setText("No quiz history");
                    noHistory.setTextSize(18f);
                    noHistory.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    noHistory.setPadding(16, 64, 16, 0);
                    historyContainer.addView(noHistory);
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy • hh:mm a", Locale.getDefault());

                // --- Most Recent Quiz ---
                TextView recentLabel = new TextView(this);
                recentLabel.setText("Most Recent Quiz");
                recentLabel.setTextSize(22f);
                recentLabel.setTextColor(getResources().getColor(android.R.color.black));
                recentLabel.setPadding(0, 12, 0, 12);
                historyContainer.addView(recentLabel);

                historyContainer.addView(createQuizBlock(sdf, groupedList.get(0)));

                // --- Previous Quizzes ---
                if (groupedList.size() > 1) {
                    TextView prevLabel = new TextView(this);
                    prevLabel.setText("Previous Quizzes");
                    prevLabel.setTextSize(22f);
                    prevLabel.setTextColor(getResources().getColor(android.R.color.black));
                    prevLabel.setPadding(0, 24, 0, 12);
                    historyContainer.addView(prevLabel);

                    for (int i = 1; i < groupedList.size(); i++) {
                        LinearLayout quizBlock = createQuizBlock(sdf, groupedList.get(i));
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 0, 0, 24); // Add 12dp bottom margin between entries
                        quizBlock.setLayoutParams(params);
                        historyContainer.addView(quizBlock);

                    }
                }
            });
        }).start();
    }

    private LinearLayout createQuizBlock(SimpleDateFormat sdf, Map.Entry<Long, List<HistoryEntry>> attempt) {
        LinearLayout sectionLayout = new LinearLayout(this);
        sectionLayout.setOrientation(LinearLayout.VERTICAL);
        sectionLayout.setPadding(0, 0, 0, 24);
        sectionLayout.setBackground(getDrawable(R.drawable.rounded_card));

        // Header
        LinearLayout headerLayout = new LinearLayout(this);
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);
        headerLayout.setPadding(32, 24, 32, 24);
        headerLayout.setGravity(Gravity.CENTER_VERTICAL);

        TextView dateLabel = new TextView(this);
        dateLabel.setText(sdf.format(new Date(attempt.getKey())));
        dateLabel.setTextSize(16f);
        dateLabel.setTextColor(getResources().getColor(android.R.color.white));
        dateLabel.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        ImageView dropdownIcon = new ImageView(this);
        dropdownIcon.setImageResource(android.R.drawable.arrow_down_float);
        headerLayout.addView(dateLabel);
        headerLayout.addView(dropdownIcon);
        sectionLayout.addView(headerLayout);

        // Details
        LinearLayout detailLayout = new LinearLayout(this);
        detailLayout.setOrientation(LinearLayout.VERTICAL);
        detailLayout.setVisibility(View.GONE);
        detailLayout.setPadding(16, 0, 16, 16);

        int score = 0;
        int i = 1;
        for (HistoryEntry q : attempt.getValue()) {
            CardView card = new CardView(this);
            card.setCardElevation(4f);
            card.setRadius(16f);
            card.setUseCompatPadding(true);
            card.setCardBackgroundColor(getResources().getColor(android.R.color.white));

            LinearLayout cardContent = new LinearLayout(this);
            cardContent.setOrientation(LinearLayout.VERTICAL);
            cardContent.setPadding(24, 24, 24, 24);

            TextView questionText = new TextView(this);
            questionText.setText(i++ + ". " + q.question);
            questionText.setTextColor(getResources().getColor(android.R.color.black));
            questionText.setTextSize(16f);
            cardContent.addView(questionText);

            TextView topicText = new TextView(this);
            topicText.setText("Topic: " + q.topic);
            topicText.setTextColor(getResources().getColor(android.R.color.black));
            topicText.setTextSize(14f);
            cardContent.addView(topicText);

            TextView userAnswer = new TextView(this);
            boolean isCorrect = q.userAnswer.equals(q.correctAnswer);
            userAnswer.setText("Your Answer: " + q.userAnswer);
            userAnswer.setTextColor(getResources().getColor(isCorrect ?
                    android.R.color.holo_green_dark : android.R.color.holo_red_dark));
            cardContent.addView(userAnswer);

            if (!isCorrect) {
                TextView correct = new TextView(this);
                correct.setText("Correct Answer: " + q.correctAnswer);
                correct.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                cardContent.addView(correct);
            } else {
                score++;
            }

            card.addView(cardContent);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 12, 0, 0);
            detailLayout.addView(card, layoutParams);
        }

        TextView scoreLabel = new TextView(this);
        scoreLabel.setText("Score: " + score + "/" + attempt.getValue().size());
        scoreLabel.setTextColor(getResources().getColor(android.R.color.white));
        scoreLabel.setTextSize(16f);
        scoreLabel.setTypeface(null, android.graphics.Typeface.BOLD);
        scoreLabel.setPadding(0, 16, 0, 0);
        detailLayout.addView(scoreLabel);

        sectionLayout.addView(detailLayout);

        headerLayout.setOnClickListener(v -> {
            if (detailLayout.getVisibility() == View.GONE) {
                detailLayout.setVisibility(View.VISIBLE);
                dropdownIcon.setImageResource(android.R.drawable.arrow_up_float);
            } else {
                detailLayout.setVisibility(View.GONE);
                dropdownIcon.setImageResource(android.R.drawable.arrow_down_float);
            }
        });

        return sectionLayout;
    }

    private void promptForPasswordAndDelete() {
        EditText passwordInput = new EditText(this);
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordInput.setHint("Enter your password");

        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete all your history? Enter your password to confirm:")
                .setView(passwordInput)
                .setPositiveButton("Delete", (dialog, which) -> {
                    String enteredPassword = passwordInput.getText().toString();
                    new Thread(() -> {
                        User user = db.userDao().findByUsername(username);
                        if (user != null && user.password.equals(enteredPassword)) {
                            db.historyDao().deleteAllForUser(username);
                            db.userDao().resetQuizStats(username);
                            runOnUiThread(() -> {
                                Toast.makeText(this, "History deleted successfully", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                loadHistory();
                            });
                        } else {
                            runOnUiThread(() ->
                                    Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show());
                        }
                    }).start();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
