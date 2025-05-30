package com.example.improvedpersonalisedlearningexperienceapp.activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.improvedpersonalisedlearningexperienceapp.R;
import com.example.improvedpersonalisedlearningexperienceapp.database.AppDatabase;
import com.example.improvedpersonalisedlearningexperienceapp.model.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class UpgradeActivity extends AppCompatActivity {

    private String username;
    private AppDatabase db;
    private TextView currentPlanText;
    private Button starterPurchase, intermediatePurchase, advancedPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        db = AppDatabase.getInstance(this);
        username = getIntent().getStringExtra("username");

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        currentPlanText = findViewById(R.id.currentPlanText);
        starterPurchase = findViewById(R.id.starterPurchase);
        intermediatePurchase = findViewById(R.id.intermediatePurchase);
        advancedPurchase = findViewById(R.id.advancedPurchase);

        loadCurrentPlan();

        starterPurchase.setOnClickListener(v -> showFakePaySheet("Starter"));
        intermediatePurchase.setOnClickListener(v -> showFakePaySheet("Intermediate"));
        advancedPurchase.setOnClickListener(v -> showFakePaySheet("Advanced"));

        TextView revertToFreePlan = findViewById(R.id.revertToFreePlan);
        revertToFreePlan.setOnClickListener(v -> {
            new Thread(() -> {
                db.userDao().updatePlan(username, "Free");
                runOnUiThread(() -> {
                    Toast.makeText(this, "Plan reverted to Free.", Toast.LENGTH_SHORT).show();
                    currentPlanText.setText("You are currently on the Free plan.");
                    updateButtonStates("Free");
                    setResult(RESULT_OK);
                });
            }).start();
        });
    }

    private void loadCurrentPlan() {
        new Thread(() -> {
            User user = db.userDao().findByUsername(username);
            runOnUiThread(() -> {
                String plan = (user != null && user.plan != null && !user.plan.isEmpty()) ? user.plan : "Free";
                currentPlanText.setText("You are currently on the " + plan + " plan.");
                updateButtonStates(plan);
            });
        }).start();
    }

    private void showFakePaySheet(String planLevel) {
        View sheetView = LayoutInflater.from(this).inflate(R.layout.fake_pay_sheet, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(sheetView);

        TextView paymentAmount = sheetView.findViewById(R.id.paymentAmount);
        Button confirmBtn = sheetView.findViewById(R.id.confirmPayBtn);

        switch (planLevel) {
            case "Starter":
                paymentAmount.setText("Plan: Starter - $4.99");
                break;
            case "Intermediate":
                paymentAmount.setText("Plan: Intermediate - $9.99");
                break;
            case "Advanced":
                paymentAmount.setText("Plan: Advanced - $14.99");
                break;
        }

        confirmBtn.setOnClickListener(view -> {
            dialog.dismiss();
            simulatePayment(planLevel);
        });

        dialog.show();
    }

    private void simulatePayment(String planLevel) {
        new Handler().postDelayed(() -> {
            new Thread(() -> {
                db.userDao().updatePlan(username, planLevel);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Purchase successful: " + planLevel + " plan activated!", Toast.LENGTH_LONG).show();
                    currentPlanText.setText("You are currently on the " + planLevel + " plan.");
                    updateButtonStates(planLevel);
                    setResult(RESULT_OK); // notify ProfileActivity of plan update
                });
            }).start();
        }, 2000); // simulate processing delay
    }

    private void updateButtonStates(String activePlan) {
        // Reset all buttons
        resetButton(starterPurchase, "Starter", "$4.99");
        resetButton(intermediatePurchase, "Intermediate", "$9.99");
        resetButton(advancedPurchase, "Advanced", "$14.99");

        // Disable active one
        switch (activePlan) {
            case "Starter":
                markButtonAsActivated(starterPurchase);
                break;
            case "Intermediate":
                markButtonAsActivated(intermediatePurchase);
                break;
            case "Advanced":
                markButtonAsActivated(advancedPurchase);
                break;
        }
    }

    private void resetButton(Button button, String label, String price) {
        button.setEnabled(true);
        button.setText("Purchase");
        button.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_green_light));
        button.setTextColor(getResources().getColor(android.R.color.black));
    }

    private void markButtonAsActivated(Button button) {
        button.setEnabled(false);
        button.setText("Activated");
        button.setBackgroundTintList(getResources().getColorStateList(android.R.color.darker_gray));
        button.setTextColor(getResources().getColor(android.R.color.white));
    }
}
