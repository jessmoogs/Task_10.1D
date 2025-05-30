package com.example.improvedpersonalisedlearningexperienceapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.improvedpersonalisedlearningexperienceapp.R;
import com.example.improvedpersonalisedlearningexperienceapp.database.AppDatabase;
import com.example.improvedpersonalisedlearningexperienceapp.model.User;

public class SignupActivity extends AppCompatActivity {

    EditText firstNameInput, lastNameInput, usernameInput, emailInput, confirmEmailInput,
            phoneInput, passwordInput, confirmPasswordInput;
    Button signupButton;
    TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstNameInput = findViewById(R.id.editFirstName);
        lastNameInput = findViewById(R.id.editLastName);
        usernameInput = findViewById(R.id.editUsername);
        emailInput = findViewById(R.id.editEmail);
        confirmEmailInput = findViewById(R.id.editConfirmEmail);
        phoneInput = findViewById(R.id.editPhone);
        passwordInput = findViewById(R.id.editPassword);
        confirmPasswordInput = findViewById(R.id.editConfirmPassword);
        signupButton = findViewById(R.id.signupButton);
        loginLink = findViewById(R.id.loginLink);

        signupButton.setOnClickListener(v -> {
            String firstName = firstNameInput.getText().toString().trim();
            String lastName = lastNameInput.getText().toString().trim();
            String username = usernameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String confirmEmail = confirmEmailInput.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() ||
                    email.isEmpty() || confirmEmail.isEmpty() || phone.isEmpty() ||
                    password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!email.equals(confirmEmail)) {
                Toast.makeText(this, "Emails do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(this);
                User existingUser = db.userDao().findByUsername(username);

                runOnUiThread(() -> {
                    if (existingUser != null) {
                        Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        User user = new User();
                        user.firstName = firstName;
                        user.lastName = lastName;
                        user.username = username;
                        user.email = email;
                        user.password = password;
                        user.phone = phone;
                        user.interests = ""; // To be updated in InterestsActivity

                        new Thread(() -> {
                            db.userDao().insert(user);
                            runOnUiThread(() -> {
                                Intent intent = new Intent(SignupActivity.this, InterestsActivity.class);
                                intent.putExtra("username", username);
                                startActivity(intent);
                                finish();
                            });
                        }).start();
                    }
                });
            }).start();
        });

        loginLink.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }
}
