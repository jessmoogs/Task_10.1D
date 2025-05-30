package com.example.improvedpersonalisedlearningexperienceapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String firstName;
    public String lastName;
    public String username;
    public String email;
    public String password;
    public String phone;
    public String interests;
    public int totalQuestions;
    public int correctAnswers;
    public String plan; // default value should be "Free"

}

