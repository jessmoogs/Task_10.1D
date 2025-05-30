package com.example.improvedpersonalisedlearningexperienceapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history")
public class HistoryEntry {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String username;
    public String question;
    public String userAnswer;
    public String correctAnswer;
    public String topic;
    public long timestamp;
}
